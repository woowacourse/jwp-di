package nextstep.di.factory.domain;

import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.stereotype.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericApplicationContextTest {
    private ApplicationContext applicationContext;

    @BeforeEach
    public void setUp() {
        applicationContext = new GenericApplicationContext(IntegrationConfig.class);
        applicationContext.initialize();
    }

    @Test
    public void getBean() {
        assertThat(applicationContext.getBean(JdbcUserRepository.class))
                .isInstanceOf(JdbcUserRepository.class);
    }

    @Test
    public void getSupportedClassByAnnotation() {
        assertThat(applicationContext.getSupportedClassByAnnotation(Repository.class))
                .contains(JdbcUserRepository.class)
                .contains(JdbcQuestionRepository.class);
    }

    @Test
    public void singleInstanceTest() {
        assertThat(applicationContext.getBean(MyJdbcTemplate.class))
                .isEqualTo(applicationContext.getBean(MyJdbcTemplate.class));
    }
}