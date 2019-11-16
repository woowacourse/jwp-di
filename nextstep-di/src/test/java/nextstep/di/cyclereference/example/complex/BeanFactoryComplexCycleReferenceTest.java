package nextstep.di.cyclereference.example.complex;

import nextstep.di.factory.factory.BeanFactory;
import nextstep.di.factory.factory.BeanScanner;
import nextstep.di.factory.exception.CycleReferenceException;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryComplexCycleReferenceTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryComplexCycleReferenceTest.class);

    //    private Reflections reflections;
    private BeanScanner beanScanner;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanScanner = new BeanScanner("nextstep.di.cyclereference.example.complex");
        Set<Class<?>> preInstantiatedClazz = beanScanner.getTypesAnnotatedWith(Service.class, Controller.class, Repository.class);

        beanFactory = new BeanFactory(preInstantiatedClazz);
    }

    @Test
    public void complexCylceReference() throws Exception {
        assertThrows(CycleReferenceException.class, () -> beanFactory.initialize());
    }
}
