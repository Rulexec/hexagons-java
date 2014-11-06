package by.muna.monads;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Actually it must be abstract class with final methods. But then we cannot create anonymous implementation with lambda syntax.
 * @param <R>
 * @param <E>
 */
@FunctionalInterface
public interface IAsyncMonad<R, E> {
    default <NR> IAsyncMonad<NR, E> bind(Function<R, IAsyncMonad<NR, E>> f) {
        return this.bind(f, x -> x);
    }
    default <NR, NE> IAsyncMonad<NR, NE> bind(Function<R, IAsyncMonad<NR, NE>> f, Function<E, NE> convertError) {
        return callback -> IAsyncMonad.this.run((x, e) -> {
            if (e == null) {
                IAsyncMonad<NR, NE> m = f.apply(x);
                m.run(callback);
            } else {
                callback.accept(null, convertError.apply(e));
            }
        });
    }

    default <NR> IAsyncMonad<NR, E> skip(IAsyncMonad<NR, E> m) {
        return callback -> IAsyncMonad.this.run((x, e) -> {
            if (e == null) {
                m.run(callback);
            } else {
                callback.accept(null, e);
            }
        });
    }

    default <NR> IAsyncMonad<NR, E> fmap(Function<R, NR> f) {
        return callback -> IAsyncMonad.this.run((x, e) -> {
            if (e == null) {
                callback.accept(f.apply(x), null);
            } else {
                callback.accept(null, e);
            }
        });
    }

    public void run(BiConsumer<R, E> callback);
}
