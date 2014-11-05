package by.muna.monads;

import java.util.function.BiConsumer;

public class AsyncMonadPure<T, E> implements AsyncMonad<T, E> {
    private T value;
    private E error;

    public AsyncMonadPure(T value) {
        this.value = value;
    }
    public AsyncMonadPure(T value, E error) {
        this.value = value;
        this.error = error;
    }

    @Override
    public void run(BiConsumer<T, E> f) {
        f.accept(this.value, this.error);
    }
}
