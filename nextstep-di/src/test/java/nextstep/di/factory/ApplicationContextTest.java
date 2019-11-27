package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.example.config.IntegrationConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationContextTest {
    private ApplicationContext applicationContext;

    @Test
    void basePackage() {
        applicationContext = new ApplicationContext("nextstep.di.factory.example");
        testQnaController();
    }

    @DisplayName("@ComponentScan이 붙은 클래스를 생성자로 주입")
    @Test
    void componentScan() {
        applicationContext = new ApplicationContext(IntegrationConfig.class);
        testQnaController();
    }

    @DisplayName("@ComponentScan이 붙지 않은 클래스를 생성자로 주입. 해당 클래스의 위치부터 하위 패키지가 scan 대상")
    @Test
    void configuration() {
        applicationContext = new ApplicationContext(ExampleConfig.class);
        testQnaController();
    }

    private void testQnaController() {
        QnaController qnaController = applicationContext.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }
}