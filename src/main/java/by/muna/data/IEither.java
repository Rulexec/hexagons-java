package by.muna.data;

import java.util.function.Function;

public interface IEither<L, R> {
    boolean isRight();
    L getLeft();
    R getRight();
}
