package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.schedule.Hunk;
import osd.schedule.Lookups;
import osd.util.classpath.Everything;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class BaseConsiderationModuleTest {

    @Mock private Everything mockEverything; // MOCK THE WORLD!!!
    private BaseConsiderationModule instance;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockEverything.extending(BaseConstraint.class)).thenReturn(Stream.of(FakeBaseConstraint.class));
        when(mockEverything.extending(BasePreference.class)).thenReturn(Stream.of(FakeBasePreference.class));
        instance = new BaseConsiderationModule();
    }

    @Test
    void providesBaseConstraints() {
        final Collection<BaseConstraint> result = instance.providesRawBaseConstraints(mockEverything);
        assertTrue(result.size() == 1);
        assertTrue(result.iterator().next() instanceof FakeBaseConstraint);
    }

    @Test
    void providesBasePreferences() {
        final Collection<BasePreference> result = instance.providesRawBasePreferences(mockEverything);
        assertTrue(result.size() == 1);
        assertTrue(result.iterator().next() instanceof FakeBasePreference);
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