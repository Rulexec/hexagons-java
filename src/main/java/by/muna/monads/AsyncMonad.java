package by.muna.monads;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Actually it must be abstract class with final methods. But then we cannot create anonymous implementation with lambda syntax.
 * @param <R>
 * @param <E>
 */
@FunctionalInterface
public interface AsyncMonad<R, E> {
    default <NR> AsyncMonad<NR, E> bind(Function<R, AsyncMonad<NR, E>> f) {
        return this.bind(f, x -> x);
    }
    default <NR, NE> AsyncMonad<NR, NE> bind(Function<R, AsyncMonad<NR, NE>> f, Function<E, NE> convertError) {
        return callback -> AsyncMonad.this.run((x, e) -> {
            if (e == null) {
                AsyncMonad<NR, NE> m = f.apply(x);
                m.run(callback);
            } else {
                callback.accept(null, convertError.apply(e));
            }
        });
    }

    default <NR> AsyncMonad<NR, E> skip(AsyncMonad<NR, E> m) {
        return callback -> AsyncMonad.this.run((x, e) -> {
            if (e == null) {
                m.run(callback);
            } else {
                callback.accept(null, e);
            }
        });
    }

    default <NR> AsyncMonad<NR, E> fmap(Function<R, NR> f) {
        return callback -> AsyncMonad.this.run((x, e) -> {
            if (e == null) {
                callback.accept(f.apply(x), null);
            } else {
                callback.accept(null, e);
            }
        });
    }

    public void run(BiConsumer<R, E> callback);
}
