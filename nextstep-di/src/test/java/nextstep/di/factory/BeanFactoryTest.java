package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.example.QuestionRepository;
import nextstep.di.scanner.ClasspathBeanScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger( BeanFactoryTest.class );

    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = new BeanFactory();
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        classpathBeanScanner.doScan("nextstep.di.factory.example");
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
