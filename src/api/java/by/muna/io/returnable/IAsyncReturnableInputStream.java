package by.muna.io.returnable;

import by.muna.io.AsyncInputStreamUtil;
import by.muna.io.IAsyncByteInputStream;
import by.muna.io.IByteReader;
import by.muna.monads.IAsyncFuture;

import java.util.function.Function;

public interface IAsyncReturnableInputStream extends IAsyncByteInputStream {
    @Override
    default void onCanRead(Function<IByteReader, Boolean> reader) {
        this.onCanReadReturnable(reader::apply);
    }

    void onCanReadReturnable(Function<IReturnableByteReader, Boolean> reader);

    @Override
    default IAsyncFuture<Object> end() {
        return this.end(AsyncInputStreamUtil.empty());
    }

    IAsyncFuture<Object> end(IAsyncByteInputStream rest);
}
