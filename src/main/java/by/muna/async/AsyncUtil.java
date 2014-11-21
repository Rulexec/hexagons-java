package by.muna.async;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class AsyncUtil {
    public static <L, M, R, E> Function<L, IAsync<R, E>> composeL(
            Function<L, IAsync<M, E>> left,
            Function<M, IAsync<R, E>> right)
    {
        return a -> left.apply(a).bind(right);
    }

    public static <R, NR, E> Function<R, IAsync<NR, E>> lift(Function<R, NR> f) {
        return x -> new AsyncPure<>(f.apply(x));
    }

    // just because lambda syntax is not perfect
    public static <L, M, R> Function<L, R> composeFL(Function<L, M> left, Function<M, R> right) {
        return left.andThen(right);
    }

    public static <R, E> IAsync<R, E> cannotBeStopped(Consumer<BiConsumer<R, E>> consumer) {
        return callback -> {
            consumer.accept(callback);
            return new IAsyncRunning() {};
        };
    }
}