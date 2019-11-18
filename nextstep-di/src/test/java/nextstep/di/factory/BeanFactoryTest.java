package nextstep.di.factory;

import java.util.Set;

import com.google.common.collect.Sets;
import nextstep.di.factory.example.*;
import nextstep.exception.BeanCreateFailException;
import nextstep.exception.CircularReferenceException;
import nextstep.exception.DefaultConstructorFindFailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BeanFactoryTest {
    private static final String BASE_PACKAGE = "nextstep.di.factory.example";
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = new BeanFactory();
    }

    @Test
    public void di() {
        beanFactory.initialize(new BeanScanner(BASE_PACKAGE).scanBeans());

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void getControllers() {
        beanFactory.initialize(new BeanScanner(BASE_PACKAGE).scanBeans());
        assertThat(beanFactory.getControllers().size()).isEqualTo(1);
    }

    @Test
    void canNotFoundDefaultConstructor() {
        assertThrows(DefaultConstructorFindFailException.class,
                () -> beanFactory.initialize(Sets.newHashSet(NoDefaultConstructorController.class)));
    }

    @Test
    void confirmSingleton() {
        Set<Class<?>> preInstantiatedBeans = new BeanScanner(BASE_PACKAGE).scanBeans();

        beanFactory.initialize(preInstantiatedBeans);
        Object originController = beanFactory.getBean(QnaController.class);

        beanFactory.initialize(preInstantiatedBeans);
        Object expectedController = beanFactory.getBean(QnaController.class);

        assertSame(originController, expectedController);
    }

    @Test
    void throwExceptionWhenCircularReference() {
        assertThrows(CircularReferenceException.class,
                () -> beanFactory.initialize(Sets.newHashSet(RecursiveController.class)));
    }

    @Test
    void throwExceptionWhenNotBean() {
        assertThrows(BeanCreateFailException.class,
                () -> beanFactory.initialize(Sets.newHashSet(InjectNotBean.class)));
    }
}
