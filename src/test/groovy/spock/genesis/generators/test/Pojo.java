package spock.genesis.generators.test;

import java.util.Objects;

public final class Pojo {

    private String a;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pojo pojo = (Pojo) o;
        return Objects.equals(getA(), pojo.getA());
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }
}
