package by.muna.io;

import by.muna.monads.IAsyncFuture;

import java.util.function.Function;

public interface IAsyncByteOutputStream {
    /**
     * This method can be called only after setting consumer, else behaviour is undefined.
     */
    void requestWriting();

    /**
     * You must use IByteWriter methods only in scope of consumer execution. Else behaviour is undefined.
     * @param writer returns true, if want write more
     */
    void onCanWrite(Function<IByteWriter, Boolean> writer);

    boolean isEnded();
    IAsyncFuture<Object> end();
    IAsyncFuture<Object> onEnd();
}