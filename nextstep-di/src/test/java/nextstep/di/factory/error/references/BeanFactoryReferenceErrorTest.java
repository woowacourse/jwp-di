package nextstep.di.factory.error.references;

import nextstep.di.BeanScanner;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.exception.CircularReferenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanFactoryReferenceErrorTest {

    private BeanFactory beanFactory;

    @Test
    @DisplayName("순환 참조 예외 발생")
    void initialize() {
        beanFactory = new BeanFactory();

        BeanScanner beanScanner = new BeanScanner(beanFactory);
        beanScanner.scan("nextstep.di.factory.error.references");

        assertThrows(CircularReferenceException.class, () -> beanFactory.initialize());
    }
}
