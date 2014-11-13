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
        int position = buffer.position();
        int readed = this.read(new IByteWriter() {
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
        buffer.position(readed);
        return readed;
    }

    /**
     * Caller of this method must use return of this method, because implementation can get from writer more data,
     * than actually used.
     * @param writer
     * @return
     */
    default int read(IByteWriter writer) {
        int totalReaded = 0;

        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];

        while (true) {
            int written = writer.write(buffer);
            if (written == 0) break;

            int readed = this.read(buffer, 0, written);
            totalReaded += readed;
            if (written > readed) break;
        }

        return totalReaded;
    }

    void end();
    boolean isEnded();
}
