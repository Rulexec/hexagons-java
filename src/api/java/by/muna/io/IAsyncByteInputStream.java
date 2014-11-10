package by.muna.io;

import java.util.function.Consumer;
import java.util.function.Function;

public interface IAsyncByteInputStream {
    void requestReading();

    /**
     *
     * @param reader returns true, if want read more
     */
    void onCanRead(Function<IByteReader, Boolean> reader);

    void end();
    boolean isEnded();
}
