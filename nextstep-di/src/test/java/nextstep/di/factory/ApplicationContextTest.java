package nextstep.di.factory;

import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationContextTest {
    @Test
    public void BeanScanner_통합() {
        ApplicationContext applicationContext = new ApplicationContext(IntegrationConfig.class);
        assertNotNull(applicationContext.getBean(DataSource.class));

        JdbcUserRepository userRepository = applicationContext.getBean(JdbcUserRepository.class);
        assertNotNull(userRepository);
        assertNotNull(userRepository.getDataSource());

        MyJdbcTemplate jdbcTemplate = applicationContext.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }
}