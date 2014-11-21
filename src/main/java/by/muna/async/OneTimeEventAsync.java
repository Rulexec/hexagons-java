package by.muna.async;

import by.muna.data.IEither;
import by.muna.data.either.EitherLeft;
import by.muna.data.either.EitherRight;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BiConsumer;

public class OneTimeEventAsync<R, E> implements IAsync<R, E> {
    private IEither<Queue<BiConsumer<R, E>>, IEither<E, R>> listenersOrValue = new EitherLeft<>(new LinkedList<>());

    public OneTimeEventAsync() {}

    @Override
    public IAsyncRunning run(BiConsumer<R, E> callback) {
        if (this.listenersOrValue.isRight()) {
            IEither<E, R> r = this.listenersOrValue.getRight();
            callback.accept(r.getRight(), r.getLeft());
            return AsyncRunningUtil.alreadyFinished();
        } else {
            synchronized (this) {
                if (this.listenersOrValue.isRight()) {
                    IEither<E, R> r = this.listenersOrValue.getRight();
                    callback.accept(r.getRight(), r.getLeft());
                    return AsyncRunningUtil.alreadyFinished();
                } else {
                    this.listenersOrValue.getLeft().add(callback);
                }
            }
        }
    }

    /**
     * Can be called only once, else noop.
     * @param value
     */
    public void event(R value, E error) {
        Queue<BiConsumer<R, E>> listeners;

        synchronized (this) {
            if (this.listenersOrValue.isRight()) return;

            listeners = this.listenersOrValue.getLeft();
            this.listenersOrValue = new EitherRight<>(error == null ? new EitherRight<>(value) : new EitherLeft<>(error));
        }

        for (BiConsumer<R, E> listener : listeners) listener.accept(value, error);
    }

    public boolean isEventHappened() {
        synchronized (this) {
            return this.listenersOrValue.isRight();
        }
    }
}
