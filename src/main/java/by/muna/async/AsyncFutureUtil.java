package by.muna.async;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public static <T> IAsyncFuture<T> parallel(Collection<IAsyncFuture<T>> futures) {
        return AsyncFutureUtil.parallel(futures, x -> x != null);
    }
    public static <T> IAsyncFuture<T> parallel(Collection<IAsyncFuture<T>> futures, Predicate<T> isFinal) {
        return AsyncFutureUtil.cannotBeStopped(callback -> {
            AtomicBoolean finished = new AtomicBoolean(false);
            AtomicInteger finishedCount = new AtomicInteger(0);

            int count = futures.size();

            for (IAsyncFuture<T> future : futures) {
                future.run(x -> {
                    if (finished.get()) return;

                    if (isFinal.test(x)) {
                        if (!finished.compareAndSet(false, true)) return;

                        callback.accept(x);
                    } else {
                        int n = finishedCount.incrementAndGet();
                        if (n == count) {
                            callback.accept(x);
                        }
                    }
                });
            }
        });
    }

    public static <T> IAsyncFuture<T> cannotBeStopped(Consumer<Consumer<T>> consumer) {
        return callback -> {
            consumer.accept(callback);
            return new IAsyncRunning() {};
        };
    }
}
