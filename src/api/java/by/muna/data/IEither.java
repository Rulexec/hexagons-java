package by.muna.data;

public interface IEither<L, R> {
    boolean isRight();
    L getLeft();
    R getRight();
}
