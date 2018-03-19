package osd.considerations;

import dagger.Module;
import dagger.Provides;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import osd.database.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@Module
public class BaseConsiderationModule {

    private final Supplier<FastClasspathScanner> scannerFactory;

    public BaseConsiderationModule(final Supplier<FastClasspathScanner> scannerFactory) {
        this.scannerFactory = scannerFactory;
    }

    @Provides
    Collection<BaseConstraint> providesBaseConstraints() {
        return scan(BaseConstraint.class);
    }

    @Provides
    Collection<BasePreference> providesBasePreferences() {
        return scan(BasePreference.class);
    }

    private <T> Collection<T> scan(final Class<T> clazz) {
        final List<T> result = new ArrayList<>();
        scannerFactory.get()
                .matchClassesImplementing(clazz, c -> result.add(init(c)))
                .scan();
        return result;
    }

    private static <T> T init(final Class<T> clazz) {
        try {
            final Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (final ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

}
