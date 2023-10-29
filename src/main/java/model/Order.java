package model;

import java.util.Comparator;

public enum Order {
    ASC(Comparator.naturalOrder()),
    DESC(Comparator.reverseOrder());

    private final Comparator<? extends Comparable<?>> comparator;

    Order(Comparator<? extends Comparable<?>> comparator) {
        this.comparator = comparator;
    }

    public <T> Comparator<T> getComparator() {
        return (Comparator<T>) comparator;
    }
}
