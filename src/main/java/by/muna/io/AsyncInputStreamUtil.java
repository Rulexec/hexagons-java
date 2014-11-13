package by.muna.io;

import by.muna.monads.IAsyncFuture;

import java.util.function.Function;

public class AsyncInputStreamUtil {
    public static IAsyncByteInputStream empty() {
        return new IAsyncByteInputStream() {
            private Function<IByteReader, Boolean> listener;

            @Override
            public void requestReading(boolean request) {
                if (request) this.listener.apply(ByteReaderUtil.empty());
            }

            @Override
            public void onCanRead(Function<IByteReader, Boolean> listener) {
                this.listener = listener;
            }

            @Override
            public boolean isEnded() {
                return true;
            }

            @Override
            public IAsyncFuture<Object> end() {
                return callback -> callback.accept(null);
            }

            @Override
            public IAsyncFuture<Object> onEnd() {
                return callback -> callback.accept(null);
            }
        };
    }
}
