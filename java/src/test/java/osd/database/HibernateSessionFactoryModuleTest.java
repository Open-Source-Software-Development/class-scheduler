package osd.database;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.util.classpath.Everything;

import javax.persistence.Entity;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HibernateSessionFactoryModuleTest {

    @Mock private Everything mockEverything;
    @Mock private Stream<Class<?>> mockStream;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockEverything.annotatedBy(Entity.class)).thenReturn(mockStream);
    }

    @Test
    void providesSessionFactory() {
        final SessionFactory sessionFactory = HibernateSessionFactoryModule.providesSessionFactory(mockEverything);
        assertNotNull(sessionFactory);
        verify(mockEverything).annotatedBy(Entity.class);
        verify(mockStream).forEach(any());
    }

}