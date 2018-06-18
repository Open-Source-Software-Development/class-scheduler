package osd.util.classpath;

import dagger.Module;
import dagger.Provides;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.util.function.Supplier;

@Module
public class ClasspathModule {

    @Provides
    static Supplier<FastClasspathScanner> providesScannerSupplier() {
        return FastClasspathScanner::new;
    }

}
