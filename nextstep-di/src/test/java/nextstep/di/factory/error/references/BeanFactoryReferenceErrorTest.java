package nextstep.di.factory.error.references;

import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.exception.CircularReferenceException;
import nextstep.di.scanner.ClassPathBeanScanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanFactoryReferenceErrorTest {

    private BeanFactory beanFactory;

    @Test
    @DisplayName("순환 참조 예외 발생")
    void initialize() {
        beanFactory = new BeanFactory();

        ClassPathBeanScanner classPathBeanScanner = new ClassPathBeanScanner();
        classPathBeanScanner.scan("nextstep.di.factory.error.references");
        classPathBeanScanner.registerBeans(beanFactory);

        assertThrows(CircularReferenceException.class, () -> beanFactory.initialize());
    }
}
