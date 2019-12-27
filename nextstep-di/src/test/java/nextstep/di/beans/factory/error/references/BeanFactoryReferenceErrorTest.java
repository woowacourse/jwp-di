package nextstep.di.beans.factory.error.references;

import nextstep.di.beans.factory.BeanFactory;
import nextstep.di.beans.factory.exception.CircularReferenceException;
import nextstep.di.beans.scanner.AnnotatedTypeBeanScanner;
import nextstep.di.beans.specification.BeanSpecification;
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
        AnnotatedTypeBeanScanner annotatedTypeBeanScanner = new AnnotatedTypeBeanScanner(new String[]{"nextstep.di.beans.factory.error.references"});
        Set<BeanSpecification> preInstantiateClazz = annotatedTypeBeanScanner.scan();
        beanFactory = new BeanFactory(preInstantiateClazz);
        assertThrows(CircularReferenceException.class, () -> beanFactory.initialize());
    }
}
