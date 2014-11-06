package by.muna.monads;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface AsyncFuture<T> {
    default <V> AsyncFuture<V> bind(Function<T, AsyncFuture<V>> f) {
        return callback -> AsyncFuture.this.run(x -> f.apply(x).run(callback));
    }

    default <V> AsyncFuture<V> skip(AsyncFuture<V> m) {
        return callback -> AsyncFuture.this.run(x -> m.run(callback));
    }

    default <V> AsyncFuture<V> fmap(Function<T, V> f) {
        return callback -> AsyncFuture.this.run(x -> callback.accept(f.apply(x)));
    }

    public void run(Consumer<T> callback);
}
