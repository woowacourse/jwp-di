package nextstep.di.factory;

import nextstep.di.context.ApplicationBeanContext;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.scanner.AnnotatedBeanScanner;
import nextstep.di.scanner.BeanScanner;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnnotatedBeanFactoryTest {

    private AnnotatedBeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        BeanScanner beanScanner = new AnnotatedBeanScanner(new ApplicationBeanContext("nextstep.di.factory.example"),
                Controller.class, Service.class, Repository.class);
        beanFactory = new AnnotatedBeanFactory(new BeanRegistry(), beanScanner);

        beanScanner.doScan();
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
