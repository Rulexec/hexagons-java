package by.muna.io;

import java.util.LinkedList;
import java.util.Queue;

public class AsyncStreamUtil {
    public static void pipe(IAsyncByteInputStream input, IAsyncByteOutputStream output) {
        // TODO: Implementation is blocking on I/O and cannot do it simultaneously, even if reading is ahead of writing

        final int BUFFER_SIZE = 1024;

        class Container {
            int buffersCount = 0;

            byte[] buffer = new byte[BUFFER_SIZE];
            int offset = 0;

            int writingOffset = 0;

            boolean endAfterWriting = false;
        }

        Queue<byte[]> buffers = new LinkedList<>();

        Container c = new Container();

        input.onCanRead(reader -> {
            synchronized (c) {
                int readed = reader.read(c.buffer, c.offset);

                if (readed != 0) {
                    c.offset += readed;

                    if (c.offset == BUFFER_SIZE) {
                        buffers.add(c.buffer);
                        c.buffersCount++;

                        c.buffer = new byte[BUFFER_SIZE];
                        c.offset = 0;
                    }

                    output.requestWriting();
                }

                if (reader.isEnded()) {
                    if (c.buffersCount == 0 && (c.offset - c.writingOffset) == 0) {
                        output.end();
                    } else {
                        c.endAfterWriting = true;
                    }
                }

                return true;
            }
        });
        output.onCanWrite(writer -> {
            synchronized (c) {
                while (!buffers.isEmpty()) {
                    byte[] buffer = buffers.peek();
                    int written = writer.write(buffer, c.writingOffset);

                    if (written == 0) return false;

                    c.writingOffset += written;

                    if (c.writingOffset == BUFFER_SIZE) {
                        buffers.poll();
                        c.writingOffset = 0;
                    }
                }

                if (c.offset - c.writingOffset > 0) {
                    int written = writer.write(c.buffer, c.writingOffset, c.offset - c.writingOffset);
                    c.writingOffset += written;
                }

                boolean done = c.buffersCount == 0 && (c.offset - c.writingOffset) == 0;

                if (done && c.endAfterWriting) {
                    output.end();
                }

                return !done;
            }
        });

        input.requestReading();
        output.requestWriting();
    }
}
