package by.muna.monads;

import java.util.function.Consumer;

public class AsyncFuturePure<T> implements AsyncFuture<T> {
    private T value;

    public AsyncFuturePure(T value) {
        this.value = value;
    }

    @Override
    public void run(Consumer<T> callback) {
        callback.accept(this.value);
    }
}
