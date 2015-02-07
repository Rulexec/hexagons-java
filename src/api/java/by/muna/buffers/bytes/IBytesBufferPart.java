package by.muna.buffers.bytes;

public interface IBytesBufferPart {
    byte[] getBytes();
    int offset();
    int length();
}
