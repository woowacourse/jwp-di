package nextstep.di.factory;

import nextstep.di.configuration.example.JdbcTemplateStub;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.scanner.ComponentScanner;
import nextstep.di.scanner.ConfigurationScanner;
import nextstep.stereotype.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final String BASE_PACKAGE = "nextstep.di.factory.example";

    private BeanFactory beanFactory;

    @BeforeEach
    public void setup() {
        beanFactory = new BeanFactory(new ComponentScanner(BASE_PACKAGE),
                new ConfigurationScanner(BASE_PACKAGE));
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
        BeanFactory beanFactory = new BeanFactory(new ConfigurationScanner("nextstep.di.configuration.example"));
        beanFactory.initialize();

        // when
        JdbcTemplateStub bean = beanFactory.getBean(JdbcTemplateStub.class);

        // then
        assertThat(bean).isNotNull();
    }

    @Test
    void get_bean_by_annotation() {
        // given
        beanFactory.initialize();

        // when & then
        assertThat(beanFactory.getBeansWithAnnotation(Repository.class)).hasSize(2);
    }
}
