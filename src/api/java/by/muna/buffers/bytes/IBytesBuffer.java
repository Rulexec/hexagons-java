package by.muna.buffers.bytes;

import java.util.Iterator;

public interface IBytesBuffer {
    int size();

    void take(int size);
    void drop(int size);

    void position(int pos);
    int position();

    /**
     * @return Part of the buffer after the position, not shifts the position.
     */
    IBytesBufferPart get();

    /**
     * @param offset
     * @param length
     * @return Parts of the buffer, not shifts the position.
     */
    Iterator<IBytesBufferPart> get(int offset, int length);
}
