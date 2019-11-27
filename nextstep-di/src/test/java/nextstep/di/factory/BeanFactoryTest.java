package nextstep.di.factory;

import nextstep.di.factory.example.*;
import nextstep.di.scanner.BeanScanner;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final String TEST_BASE_PACKAGE = "nextstep.di.factory.example";
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = new BeanFactory();
        BeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(IntegrationConfig.class);

        BeanScanner cbds = new ClasspathBeanScanner(beanFactory);
        cbds.doScan(TEST_BASE_PACKAGE);
        beanFactory.initialize();
    }

    @Test
    public void di() throws Exception {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void question_repository_single_instance_test() {
        MyQnaService myQnaService = beanFactory.getBean(MyQnaService.class);
        QuestionRepository actual = myQnaService.getQuestionRepository();
        QuestionRepository expected = beanFactory.getBean(JdbcQuestionRepository.class);
        assertEquals(expected, actual);
    }

    @Test
    void my_qna_service_single_instance_test() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);
        MyQnaService actual = qnaController.getQnaService();
        MyQnaService expected = beanFactory.getBean(MyQnaService.class);
        assertEquals(expected, actual);
    }
}
