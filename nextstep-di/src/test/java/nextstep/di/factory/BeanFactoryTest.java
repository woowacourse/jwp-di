package nextstep.di.factory;

import nextstep.di.configuration.example.JdbcTemplateStub;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.exception.CircularReferenceException;
import nextstep.di.scanner.BeanScanner;
import nextstep.di.scanner.ComponentScanner;
import nextstep.di.scanner.ConfigurationScanner;
import nextstep.stereotype.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        BeanScanner beanScanner = new ComponentScanner("nextstep.di.factory.example");
        beanFactory = new BeanFactory(beanScanner.getBeanDefinitions());
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
    void configuration() {
        // given
        BeanScanner beanScanner = new ConfigurationScanner("nextstep.di.configuration.example");
        BeanFactory beanFactory = new BeanFactory(beanScanner.getBeanDefinitions());
        beanFactory.initialize();

        // when
        JdbcTemplateStub bean = beanFactory.getBean(JdbcTemplateStub.class);

        // then
        assertThat(bean).isNotNull();
    }

    @Test
    void get_bean_by_annotation() {
        // given
        BeanScanner beanScanner = new ComponentScanner("nextstep.di.factory.example");
        BeanFactory beanFactory = new BeanFactory(beanScanner.getBeanDefinitions());
        beanFactory.initialize();

        // when & then
        assertThat(beanFactory.getBeansWithAnnotation(Repository.class)).hasSize(2);
    }

    @Test
    void fail_with_circular_reference() {
        BeanScanner beanScanner = new ComponentScanner("nextstep.di.factory.failexample.circularreference");
        BeanFactory beanFactory = new BeanFactory(beanScanner.getBeanDefinitions());
        assertThrows(CircularReferenceException.class, () -> beanFactory.initialize());
    }
}
