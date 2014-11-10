package by.muna.io;

import by.muna.monads.IAsyncFuture;

import java.util.function.Consumer;
import java.util.function.Function;

public interface IAsyncByteInputStream {
    void requestReading();

    /**
     *
     * @param reader returns true, if want read more
     */
    void onCanRead(Function<IByteReader, Boolean> reader);

    boolean isEnded();
    IAsyncFuture<Object> end();
    IAsyncFuture<Object> onEnd();
}