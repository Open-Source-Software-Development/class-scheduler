package osd.input.placeholder;

import osd.input.Named;

class NamedPlaceholder extends Placeholder implements Named {

    private String name;

    NamedPlaceholder(final String[] row) {
        super(row);
    }

    @FromCSV(0)
    void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
