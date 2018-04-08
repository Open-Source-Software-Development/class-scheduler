package osd.util.classpath;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClasspathModuleTest {

    @Test
    void providesScannerSupplier() {
        assertEquals(FastClasspathScanner.class, ClasspathModule.providesScannerSupplier().get().getClass());
    }

}