package by.muna.monads;

import java.util.function.Function;

public class AsyncFutureUtil {
    public static <L, M, R> Function<L, AsyncFuture<R>> composeFL(
        Function<L, AsyncFuture<M>> left,
        Function<M, AsyncFuture<R>> right)
    {
        return a -> left.apply(a).bind(right);
    }

    public static <T, V> Function<T, AsyncFuture<V>> liftF(Function<T, V> f) {
        return x -> new AsyncFuturePure<>(f.apply(x));
    }
}
