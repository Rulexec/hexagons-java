package by.muna.async;

import java.util.function.Consumer;
import java.util.function.Function;

public interface IAsyncFuture<T> {
    default <V> IAsyncFuture<V> bind(Function<T, IAsyncFuture<V>> f) {
        return callback -> IAsyncFuture.this.run(x -> f.apply(x).run(callback));
    }

    default <V> IAsyncFuture<V> skip(IAsyncFuture<V> m) {
        return callback -> IAsyncFuture.this.run(x -> m.run(callback));
    }

    default <V> IAsyncFuture<V> fmap(Function<T, V> f) {
        return callback -> IAsyncFuture.this.run(x -> callback.accept(f.apply(x)));
    }

    public IAsyncRunning run(Consumer<T> callback);
}