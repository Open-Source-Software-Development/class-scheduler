package osd.input.placeholder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;

class Placeholder {

    private String[] row;

    Placeholder() {}

    Placeholder(final String[] row) {
        parse(row);
    }

    void parse(final String[] row) {
        this.row = row;
        for (Class<?> clazz = getClass(); Placeholder.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
            try {
                for (final Method method : clazz.getDeclaredMethods()) {
                    final FromCSV data = method.getAnnotation(FromCSV.class);
                    if (data != null) {
                        final String arg = row[data.value()];
                        method.invoke(this, arg);
                    }
                }
            } catch (final ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Placeholder that = (Placeholder) o;
        return Arrays.equals(row, that.row);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(row);
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FromCSV {
        int value();
    }

}
