package by.muna.buffers.bytes;

public interface IBytesBufferPart {
    /**
     * @return Offset in the buffer itself.
     */
    int totalOffset();

    byte[] getBytes();

    /**
     * @return offset in .getBytes()
     */
    int offset();
    int length();
}
