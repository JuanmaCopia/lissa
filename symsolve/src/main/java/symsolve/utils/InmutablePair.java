package symsolve.utils;

public class InmutablePair<F, S> {

    private final F first;
    private final S second;


    public InmutablePair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (first != null ? first.hashCode() : 0);
        hash = 31 * hash + (second != null ? second.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof InmutablePair) {
            InmutablePair pair = (InmutablePair) o;
            if (first != null) {
                if (!first.equals(pair.getFirst()))
                    return false;
            } else if (pair.getFirst() != null)
                return false;

            if (second != null)
                return second.equals(pair.getSecond());
            else
                return pair.getFirst() == null;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s,%s", first.toString(), second.toString());
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

}
