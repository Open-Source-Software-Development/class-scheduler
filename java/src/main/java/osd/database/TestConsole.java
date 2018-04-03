package osd.database;

import java.lang.reflect.Field;
import java.util.Scanner;

class TestConsole {

    private static final From from = new From(DatabaseModule.providesSessionFactory());

    public static void main(final String[] args) {
        final Class<?> testClass = chooseTestClass();
        from.from(testClass).forEach(TestConsole::log);
        System.exit(0);
    }

    private static Class<?> chooseTestClass() {
        System.out.print("Type to test: ");
        String canonicalTypeName = null;
        try (final Scanner s = new Scanner(System.in)) {
            final String interfaceTypeName = s.nextLine().trim();
            canonicalTypeName = "osd.database." + interfaceTypeName;
            return Class.forName(canonicalTypeName);
        } catch (final ClassNotFoundException e) {
            System.out.println("Couldn't find a class " + canonicalTypeName);
            System.out.println("Things to check:");
            System.out.println("  Capitalization (Professor vs. professor)");
            System.out.println("  Don't include 'record' at the end");
            System.exit(0);
            return null;
        }
    }

    private static void log(final Object o) {
        System.out.println("Loaded record " + o);
        try {
            System.out.println(log0(o));
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static String log0(final Object o) throws IllegalAccessException {
        final StringBuilder result = new StringBuilder();
        for (final Field field: o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            result.append("  ").append(field.getName())
                    .append("(").append(field.getType().getSimpleName()).append(")")
                    .append(": ")
                    .append(field.get(o))
                    .append(System.lineSeparator());
        }
        return result.toString();
    }

}
