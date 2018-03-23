package osd.input.placeholder;

import osd.considerations.Consideration;

import java.util.function.Supplier;

abstract class UserConsiderationPlaceholder<T extends Consideration> extends Placeholder implements Supplier<T> {

    private final PlaceholderConfig config;
    Object left, right;

    UserConsiderationPlaceholder(final String[] row, final PlaceholderConfig config) {
        this.config = config;
        parse(row);
    }

    @FromCSV(0)
    void setLeft(final String name) {
        left = config.lookup(name);
    }

    @FromCSV(1)
    void setRight(final String name) {
        right = config.lookup(name);
    }

}
