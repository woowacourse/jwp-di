package nextstep.di.factory.domain;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanScannerTest {
    private BeanScanner beanScanner;

    @BeforeEach
    public void setUp() {
        beanScanner = new BeanScanner("nextstep.di.factory.example");
    }

    @Test
    public void scanAnnotationTest() {
        Set<Class<?>> classes = beanScanner.scan();
        assertThat(classes)
                .contains(QnaController.class)
                .contains(MyQnaService.class)
                .contains(JdbcUserRepository.class)
                .contains(JdbcQuestionRepository.class);
    }

    @Test
    public void scanBeanFactory() {
        final BeanFactory2 beanFactory = new BeanFactory2();
        beanScanner.initialize();
        beanScanner.scanBeanFactory(beanFactory);

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService);
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }
}