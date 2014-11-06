package by.muna.data;

public class Single<F> implements ISingleMutable<F> {
    protected F first;

    public Single(F value) {
        this.first = value;
    }

    @Override
    public F getFirst() {
        return this.first;
    }

    @Override
    public void setFirst(F value) {
        this.first = value;
    }
}
