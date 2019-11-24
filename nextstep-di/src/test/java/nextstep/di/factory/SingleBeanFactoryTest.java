package nextstep.di.factory;

import nextstep.di.example.ExampleConfig;
import nextstep.di.example.MyQnaService;
import nextstep.di.example.QnaController;
import nextstep.di.scanner.AnnotatedBeanScanner;
import nextstep.di.scanner.ConfigurationScanner;
import nextstep.stereotype.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SingleBeanFactoryTest {
    private static final Logger logger = LoggerFactory.getLogger(SingleBeanFactoryTest.class);
    private SingleBeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = new SingleBeanFactory(
                new ConfigurationScanner("nextstep.di.example"),
                new AnnotatedBeanScanner("nextstep.di.example"));

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

    @Test
    void BeanDuplicated() {
        Set<TestBeanDefinition> source = new HashSet<>(Arrays.asList(
                new TestBeanDefinition(ExampleConfig.class),
                new TestBeanDefinition(DataSource.class)
        ));

        Set<TestBeanDefinition> changed = new HashSet<>(Arrays.asList(
                new TestBeanDefinition(DataSource.class),
                new TestBeanDefinition(ExampleConfig.class)
        ));

        logger.debug("RetailAll : {}", source.retainAll(changed));
        logger.debug("BeanDuplicated test : {}", source);
    }
}
