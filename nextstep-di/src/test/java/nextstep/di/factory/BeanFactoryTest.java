package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);
    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {

        beanFactory = new BeanFactory();

    }

    @Test
    public void 컴포넌트_di() {
        ComponentBeanScanner scanner = new ComponentBeanScanner("nextstep.di.factory.example");
        beanFactory.addScanner(scanner);
        beanFactory.initialize();
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void 빈_어노테이션_di() {
        ConfigurationBeanScanner scanner = new ConfigurationBeanScanner(ExampleConfig.class);
        beanFactory.addScanner(scanner);
        beanFactory.initialize();

        DataSource dataSource = beanFactory.getBean(DataSource.class);
        assertThat(dataSource).isNotNull();
    }
}
