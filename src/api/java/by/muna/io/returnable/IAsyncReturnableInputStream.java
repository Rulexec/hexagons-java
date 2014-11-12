package by.muna.io.returnable;

import by.muna.io.ByteReaderUtil;
import by.muna.io.IAsyncByteInputStream;
import by.muna.io.IByteReader;
import by.muna.io.IByteWriter;
import by.muna.monads.IAsyncFuture;

import java.nio.ByteBuffer;
import java.util.function.Function;

public interface IAsyncReturnableInputStream extends IAsyncByteInputStream, IReturnableInput {
    @Override
    default void onCanRead(Function<IByteReader, Boolean> reader) {
        this.onCanReadReturnable(reader::apply);
    }

    default void onCanReadReturnable(Function<IReturnableByteReader, Boolean> listener) {
        this.onCanRead(reader -> listener.apply(new IReturnableByteReader() {
            @Override
            public int available() {
                return reader.available();
            }

            @Override
            public int read(byte[] buffer) {
                return reader.read(buffer);
            }

            @Override
            public int read(byte[] buffer, int offset) {
                return reader.read(buffer, offset);
            }

            @Override
            public int read(byte[] buffer, int offset, int length) {
                return reader.read(buffer, offset, length);
            }

            @Override
            public int read(ByteBuffer buffer) {
                return reader.read(buffer);
            }

            @Override
            public int read(IByteWriter writer) {
                return reader.read(writer);
            }

            @Override
            public void end() {
                reader.end();
            }

            @Override
            public boolean isEnded() {
                return reader.isEnded();
            }

            @Override
            public void returnInput(IByteReader rest) {
                IAsyncReturnableInputStream.this.returnInput(rest);
            }
        }));
    }

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
