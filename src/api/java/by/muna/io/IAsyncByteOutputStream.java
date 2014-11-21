package by.muna.io;

import by.muna.async.IAsyncFuture;

import java.util.function.Function;

public interface IAsyncByteOutputStream {
    default void requestWriting() { this.requestWriting(true); }
    /**
     * This method can be called only after setting consumer, else behaviour is undefined.
     * @param request false, if want to cancel request
     */
    void requestWriting(boolean request);

    /**
     * You must use IByteWriter methods only in scope of consumer execution. Else behaviour is undefined.
     * @param writer returns true, if want write more
     */
    void onCanWrite(Function<IByteWriter, Boolean> writer);

    boolean isEnded();
    IAsyncFuture<Object> end();
    IAsyncFuture<Object> onEnd();
}