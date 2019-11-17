package nextstep.di.factory;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.NoDefaultConstructorController;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.example.RecursiveController;
import nextstep.exception.CircularReferenceException;
import nextstep.exception.DefaultConstructorFindFailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {
    final String basePackage = "nextstep.di.factory.example";
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = new BeanFactory();
    }

    @Test
    public void di() {
        beanFactory.initialize(new BeanScanner(basePackage).scanBeans());

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void getControllers() {
        beanFactory.initialize(new BeanScanner(basePackage).scanBeans());
        assertThat(beanFactory.getControllers().size()).isEqualTo(1);
    }

    @Test
    void canNotFoundDefaultConstructor() {
        assertThrows(DefaultConstructorFindFailException.class,
                () -> beanFactory.initialize(Sets.newHashSet(NoDefaultConstructorController.class)));
    }

    @Test
    void confirmSingleton() {
        Set<Class<?>> preInstantiatedBeans = new BeanScanner(basePackage).scanBeans();

        beanFactory.initialize(preInstantiatedBeans);
        Map<Class<?>, Object> originController = beanFactory.getControllers();

        beanFactory.initialize(preInstantiatedBeans);
        assertThat(originController).isEqualTo(beanFactory.getControllers());
    }

    @Test
    void throwExceptionWhenCircularReference() {
        assertThrows(CircularReferenceException.class,
                () -> beanFactory.initialize(Sets.newHashSet(RecursiveController.class)));
    }
}
