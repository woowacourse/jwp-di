package nextstep.di.factory;

import nextstep.di.factory.example.MyConfig;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextTest {
    private ApplicationContext applicationContext;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        applicationContext = new ApplicationContext(MyConfig.class);
    }

    @Test
    public void di() {
        QnaController qnaController = applicationContext.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    @DisplayName("해당 패키지에 있는 클래스를 찾는다.")
    void getControllers() {
        Map<Class<?>, Object> controllers = applicationContext.getControllers();
        assertThat(controllers.containsKey(QnaController.class)).isTrue();
        assertThat(controllers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("해당 패키지에 없는 클래스는 찾지 못한다.")
    void getControllersFail() {
        Map<Class<?>, Object> controllers = applicationContext.getControllers();
        assertThat(controllers.containsKey(ClasspathBeanScanner.class)).isFalse();
    }

    @Test
    @DisplayName("해당 패키지에 있는 클래스의 빈을 찾는다.")
    void getBean() {
        assertNotNull(applicationContext.getBean(MyQnaService.class));
    }

    @Test
    @DisplayName("다른 패키지에 있는 클래스의 빈을 찾는 경우 예외가 발생한다.")
    void getBeanFail() {
        assertThrows(RuntimeException.class, () -> applicationContext.getBean(ClasspathBeanScanner.class));
    }
}
