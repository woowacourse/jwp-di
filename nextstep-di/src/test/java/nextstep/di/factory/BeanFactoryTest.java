package nextstep.di.factory;

import nextstep.di.factory.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BeanFactoryTest {
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = new BeanFactory();

        Map<Class<?>, BeanDefinition> configurationBeanDefinitions = new ConfigurationBeanScanner().doScan(IntegrationConfig.class);
        Map<Class<?>, BeanDefinition> classpathBeanDefinitions = new ClasspathBeanScanner().doScan("nextstep.di.factory.example");

        configurationBeanDefinitions.putAll(classpathBeanDefinitions);

        beanFactory.init(configurationBeanDefinitions);
        beanFactory.initialize();
    }

    @Test
    public void di() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void 생성자_매개변수가_인터페이스인_경우_싱글_인스턴스_확인() {
        MyQnaService myQnaService = beanFactory.getBean(MyQnaService.class);

        UserRepository actual = myQnaService.getUserRepository();
        UserRepository expected = beanFactory.getBean(JdbcUserRepository.class);

        assertEquals(expected, actual);
    }

    @Test
    void 생성자_매개변수가_클래스인_경우_싱글_인스턴스_확인() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        MyQnaService actual = qnaController.getQnaService();
        MyQnaService expected = beanFactory.getBean(MyQnaService.class);

        assertEquals(expected, actual);
    }

    @Test
    void bean_에서_ControllerAnnotation_이_붙은_클래스_찾기() {
        Set<Class<?>> controller = beanFactory.getController();

        assertTrue(controller.contains(QnaController.class));
    }
}
