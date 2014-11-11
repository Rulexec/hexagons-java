package by.muna.io;

import java.util.LinkedList;
import java.util.Queue;

public class AsyncStreamUtil {
    public static void pipe(IAsyncByteInputStream input, IAsyncByteOutputStream output) {
        class Container {
            IByteReader reader;
            IByteWriter writer;
        }

        Container c = new Container();

        input.onCanRead(reader -> {
            c.reader = reader;
            if (c.writer != null) {
                reader.read(c.writer);
            }

            output.requestWriting();

            return false;
        });
        output.onCanWrite(writer -> {
            c.writer = writer;
            if (c.reader != null) {
                writer.write(c.reader);
            }

            input.requestReading();

            return false;
        });

        input.requestReading();
        output.requestWriting();
    }
}
