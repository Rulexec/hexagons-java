package by.muna.io;

import by.muna.async.IAsyncFuture;
import by.muna.buffers.bytes.IBytesBuffer;
import by.muna.io.output.IAsyncByteOutputStreamOnWrite;
import by.muna.pubsub.ISubscription;

import java.util.function.Consumer;

public interface IAsyncByteOutputStream {
    ISubscription onCanWrite(Consumer<IAsyncByteOutputStreamOnWrite> callback);

    int write(IBytesBuffer buffer);

    boolean isEnded();
    IAsyncFuture<Object> end();
    IAsyncFuture<Object> onEnd();

    /**
     * Hint to output stream, that next write potentially will be >= size
     * @param size
     */
    default void hintCapacity(int size) {}
}