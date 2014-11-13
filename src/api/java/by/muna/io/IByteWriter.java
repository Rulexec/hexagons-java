package by.muna.io;

import java.nio.ByteBuffer;

/**
 * Must implement or write(byte[], ...), or write(ByteBuffer)
 */
public interface IByteWriter {
    /**
     * This is only hint, zero means not no possible output, but it can mean unknown possible output size.
     * @return
     */
    default int available() { return 0; }

    default int write(byte[] buffer) {
        return this.write(buffer, 0, buffer.length);
    }
    default int write(byte[] buffer, int offset) {
        return this.write(buffer, offset, buffer.length - offset);
    }
    default int write(byte[] buffer, int offset, int length) {
        return this.write(ByteBuffer.wrap(buffer, offset, length));
    }

    /**
     * if not to implement this method and bytebuffer don't has backing array, then it will be readed
     * and then position will be set to initial position.
     * @param buffer
     * @return
     */
    default int write(ByteBuffer buffer) {
        int position = buffer.position();
        int written = this.write(new IByteReader() {
            @Override
            public int read(byte[] buff, int offset, int length) {
                int canRead = Math.min(length, buffer.remaining());
                buffer.get(buff, offset, canRead);
                return canRead;
            }

            @Override
            public int read(ByteBuffer buff) {
                int canRead = Math.min(buff.remaining(), buffer.remaining());
                ByteBuffer b = (ByteBuffer) buffer.slice().limit(buffer.position() + canRead);
                buff.put(b);
                return canRead;
            }

            @Override
            public void end() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isEnded() {
                return false;
            }
        });

        buffer.position(position + written);
        return written;
    }

    /**
     * Caller of this method must use return of this method, because implementation can get from reader more data,
     * than actually used.
     * @param reader
     * @return
     */
    default int write(IByteReader reader) {
        int totalWritten = 0;

        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];

        while (true) {
            int readed = reader.read(buffer);
            if (readed == 0) break;

            int written = this.write(buffer, 0, readed);
            totalWritten += written;
            if (readed > written) break;
        }

        return totalWritten;
    }

    void end();
    boolean isEnded();
}
