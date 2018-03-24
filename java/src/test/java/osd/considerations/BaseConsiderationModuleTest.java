package osd.considerations;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.ImplementingClassMatchProcessor;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.schedule.Hunk;
import osd.schedule.Lookups;

import java.util.Collection;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseConsiderationModuleTest<T> {

    @Mock private FastClasspathScanner mockScanner;
    private Class<? extends T> scanResult;
    private ImplementingClassMatchProcessor<T> callback;
    private BaseConsiderationModule instance;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockScanner.matchClassesImplementing(any(), any())).thenAnswer(a -> {
            setScanResult(a.getArgument(0));
            callback = a.getArgument(1);
            return mockScanner;
        });
        when(mockScanner.scan()).then(a -> {
            callback.processMatch(scanResult);
            return mock(ScanResult.class);
        });
        instance = new BaseConsiderationModule(() -> mockScanner);
    }

    @Test
    void providesBaseConstraints() {
        final Collection<BaseConstraint> result = instance.providesRawBaseConstraints();
        assertTrue(result.size() == 1);
        assertTrue(result.iterator().next() instanceof FakeBaseConstraint);
    }

    @Test
    void providesBasePreferences() {
        final Collection<BasePreference> result = instance.providesRawBasePreferences();
        assertTrue(result.size() == 1);
        assertTrue(result.iterator().next() instanceof FakeBasePreference);
    }

    @SuppressWarnings("unchecked")
    private void setScanResult(final Class<? extends T> clazz) {
        if (clazz == BaseConstraint.class) {
            scanResult = (Class<? extends T>)FakeBaseConstraint.class;
            return;
        }
        if (clazz == BasePreference.class) {
            scanResult = (Class<? extends T>)FakeBasePreference.class;
            return;
        }
        throw new IllegalArgumentException(clazz.getCanonicalName());
    }

    private static class FakeBaseConstraint implements BaseConstraint {
        @Override
        public Predicate<Hunk> bindPredicate(final Lookups lookups) {
            return h -> true;
        }
    }

    private static class FakeBasePreference implements BasePreference {

        @Override
        public int worth() {
            return 0;
        }

        @Override
        public Predicate<Hunk> bindPredicate(Lookups lookups) {
            return h -> true;
        }

    }

}