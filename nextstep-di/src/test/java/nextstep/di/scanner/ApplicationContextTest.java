package nextstep.di.scanner;

import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.test.ComponentScanTest;
import nextstep.di.factory.test.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationContextTest {
    @Test
    public void ApplicationContextTest() {
        ApplicationContext ac = new ApplicationContext(IntegrationConfig.class);

        assertNotNull(ac.getBean(DataSource.class));

        JdbcUserRepository userRepository = ac.getBean(JdbcUserRepository.class);
        assertNotNull(userRepository);
        assertNotNull(userRepository.getDataSource());

        MyJdbcTemplate jdbcTemplate = ac.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }

    @Test
    @DisplayName("basePackage가 2개 일때")
    void configTest() {
        ApplicationContext ac = new ApplicationContext(TestConfig.class);

        assertNotNull(ac.getBean(DataSource.class));

        MyJdbcTemplate jdbcTemplate = ac.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());

        ComponentScanTest test = ac.getBean(ComponentScanTest.class);
        assertNotNull(test);
        assertNotNull(test.getTestName());
    }
}
