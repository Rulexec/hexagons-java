package by.muna.io;

import by.muna.buffers.bytes.IBytesBuffer;

import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncStreamUtil {
    public static void pipe(IAsyncByteInputStream input, IAsyncByteOutputStream output) {
        AsyncStreamUtil.pipe(input, output, true);
    }

    public static void pipe(IAsyncByteInputStream input, IAsyncByteOutputStream output, boolean closeOutputIfInputClosed)
    {
        AtomicBoolean inPumping = new AtomicBoolean(false);

        AtomicBoolean canRead = new AtomicBoolean(false);
        AtomicBoolean canWrite = new AtomicBoolean(false);

        final IBytesBuffer buffer = input.read();

        class Container {
            void possible(boolean isRead) {
                if (isRead ? canWrite.get() : canRead.get()) {
                    while (true) {
                        if (!inPumping.compareAndSet(false, true)) return;

                        canRead.set(false);
                        canWrite.set(false);

                        while (true) {
                            int written = output.write(buffer);

                            if (written > 0) buffer.take(written);
                            else break;
                        }

                        inPumping.set(false);

                        if (!(canRead.get() && canWrite.get())) break;
                    }
                }
            }
        }
        Container c = new Container();

        input.onCanRead(readHints -> {
            if (canRead.compareAndSet(false, true)) c.possible(true);
        });
        output.onCanWrite(writeHints -> {
            if (canWrite.compareAndSet(false, true)) c.possible(false);
        });

        if (closeOutputIfInputClosed) input.onEnd().skip(output.end());
    }
}
