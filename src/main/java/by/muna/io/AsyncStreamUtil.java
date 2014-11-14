package by.muna.io;

import java.util.function.BiConsumer;

public class AsyncStreamUtil {
    public static void pipe(IAsyncByteInputStream input, IAsyncByteOutputStream output) {
        AsyncStreamUtil.pipe(input, output, true);
    }

    public static void pipe(IAsyncByteInputStream input, IAsyncByteOutputStream output, boolean closeOutputIfInputClosed)
    {
        class Container {
            IByteReader reader;
            IByteWriter writer;
        }

        Container c = new Container();

        input.onCanRead(reader -> {
            if (reader.isEnded()) {
                synchronized (c) { c.reader = null; }
                output.requestWriting(false);
                return false;
            }

            c.reader = reader;
            synchronized (c) {
                if (c.writer != null) {
                    reader.read(c.writer);
                }
            }

            output.requestWriting();

            return false;
        });
        output.onCanWrite(writer -> {
            if (writer.isEnded()) {
                synchronized (c) { c.writer = null; }
                input.requestReading(false);
                return false;
            }

            c.writer = writer;
            synchronized (c) {
                if (c.reader != null) {
                    // TODO: it's very dangerous to use old reader, because of our InputStreamWithReturnableInput
                    // implementation, for example. Here can be outdated reader
                    // (if input was returned and after that onCanWrite event happened)
                    // I must rewrite InputStreamWithReturnableInput in such order to exclude this possibility
                    // and guarantee, that reader, passed to onCanRead never outdates.
                    writer.write(c.reader);
                }
            }

            input.requestReading();

            return false;
        });

        if (closeOutputIfInputClosed) input.onEnd().skip(output.end());

        input.requestReading();
        output.requestWriting();
    }
}
