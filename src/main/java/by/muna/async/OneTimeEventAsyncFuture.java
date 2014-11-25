package by.muna.async;

import by.muna.data.IEither;
import by.muna.data.either.EitherLeft;
import by.muna.data.either.EitherRight;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class OneTimeEventAsyncFuture<T> implements IAsyncFuture<T> {
    private static class Listener<T> {
        Consumer<T> callback;
        AtomicBoolean runnedOrCancelled = new AtomicBoolean(false);
        AtomicBoolean cancelled = new AtomicBoolean(false);

        public Listener(Consumer<T> callback) {
            this.callback = callback;
        }
    }

    private volatile IEither<Queue<Listener<T>>, T> listenersOrValue = new EitherLeft<>(new LinkedList<>());

    public OneTimeEventAsyncFuture() {}

    @Override
    public IAsyncRunning run(Consumer<T> callback) {
        isLeft: if (!this.listenersOrValue.isRight()) {
            synchronized (this) {
                if (!this.listenersOrValue.isRight()) {
                    Listener<T> listener = new Listener<>(callback);

                    this.listenersOrValue.getLeft().add(listener);

                    return new IAsyncRunning() {
                        @Override public IAsyncFuture<AsyncCancellationResult> cancel() {
                            return callback -> {
                                // Maybe it can be done more optimally

                                boolean isCancelled = listener.cancelled.getAndSet(true);

                                if (isCancelled || listener.runnedOrCancelled.compareAndSet(false, true)) {
                                    callback.accept(AsyncCancellationResult.CANCELLED);
                                } else {
                                    callback.accept(AsyncCancellationResult.ALREADY_FINISHED);
                                }

                                return AsyncRunningUtil.alreadyFinished();
                            };
                        }
                    };
                } else {
                    break isLeft;
                }
            }
        }

        callback.accept(this.listenersOrValue.getRight());
        return AsyncRunningUtil.alreadyFinished();
    }

    /**
     * Can be called only once, else noop.
     * @param value
     */
    public void event(T value) {
        Queue<Listener<T>> listeners;

        synchronized (this) {
            if (this.listenersOrValue.isRight()) return;

            listeners = this.listenersOrValue.getLeft();
            this.listenersOrValue = new EitherRight<>(value);
        }

        for (Listener<T> listener : listeners) {
            boolean shouldRun = listener.runnedOrCancelled.compareAndSet(false, true);
            if (!shouldRun) continue;

            listener.callback.accept(value);
        }
    }

    public boolean isEventHappened() {
        if (this.listenersOrValue.isRight()) return true;

        synchronized (this) {
            return this.listenersOrValue.isRight();
        }
    }
}
