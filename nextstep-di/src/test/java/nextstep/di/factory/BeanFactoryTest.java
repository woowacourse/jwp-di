package nextstep.di.factory;

import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.scanner.ApplicationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final String TEST_BASE_PACKAGE = "nextstep.di.factory.example";

    private ApplicationContext ac = new ApplicationContext(TEST_BASE_PACKAGE);

    @Test
    public void di() {
        QnaController qnaController = ac.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }
}
