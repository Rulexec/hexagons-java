package by.muna.io;

import java.nio.ByteBuffer;

/**
 * Must implement or write(byte[], ...), or write(ByteBuffer)
 */
public interface IByteWriter {
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
        /*if (buffer.hasArray()) {
            byte[] array = buffer.array();
            int offset = buffer.arrayOffset();
            int length = buffer.remaining();

            return this.write(array, offset, length);
        } else {
            int position = buffer.position();

            byte[] array = new byte[buffer.remaining()];

            buffer.get(array);
            buffer.position(position);

            return this.write(array, 0, array.length);
        }*/
        return this.write(new IByteReader() {
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
    }

    default int write(IByteReader reader) {
        int totalWritten = 0;

        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];

        while (true) {
            int readed = reader.read(buffer);
            if (readed == 0) break;

            totalWritten += this.write(buffer, 0, readed);
        }

        return totalWritten;
    }

    void end();
    boolean isEnded();
}
