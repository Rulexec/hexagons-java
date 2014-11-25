package by.muna.data;

public interface IPairMutable<F, S> extends IPair<F, S>, ISingleMutable<F> {
    void setSecond(S value);
}
