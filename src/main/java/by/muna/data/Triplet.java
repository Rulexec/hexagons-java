package by.muna.data;

public class Triplet<F, S, T> extends Pair<F, S> implements ITripletMutable<F, S, T> {
    protected T third;

    public Triplet(F first, S second, T third) {
        super(first, second);
        this.third = third;
    }

    @Override public T getThird() {
        return this.third;
    }

    @Override public void setThird(T value) {
        this.third = value;
    }
}
