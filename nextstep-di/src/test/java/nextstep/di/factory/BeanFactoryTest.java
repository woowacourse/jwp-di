package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.di.factory.example.component.*;
import nextstep.di.factory.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {
    private BeanFactory beanFactory;
    private String basePackages = "nextstep.di.factory.example.component";

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = BeanFactory.getInstance();
    }

    @Test
    public void di() {
        beanFactory.initialize(ClassBeanScanner.scan(basePackages));

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    public void recursiveReferenceException() {
        assertThrows(RecursiveFieldException.class, () -> beanFactory.initialize(
                Maps.asMap(Sets.newHashSet(RecursiveController.class), ClassBeanCreator::new)));
    }

    @Test
    public void noDefaultConstructorException() {
        assertThrows(NoDefaultConstructorException.class, () -> beanFactory.initialize(
                Maps.asMap(Sets.newHashSet(NoDefaultCtorController.class), ClassBeanCreator::new)));
    }

    @Test
    public void implClassNotFoundException() {
        assertThrows(ImplClassNotFoundException.class, () -> beanFactory.initialize(
                Maps.asMap(Sets.newHashSet(NoImplService.class), ClassBeanCreator::new)));
    }

    @Test
    public void interfaceCannotInstantiatedException() {
        assertThrows(InterfaceCannotInstantiatedException.class, () -> beanFactory.initialize(
                Maps.asMap(Sets.newHashSet(NoImplRepository.class), ClassBeanCreator::new)));
    }

    @Test
    public void primitiveTypeInjectionFailException() {
        assertThrows(PrimitiveTypeInjectionFailException.class, () -> beanFactory.initialize(
                Maps.asMap(Sets.newHashSet(PrimitiveTypeInjectController.class), ClassBeanCreator::new)));
    }

    @Test
    public void interfaceExtendsInterfaceSuccess() {
        beanFactory.initialize(
                Maps.asMap(Sets.newHashSet(NoImplService.class, ImplIntermediateRepository.class), ClassBeanCreator::new));
    }

    @Test
    public void getControllersTest() {
        beanFactory.initialize(
                Maps.asMap(Sets.newHashSet(AnnotatedController.class, AnnotatedService.class, AnnotatedRepository.class), ClassBeanCreator::new));
        assertThat(beanFactory.getControllers().size()).isEqualTo(1);
    }
}