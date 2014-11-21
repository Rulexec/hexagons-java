package by.muna.async;

import java.util.function.BiConsumer;

public class AsyncPure<T, E> implements IAsync<T, E> {
    private T value;
    private E error;

    public AsyncPure(T value) {
        this.value = value;
    }
    public AsyncPure(T value, E error) {
        this.value = value;
        this.error = error;
    }

    @Override
    public IAsyncRunning run(BiConsumer<T, E> f) {
        f.accept(this.value, this.error);
        return AsyncRunningUtil.alreadyFinished();
    }
}
