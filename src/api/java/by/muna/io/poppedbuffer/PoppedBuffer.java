package by.muna.io.poppedbuffer;

public abstract class PoppedBuffer {
    private BufferType bufferType;

    public PoppedBuffer(BufferType bufferType) {
        this.bufferType = bufferType;
    }

    public BufferType getBufferType() {
        return this.bufferType;
    }
}
