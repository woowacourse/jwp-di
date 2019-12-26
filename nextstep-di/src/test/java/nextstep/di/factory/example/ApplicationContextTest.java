package nextstep.di.factory.example;

import nextstep.di.context.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApplicationContextTest {

    private ApplicationContext applicationContext;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        this.applicationContext = new ApplicationContext("nextstep.di.factory.example");
    }

    @Test
    public void getBean() {
        QnaController qnaController = applicationContext.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    @DisplayName("하나의 Bean에 대해 하나의 인스턴스만 생성되는지 테스트")
    void singleton() {
        QnaController qnaController = applicationContext.getBean(QnaController.class);
        TestQnaController testQnaController = applicationContext.getBean(TestQnaController.class);

        assertEquals(qnaController.getQnaService(), testQnaController.getQnaService());
    }
}
