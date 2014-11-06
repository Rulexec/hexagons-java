package by.muna.monads;

import java.util.function.Function;

public class AsyncFutureUtil {
    public static <L, M, R> Function<L, IAsyncFuture<R>> composeFL(
        Function<L, IAsyncFuture<M>> left,
        Function<M, IAsyncFuture<R>> right)
    {
        return a -> left.apply(a).bind(right);
    }

    public static <T, V> Function<T, IAsyncFuture<V>> liftF(Function<T, V> f) {
        return x -> new AsyncFuturePure<>(f.apply(x));
    }
}
