package osd.util.classpath;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.ClassAnnotationMatchProcessor;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.ImplementingClassMatchProcessor;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.SubclassMatchProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EverythingTest {

    /* HOW THIS TEST WORKS:

    We create a mock scanner that pretends every possible scan matches only
    itself (not particularly realistic, I know), construct an Everything
    instance that uses it, and check that we always get the original class,
    and nothing else, back. If so, we're deferring correctly to the correct
    scanner.
     */

    @Mock private FastClasspathScanner mockScanner;
    private Everything instance;

    @BeforeEach
    <T> void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockScanner.matchClassesImplementing(any(), any())).then(invocation -> {
            final ImplementingClassMatchProcessor<T> interfaceMatchProcessor = invocation.getArgument(1);
            interfaceMatchProcessor.processMatch(invocation.getArgument(0));
            return mockScanner;
        });
        when(mockScanner.matchSubclassesOf(any(), any())).then(invocation -> {
            SubclassMatchProcessor<T> subclassMatchProcessor = invocation.getArgument(1);
            subclassMatchProcessor.processMatch(invocation.getArgument(0));
            return mockScanner;
        });
        when(mockScanner.matchClassesWithAnnotation(any(), any())).then(invocation -> {
            ClassAnnotationMatchProcessor classAnnotationMatchProcessor = invocation.getArgument(1);
            classAnnotationMatchProcessor.processMatch(invocation.getArgument(0));
            return mockScanner;
        });

        instance = new Everything(() -> mockScanner);
    }

    @Test
    void extending_Class() {
        doTest(TestClass.class, instance.extending(TestClass.class));
        verify(mockScanner).matchSubclassesOf(eq(TestClass.class), any());
    }

    @Test
    void extending_Interface() {
        doTest(TestInterface.class, instance.extending(TestInterface.class));
        verify(mockScanner).matchClassesImplementing(eq(TestInterface.class), any());
    }

    @Test
    void annotatedBy() {
        doTest(TestAnnotation.class, instance.annotatedBy(TestAnnotation.class));
    }

    private void doTest(final Class<?> clazz, Stream<Class<?>> resultStream) {
        final Set<Class<?>> expected = Collections.singleton(clazz);
        final Set<Class<?>> result = resultStream.collect(Collectors.toSet());
        assertEquals(expected, result);
        verify(mockScanner).scan();
    }

    private class TestClass {}

    private interface TestInterface {}

    private @interface TestAnnotation {}

}