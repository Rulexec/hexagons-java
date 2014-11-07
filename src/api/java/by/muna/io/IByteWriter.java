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
        if (buffer.hasArray()) {
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
        }
    }

    void end();
    boolean isEnded();
}
