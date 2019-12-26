package nextstep.di.factory.error.references;

import nextstep.di.AnnotatedTypeBeanScanner;
import nextstep.di.BeanSpecification;
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
        AnnotatedTypeBeanScanner annotatedTypeBeanScanner = new AnnotatedTypeBeanScanner(new String[]{"nextstep.di.factory.error.references"});
        Set<BeanSpecification> preInstantiateClazz = annotatedTypeBeanScanner.scan();
        beanFactory = new BeanFactory(preInstantiateClazz);
        assertThrows(CircularReferenceException.class, () -> beanFactory.initialize());
    }
}
