package by.muna.io;

import by.muna.async.IAsyncFuture;

import java.util.function.Function;

public interface IAsyncByteInputStream {
    default void requestReading() { this.requestReading(true); }

    /**
     * @param request false, if want to cancel request
     */
    void requestReading(boolean request);

    /**
     *
     * @param reader returns true, if want read more (equivalent to call requestReading, but it can be more effective)
     */
    void onCanRead(Function<IByteReader, Boolean> reader);

    boolean isEnded();
    IAsyncFuture<Object> end();
    IAsyncFuture<Object> onEnd();
}