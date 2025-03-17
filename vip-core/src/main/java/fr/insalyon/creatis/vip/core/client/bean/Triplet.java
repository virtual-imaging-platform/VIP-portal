package fr.insalyon.creatis.vip.core.client.bean;

import java.util.Objects;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Triplet<A, B, C> implements IsSerializable {

    private A first;
    private B second;
    private C third;

    public Triplet() {}

    public Triplet(final A first, final B second, final C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public C getThird() {
        return third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triplet<?,?,?> that = (Triplet<?,?,?>) o;

        return Objects.equals(first, that.first) &&
               Objects.equals(second, that.second) &&
               Objects.equals(third, that.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }
}
