package by.muna.buffers.bytes;

import java.util.Iterator;

/**
 * <p>Byte buffer, that has one writer and one reader&amp;writer.
 * Writing can be done only to the end of the buffer,
 * writer can only write and nothing else.</p>
 *
 * <p>Reader&amp;Writer can read, write, remove bytes from buffer,
 * and also have a position, that can move around.</p>
 *
 * <p>Position can be used by implementation to optimize getting new data from the buffer
 * (for example, if this implementation based on linked list, it can hold ListIterator near position).</p>
 */
public interface IBytesBuffer {
    int size();

    default void take(int size) {
        this.remove(0, size);
    }
    void remove(int offset, int length);

    void position(int pos);
    int position();

    /**
     * @return Part of the buffer after the position, not shifts the position.
     */
    default IBytesBufferPart get() {
        int pos = this.position();
        return this.get(pos, this.size() - pos).next();
    }

    /**
     * @param offset
     * @param length
     * @return Parts of the buffer, not shifts the position.
     *         .remove() on the iterator will really remove part from the buffer.
     */
    Iterator<IBytesBufferPart> get(int offset, int length);

    default IBytesBufferPart blockingGet() throws InterruptedException {
        return this.blockingGet(this.position());
    }

    /**
     * Waits for availability of part from specified offset and returns it.
     * @param offset
     * @return
     * @throws InterruptedException
     */
    IBytesBufferPart blockingGet(int offset) throws InterruptedException;

    /**
     * @return True, if you have successfully acquired exclusive appending rights.
     */
    boolean startAppending();

    /**
     * Waits for acquiring exclusive appending rights.
     * @return True, if you have successfully acquired exclusive appending rights,
     *         False, if someone called {@link #wakeup()} method.
     * @throws InterruptedException
     */
    boolean blockingStartAppending() throws InterruptedException;

    /**
     * @param usedInLastPart Size of last buffer part, that actually used.
     */
    void finishAppending(int usedInLastPart);

    /**
     * If there are some running blocking methods,
     * then all blockingGet returns null, and all blockingStartAppending returns false.
     */
    void wakeup();

    default IBytesBufferPart append() {
        return this.append(0);
    }
    /**
     * Creates new buffer part, trying to satisfy size = length.
     * Size can be lesser, but not bigger, than length.
     *
     * Buffer size will incremented by size of returned part.
     * You need call .finishAppending with actually used length in the last part,
     * that you have created by this method.
     *
     * If you appending not in transaction (not in start/finish block),
     * then you must call .remove(offset, len), where offset calculated from totalOffset of part.
     * @param length If length == 0, than size determined by buffer implementation.
     * @return
     */
    IBytesBufferPart append(int length);
}