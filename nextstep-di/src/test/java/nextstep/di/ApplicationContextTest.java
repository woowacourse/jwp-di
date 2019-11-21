package nextstep.di;

import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationContextTest {
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        applicationContext = new ApplicationContext("nextstep.di.factory.example");
    }

    @Test
    @DisplayName("ApplicationContext로 ConfigurationBeanScanner와 ClassPathBeanScanner 통합")
    public void integration() {
        assertNotNull(applicationContext.getBean(DataSource.class));

        JdbcUserRepository userRepository = applicationContext.getBean(JdbcUserRepository.class);
        assertNotNull(userRepository);
        assertNotNull(userRepository.getDataSource());

        MyJdbcTemplate jdbcTemplate = applicationContext.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }
}