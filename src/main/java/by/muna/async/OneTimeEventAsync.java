package by.muna.async;

import by.muna.data.IEither;
import by.muna.data.IPair;
import by.muna.data.ITriplet;
import by.muna.data.either.EitherLeft;
import by.muna.data.either.EitherRight;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class OneTimeEventAsync<R, E> implements IAsync<R, E> {
    private OneTimeEventAsyncFuture<IEither<E, R>> event = new OneTimeEventAsyncFuture<>();

    public OneTimeEventAsync() {}

    @Override
    public IAsyncRunning run(BiConsumer<R, E> callback) {
        return event.run(x -> callback.accept(x.getRight(), x.getLeft()));
    }

    /**
     * Can be called only once, else noop.
     * @param value
     */
    public void event(R value, E error) {
        this.event.event(error == null ? new EitherRight<>(value) : new EitherLeft<>(error));
    }

    public boolean isEventHappened() {
        return this.event.isEventHappened();
    }
}
