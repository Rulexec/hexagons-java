package by.muna.io;

import java.util.function.Consumer;

public interface IAsyncByteInputStream {
    void onData(Consumer<IByteReader> consumer);

    boolean isEnded();
}
