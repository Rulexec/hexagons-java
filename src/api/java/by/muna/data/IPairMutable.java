package by.muna.data;

public interface IPairMutable<F, S> extends ISingleMutable<F>, IPair<F, S> {
    void setSecond(S value);
}
