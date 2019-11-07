package nextstep.di.factory;

import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;

    @BeforeEach
    public void setup() {
        beanFactory = new BeanFactory("nextstep.di.factory.example");
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
    void sameInstance() {
        DataSource dataSource = beanFactory.getBean(DataSource.class);
        MyJdbcTemplate myJdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertThat(dataSource).isEqualTo(beanFactory.getBean(DataSource.class));
        assertThat(myJdbcTemplate).isEqualTo(beanFactory.getBean(MyJdbcTemplate.class));
        assertThat(dataSource).isEqualTo(myJdbcTemplate.getDataSource());
    }
}
