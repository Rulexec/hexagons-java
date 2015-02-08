package by.muna.async;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>Actually it must be abstract class with final methods. But then we cannot create anonymous implementation with
 * lambda syntax.</p>
 *
 * <p>This is actually IAsyncFuture&lt;IEither&lt;E, R&gt;&gt;, that binds on R, and didn't nothing on E.</p>
 * @param <R>
 * @param <E>
 */
@FunctionalInterface
public interface IAsync<R, E> {
    default <NR> IAsync<NR, E> bind(Function<R, IAsync<NR, E>> f) {
        return this.bind(f, x -> x);
    }
    default <NR, NE> IAsync<NR, NE> bind(Function<R, IAsync<NR, NE>> f, Function<E, NE> convertError) {
        return callback -> IAsync.this.run((x, e) -> {
            if (e == null) {
                IAsync<NR, NE> m = f.apply(x);
                m.run(callback);
            } else {
                callback.accept(null, convertError.apply(e));
            }
        });
    }

    default <NR> IAsync<NR, E> skip(IAsync<NR, E> m) {
        return callback -> IAsync.this.run((x, e) -> {
            if (e == null) {
                m.run(callback);
            } else {
                callback.accept(null, e);
            }
        });
    }

    default <NR> IAsync<NR, E> fmap(Function<R, NR> f) {
        return callback -> IAsync.this.run((x, e) -> {
            if (e == null) {
                callback.accept(f.apply(x), null);
            } else {
                callback.accept(null, e);
            }
        });
    }

    public IAsyncRunning run(BiConsumer<R, E> callback);
}
