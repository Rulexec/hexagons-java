package by.muna.monads;

import java.util.function.Function;

public class AsyncMonadUtil {
    public static <L, M, R, E> Function<L, IAsyncMonad<R, E>> composeL(
            Function<L, IAsyncMonad<M, E>> left,
            Function<M, IAsyncMonad<R, E>> right)
    {
        return a -> left.apply(a).bind(right);
    }

    public static <R, NR, E> Function<R, IAsyncMonad<NR, E>> lift(Function<R, NR> f) {
        return x -> new AsyncMonadPure<>(f.apply(x));
    }

    // just because lambda syntax is not perfect
    public static <L, M, R> Function<L, R> composeFL(Function<L, M> left, Function<M, R> right) {
        return left.andThen(right);
    }
}