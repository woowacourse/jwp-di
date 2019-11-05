package nextstep.di.factory.error.interfaces;

import nextstep.di.BeanScanner;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.exception.IllegalAnnotationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryInterfaceErrorTest {

    private BeanFactory beanFactory;

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("인터페이스에 어노테이션을 달았을 때 예외 발생")
    void initialize() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.error.interfaces");
        Set<Class<?>> preInstantiateClazz = beanScanner.scan();
        beanFactory = new BeanFactory(preInstantiateClazz);
        assertThrows(IllegalAnnotationException.class, () -> beanFactory.initialize());
    }
}
