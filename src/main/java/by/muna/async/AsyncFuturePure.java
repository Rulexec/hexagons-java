package by.muna.async;

import java.util.function.Consumer;

public class AsyncFuturePure<T> implements IAsyncFuture<T> {
    private T value;

    public AsyncFuturePure(T value) {
        this.value = value;
    }

    @Override
    public IAsyncRunning run(Consumer<T> callback) {
        callback.accept(this.value);
        return AsyncRunningUtil.alreadyFinished();
    }
}
