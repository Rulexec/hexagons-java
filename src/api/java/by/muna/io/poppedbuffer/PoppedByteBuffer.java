package by.muna.io.poppedbuffer;

import java.nio.ByteBuffer;

public class PoppedByteBuffer extends PoppedBuffer {
    private ByteBuffer buffer;

    public PoppedByteBuffer(ByteBuffer buffer) {
        super(BufferType.BYTE_BUFFER);

        this.buffer = buffer;
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }
}
