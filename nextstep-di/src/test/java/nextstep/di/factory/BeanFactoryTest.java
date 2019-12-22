package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.example.QuestionRepository;
import nextstep.di.scanner.BeanScanner;
import nextstep.di.scanner.ClasspathBeanScanner;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BeanFactoryTest {

    private static final BeanFactory BEAN_FACTORY;

    static {
        final BeanScanner beanScanner = new ClasspathBeanScanner("nextstep.di.factory.example");
        BEAN_FACTORY = new BeanFactory(beanScanner.scan());
        BEAN_FACTORY.initialize();
    }

    @Test
    void di() {
        QnaController qnaController = BEAN_FACTORY.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void check_single_repository_instance() {
        final MyQnaService service = BEAN_FACTORY.getBean(MyQnaService.class);
        final QuestionRepository actual = service.getQuestionRepository();
        final QuestionRepository expected = BEAN_FACTORY.getBean(JdbcQuestionRepository.class);
        assertThat(actual).isEqualTo(expected);
    }

}
