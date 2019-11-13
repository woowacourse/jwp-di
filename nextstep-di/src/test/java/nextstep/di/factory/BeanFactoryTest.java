package nextstep.di.factory;

import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BeanFactoryTest {
    private BeanCreateMatcher beanCreateMatcher;
    private BeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        beanCreateMatcher = new BeanCreateMatcher();
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");
        beanScanner.scanBean(beanCreateMatcher, Controller.class, Service.class, Repository.class);

        beanFactory = new BeanFactory(beanCreateMatcher);
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
}