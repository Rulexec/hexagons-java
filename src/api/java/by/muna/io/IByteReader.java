package by.muna.io;

import by.muna.io.poppedbuffer.PoppedBuffer;

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
        final int BUFFER_SIZE = Math.max(1024, this.available());

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

        return totalReaded;
    }

    default boolean isEnd() { return false; }

    default boolean canPopBuffer() { return false; }

    /**
     * Pure wizardy, returns buffer, that uses underlying implementation.
     * And implementation guarantees, that this buffer will not used anymore by it.
     * @return
     */
    default PoppedBuffer popBuffer() { return null; }
}
