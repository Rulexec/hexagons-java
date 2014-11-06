package by.muna.data.either;

import by.muna.data.IPair;

public class Pair<F, S> implements IPair<F, S> {
    private F first;
    private S second;

    public Pair(F first, S second) {
        this.first = first;
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
}
