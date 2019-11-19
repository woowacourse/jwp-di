package nextstep.di.factory;

import nextstep.di.context.ApplicationBeanContext;
import nextstep.di.example.IntegrationConfig;
import nextstep.di.example.MyQnaService;
import nextstep.di.example.QnaController;
import nextstep.di.registry.BeanRegistry;
import nextstep.di.scanner.AnnotatedBeanScanner;
import nextstep.di.scanner.ConfigurationScanner;
import nextstep.stereotype.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SingleBeanFactoryTest {

    private SingleBeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = new SingleBeanFactory(new BeanRegistry(),
                new ConfigurationScanner(new ApplicationBeanContext(IntegrationConfig.class)),
                new AnnotatedBeanScanner(new ApplicationBeanContext("nextstep.di.example")));

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
    void getAnnotatedBeans() {
        Set<Class<?>> annotatedBeans = beanFactory.getTypes(Controller.class);

        assertNotNull(annotatedBeans.contains(QnaController.class));
    }
}
