package osd.util.classpath;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Everything {

    private final Supplier<FastClasspathScanner> scannerSupplier;

    @Inject
    Everything(final Supplier<FastClasspathScanner> scannerSupplier) {
        this.scannerSupplier = scannerSupplier;
    }

    public <T> Stream<Class<?>> extending(final Class<T> clazz) {
        return scan((list, scanner) -> scanner.matchSubclassesOf(clazz, list::add));
    }

    public <T> Stream<Class<?>> implementing(final Class<T> clazz) {
        return scan((list, scanner) -> scanner.matchClassesImplementing(clazz, list::add));
    }

    public Stream<Class<?>> annotatedBy(final Class<? extends Annotation> annotation) {
        return scan((list, scanner) -> scanner.matchClassesWithAnnotation(annotation, list::add));
    }

    private Stream<Class<?>> scan(final BiConsumer<List<Class<?>>, FastClasspathScanner> op) {
        final List<Class<?>> result = new ArrayList<>();
        final FastClasspathScanner scanner = scannerSupplier.get();
        op.accept(result, scanner);
        scanner.scan();
        return result.stream();
    }

}
