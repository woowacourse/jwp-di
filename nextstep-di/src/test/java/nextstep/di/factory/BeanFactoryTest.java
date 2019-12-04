package nextstep.di.factory;

import java.util.Set;

import com.google.common.collect.Sets;
import nextstep.di.factory.circularreference.CircularReferenceController;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.NoDefaultConstructorController;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.notbean.InjectNotBean;
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
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        classpathBeanScanner.scanBeans(BASE_PACKAGE);
        beanFactory.initialize();

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void getControllers() {
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        classpathBeanScanner.scanBeans(BASE_PACKAGE);
        beanFactory.initialize();

        assertThat(beanFactory.getControllers().size()).isEqualTo(1);
    }

    @Test
    void canNotFoundDefaultConstructor() {
        beanFactory.appendPreInstantiatedBeans(Sets.newHashSet(NoDefaultConstructorController.class));

        assertThrows(DefaultConstructorFindFailException.class,
                () -> beanFactory.initialize());
    }

    @Test
    void confirmSingleton() {
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        classpathBeanScanner.scanBeans(BASE_PACKAGE);
        beanFactory.initialize();

        Object originController = beanFactory.getBean(QnaController.class);

        beanFactory.initialize();
        Object expectedController = beanFactory.getBean(QnaController.class);

        assertSame(originController, expectedController);
    }

    @Test
    void throwExceptionWhenCircularReference() {
        beanFactory.appendPreInstantiatedBeans(Sets.newHashSet(CircularReferenceController.class));

        assertThrows(CircularReferenceException.class,
                () -> beanFactory.initialize());
    }

    @Test
    void throwExceptionWhenNotBean() {
        beanFactory.appendPreInstantiatedBeans(Sets.newHashSet(InjectNotBean.class));

        assertThrows(BeanCreateFailException.class,
                () -> beanFactory.initialize());
    }
}
