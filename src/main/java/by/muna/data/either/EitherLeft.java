package by.muna.data.either;

import by.muna.data.IEither;

public class EitherLeft<L, R> implements IEither<L, R> {
    private L value;

    public EitherLeft(L value) {
        this.value = value;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public L getLeft() {
        return this.value;
    }

    @Override
    public R getRight() {
        return null;
    }
}
