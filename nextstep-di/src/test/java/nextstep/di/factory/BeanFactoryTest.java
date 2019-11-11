package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.example.*;
import nextstep.di.factory.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;
    private String basePackages = "nextstep.di.factory.example";

    @Test
    public void di() throws Exception {
        beanFactory = new BeanFactory(BeanScanner.scan(basePackages));
        beanFactory.initialize();

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    public void recursiveReferenceException() {
        beanFactory = new BeanFactory(Sets.newHashSet(RecursiveController.class));
        assertThrows(RecursiveFieldException.class, () -> beanFactory.initialize());
    }

    @Test
    public void noDefaultConstructorException() {
        beanFactory = new BeanFactory(Sets.newHashSet(NoDefaultCtorController.class));
        assertThrows(NoDefaultConstructorException.class, () -> beanFactory.initialize());
    }

    @Test
    public void implClassNotFoundException() {
        beanFactory = new BeanFactory(Sets.newHashSet(NoImplService.class));
        assertThrows(ImplClassNotFoundException.class, () -> beanFactory.initialize());
    }

    @Test
    public void interfaceCannotInstantiatedException() {
        beanFactory = new BeanFactory(Sets.newHashSet(NoImplRepository.class));
        assertThrows(InterfaceCannotInstantiatedException.class, () -> beanFactory.initialize());
    }

    @Test
    public void primitiveTypeInjectionFailException() {
        beanFactory = new BeanFactory(Sets.newHashSet(PrimitiveTypeInjectController.class));
        assertThrows(PrimitiveTypeInjectionFailException.class, () -> beanFactory.initialize());
    }

    @Test
    public void interfaceExtendsInterfaceSuccess() {
        beanFactory = new BeanFactory(Sets.newHashSet(NoImplService.class, ImplIntermediateRepository.class));
        assertDoesNotThrow(() -> beanFactory.initialize());
    }

    @Test
    public void getControllersTest() {
        beanFactory = new BeanFactory(Sets.newHashSet(AnnotatedController.class, AnnotatedService.class, AnnotatedRepository.class));
        beanFactory.initialize();
        assertThat(beanFactory.getControllers().size()).isEqualTo(1);
    }
}
