package by.muna.data;

import by.muna.data.IPair;

public class Pair<F, S> extends Single<F> implements IPairMutable<F, S> {
    protected S second;

    public Pair(F first, S second) {
        super(first);
        this.second = second;
    }

    @Override
    public F getFirst() {
        return this.first;
    }

    @Override
    public S getSecond() {
        return this.second;
    }

    @Override
    public void setSecond(S value) {
        this.second = value;
    }
}
