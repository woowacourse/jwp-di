package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.example.config.IntegrationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    void sameInstanceMethod() {
        DataSource dataSource = beanFactory.getBean(DataSource.class);
        MyJdbcTemplate myJdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertThat(dataSource).isEqualTo(beanFactory.getBean(DataSource.class));
        assertThat(myJdbcTemplate).isEqualTo(beanFactory.getBean(MyJdbcTemplate.class));
        assertThat(dataSource).isEqualTo(myJdbcTemplate.getDataSource());
    }

    @Test
    void sameInstanceConstructor() {
        MyQnaService myQnaService = beanFactory.getBean(MyQnaService.class);
        JdbcUserRepository jdbcUserRepository = beanFactory.getBean(JdbcUserRepository.class);
        assertThat(myQnaService).isEqualTo(beanFactory.getBean(MyQnaService.class));
        assertThat(jdbcUserRepository).isEqualTo(beanFactory.getBean(JdbcUserRepository.class));
        assertThat(jdbcUserRepository).isEqualTo(myQnaService.getUserRepository());
    }

    @DisplayName("@ComponentScan이 붙은 클래스를 생성자로 주입")
    @Test
    void componentScan() {
        BeanFactory beanFactory = new BeanFactory(IntegrationConfig.class);
        beanFactory.initialize();

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @DisplayName("@ComponentScan이 붙지 않은 클래스를 생성자로 주입. 해당 클래스의 위치부터 하위 패키지가 scan 대상")
    @Test
    void configuration() {
        BeanFactory beanFactory = new BeanFactory(ExampleConfig.class);
        beanFactory.initialize();

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @AfterEach
    void tearDown() {
        beanFactory = null;
    }
}
