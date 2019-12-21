package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.example.*;
import nextstep.di.factory.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;
    private String basePackages = "nextstep.di.factory.example";

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = BeanFactory.getInstance();
    }

    @Test
    public void di() throws Exception {
        beanFactory.initialize(BeanScanner.scan(basePackages));

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    public void recursiveReferenceException() {
        assertThrows(RecursiveFieldException.class, () -> beanFactory.initialize(Sets.newHashSet(RecursiveController.class)));
    }

    @Test
    public void noDefaultConstructorException() {
        assertThrows(NoDefaultConstructorException.class, () -> beanFactory.initialize(Sets.newHashSet(NoDefaultCtorController.class)));
    }

    @Test
    public void implClassNotFoundException() {
        assertThrows(ImplClassNotFoundException.class, () -> beanFactory.initialize(Sets.newHashSet(NoImplService.class)));
    }

    @Test
    public void interfaceCannotInstantiatedException() {
        assertThrows(InterfaceCannotInstantiatedException.class, () -> beanFactory.initialize(Sets.newHashSet(NoImplRepository.class)));
    }

    @Test
    public void primitiveTypeInjectionFailException() {
        assertThrows(PrimitiveTypeInjectionFailException.class, () -> beanFactory.initialize(Sets.newHashSet(PrimitiveTypeInjectController.class)));
    }

    @Test
    public void interfaceExtendsInterfaceSuccess() {
        beanFactory.initialize(Sets.newHashSet(NoImplService.class, ImplIntermediateRepository.class));
    }

    @Test
    public void getControllersTest() {
        beanFactory.initialize(Sets.newHashSet(AnnotatedController.class, AnnotatedService.class, AnnotatedRepository.class));
        assertThat(beanFactory.getControllers().size()).isEqualTo(1);
    }

    @Test
    void QuestionRepository가_싱글_인스턴스가_맞는지_테스트() {
        beanFactory.initialize(BeanScanner.scan(basePackages));
        final MyQnaService myQnaService = beanFactory.getBean(MyQnaService.class);

        final QuestionRepository actual = myQnaService.getQuestionRepository();
        final QuestionRepository expected = beanFactory.getBean(JdbcQuestionRepository.class);

        assertThat(actual).isEqualTo(expected);
    }
}
