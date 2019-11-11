package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.scanner.BeanScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private Reflections reflections;
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");
        beanFactory = new BeanFactory(beanScanner);
        beanFactory.initialize();
    }

    @Test
    public void di() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService);
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    public void diRepository() {
        JdbcUserRepository repository = beanFactory.getBean(JdbcUserRepository.class);
        assertNotNull(repository);
    }

    @Test
    public void singleInstanceTest() {
        MyQnaService qnaService = beanFactory.getBean(QnaController.class).getQnaService();
        assertSame(qnaService, beanFactory.getBean(MyQnaService.class));
        assertSame(qnaService.getQuestionRepository(), beanFactory.getBean(JdbcQuestionRepository.class));
        assertSame(qnaService.getUserRepository(), beanFactory.getBean(JdbcUserRepository.class));
    }

    @Test
    public void getControllersTest() {
        Set<Class<?>> actual = beanFactory.getControllers();
        assertThat(actual).contains(QnaController.class);
    }
}
