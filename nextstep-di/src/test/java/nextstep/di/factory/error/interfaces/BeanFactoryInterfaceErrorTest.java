package nextstep.di.factory.error.interfaces;

import nextstep.di.AnnotatedTypeBeanScanner;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.exception.NoSuchDefaultConstructorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryInterfaceErrorTest {

    private BeanFactory beanFactory;

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("인터페이스에 어노테이션을 달았을 때 예외 발생")
    void initialize() {
        AnnotatedTypeBeanScanner annotatedTypeBeanScanner = new AnnotatedTypeBeanScanner(new String[]{"nextstep.di.factory.error.interfaces"});
        assertThrows(NoSuchDefaultConstructorException.class, annotatedTypeBeanScanner::scan);
    }
}
