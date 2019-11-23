package nextstep.di.factory.domain.scanner;

import nextstep.di.domain.factory.BeanFactory;
import nextstep.di.domain.factory.GenericBeanFactory;
import nextstep.di.domain.scanner.ClassPathScanner;
import nextstep.di.factory.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClassPathScannerTest {
    private ClassPathScanner classPathScanner;
    private BeanFactory beanFactory;

    @BeforeEach
    public void setUp() {
        beanFactory = new GenericBeanFactory();
        classPathScanner = new ClassPathScanner("nextstep.di.factory.example");
    }

    @Test
    public void classPathScan() {
        classPathScanner.scan(beanFactory);

        QnaController qnaController = beanFactory.getBean(QnaController.class);
        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService);
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    public void singleInstanceTest() {
        classPathScanner.scan(beanFactory);

        QnaController qnaController = beanFactory.getBean(QnaController.class);
        assertThat(qnaController).isEqualTo(beanFactory.getBean(QnaController.class));

        MyQnaService qnaService = qnaController.getQnaService();
        assertThat(qnaService).isEqualTo(beanFactory.getBean(MyQnaService.class));

        UserRepository userRepository = qnaService.getUserRepository();
        QuestionRepository questionRepository = qnaService.getQuestionRepository();
        assertThat(userRepository).isEqualTo(beanFactory.getBean(JdbcUserRepository.class));
        assertThat(questionRepository).isEqualTo(beanFactory.getBean(JdbcQuestionRepository.class));
    }
}