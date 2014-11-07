package by.muna.io.poppedbuffer;

public class PoppedRawBytes extends PoppedBuffer {
    private byte[] buffer;
    private int offset, length;

    public PoppedRawBytes(byte[] buffer, int offset, int length) {
        super(BufferType.RAW_BYTES);

        this.buffer = buffer;
        this.offset = offset;
        this.length = length;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }
}

