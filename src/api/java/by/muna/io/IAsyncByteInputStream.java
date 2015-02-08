package by.muna.io;

import by.muna.async.IAsyncFuture;
import by.muna.buffers.bytes.IBytesBuffer;
import by.muna.io.input.IAsyncByteInputStreamOnRead;
import by.muna.pubsub.ISubscription;

import java.util.function.Consumer;

public interface IAsyncByteInputStream {
    ISubscription onCanRead(Consumer<IAsyncByteInputStreamOnRead> callback);

    IBytesBuffer read();

    boolean isEnded();
    IAsyncFuture<Object> end();
    IAsyncFuture<Object> onEnd();

    /**
     * Hint to the input stream, that bytes will be taken from buffer, when buffer size will &gt;= count.
     * @param count
     */
    default void hintBytesRequirement(int count) {}
}