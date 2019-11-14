package nextstep.di.factory.domain;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class BeanFactory1Test {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory1Test.class);

    private BeanFactory1 beanFactory1;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");
        beanFactory1 = new BeanFactory1(beanScanner);
        beanFactory1.initialize();
    }

    @Test
    public void di() {
        QnaController qnaController = beanFactory1.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService);
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    public void diRepository() {
        JdbcUserRepository repository = beanFactory1.getBean(JdbcUserRepository.class);
        assertNotNull(repository);
    }

    @Test
    public void singleInstanceTest() {
        MyQnaService qnaService = beanFactory1.getBean(QnaController.class).getQnaService();
        assertSame(qnaService, beanFactory1.getBean(MyQnaService.class));
        assertSame(qnaService.getQuestionRepository(), beanFactory1.getBean(JdbcQuestionRepository.class));
        assertSame(qnaService.getUserRepository(), beanFactory1.getBean(JdbcUserRepository.class));
    }

    @Test
    public void getControllersTest() {
        Set<Class<?>> actual = beanFactory1.getSupportedClassByAnnotation(Controller.class);
        assertThat(actual).contains(QnaController.class);
    }
}
