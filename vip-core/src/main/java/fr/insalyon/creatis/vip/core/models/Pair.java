package fr.insalyon.creatis.vip.core.models;

import java.util.Objects;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Pair<A,B> implements IsSerializable {

    private A first;
    private B second;

    public Pair() {}

    public Pair(final A first, final B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?,?> that = (Pair<?,?>) o;

        return Objects.equals(first, that.first) &&
               Objects.equals(second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
