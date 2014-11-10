package by.muna.io;

import java.nio.ByteBuffer;

/**
 * Must implement one of read methods
 */
public interface IByteReader {
    /**
     * This is only hint, zero means not no input, but it can mean unknown input size.
     * @return
     */
    default int available() { return 0; }

    default int read(byte[] buffer) {
        return this.read(buffer, 0, buffer.length);
    }
    default int read(byte[] buffer, int offset) {
        return this.read(buffer, offset, buffer.length - offset);
    }
    default int read(byte[] buffer, int offset, int length) {
        return this.read(ByteBuffer.wrap(buffer, offset, length));
    }

    default int read(ByteBuffer buffer) {
        /*final int BUFFER_SIZE = Math.max(1024, this.available());

        byte[] array = new byte[BUFFER_SIZE];
        int offset = 0;

        int totalReaded = 0;

        while (buffer.hasRemaining()) {
            int readed = this.read(array, offset, BUFFER_SIZE);
            if (readed == 0) break;

            buffer.put(array, offset, readed);

            offset += readed;
            if (offset == BUFFER_SIZE) offset = 0;

            totalReaded += readed;
        }

        return totalReaded;*/
        return this.read(new IByteWriter() {
            @Override
            public int write(ByteBuffer buff) {
                int canWrite = Math.min(buff.remaining(), buffer.remaining());
                ByteBuffer b = (ByteBuffer) buffer.slice().limit(buffer.position() + canWrite);
                buff.put(b);
                return canWrite;
            }

            @Override
            public int write(byte[] buff, int offset, int length) {
                int canWrite = Math.min(length, buffer.remaining());
                buffer.get(buff, offset, canWrite);
                return canWrite;
            }

            @Override public void end() { throw new UnsupportedOperationException(); }
            @Override public boolean isEnded() { return false; }
        });
    }

    default int read(IByteWriter writer) {
        int totalReaded = 0;

        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];

        while (true) {
            int written = writer.write(buffer);
            if (written == 0) break;

            totalReaded += this.read(buffer, 0, written);
        }

        return totalReaded;
    }

    void end();
    boolean isEnded();
}
