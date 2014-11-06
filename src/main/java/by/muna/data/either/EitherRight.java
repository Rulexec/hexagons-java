package by.muna.data.either;

import by.muna.data.IEither;

public class EitherRight<L, R> implements IEither<L, R> {
    private R value;

    public EitherRight(R value) {
        this.value = value;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public L getLeft() {
        return null;
    }

    @Override
    public R getRight() {
        return this.value;
    }
}
