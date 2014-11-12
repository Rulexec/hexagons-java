package by.muna.io.returnable;

import by.muna.io.ByteReaderUtil;
import by.muna.io.IAsyncByteInputStream;
import by.muna.io.IByteReader;
import by.muna.monads.IAsyncFuture;

import java.util.function.Function;

public interface IAsyncReturnableInputStream extends IAsyncByteInputStream, IReturnableInput {
    @Override
    default void onCanRead(Function<IByteReader, Boolean> reader) {
        this.onCanReadReturnable(reader::apply);
    }

    void onCanReadReturnable(Function<IReturnableByteReader, Boolean> reader);

    @Override
    default IAsyncFuture<Object> end() {
        return this.end(ByteReaderUtil.empty());
    }

    default IAsyncFuture<Object> end(IByteReader rest) {
        //this.returnInput(rest);
        return callback -> {
            this.returnInput(rest);
            this.end().run(callback);
        };
    }
}
