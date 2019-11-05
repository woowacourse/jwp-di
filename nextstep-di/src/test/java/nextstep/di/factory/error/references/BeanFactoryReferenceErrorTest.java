package nextstep.di.factory.error.references;

import nextstep.di.BeanScanner;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.exception.CircularReferenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryReferenceErrorTest {

    private BeanFactory beanFactory;

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("순환 참조 예외 발생")
    void initialize() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.error.references");
        Set<Class<?>> preInstantiateClazz = beanScanner.scan();
        beanFactory = new BeanFactory(preInstantiateClazz);
        assertThrows(CircularReferenceException.class, () -> beanFactory.initialize());
    }
}
