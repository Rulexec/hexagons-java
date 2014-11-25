package by.muna.data;

public interface ITripletMutable<F, S, T> extends ITriplet<F, S, T>, IPairMutable<F, S> {
    void setThird(T value);
}
