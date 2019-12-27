package nextstep.di.beans.factory.example;

import nextstep.di.context.ApplicationContext;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConfigurationBeanScannerTest {
    @Test
    public void register_classpathBeanScanner_통합() {
        ApplicationContext applicationContext = new ApplicationContext("nextstep.di.beans.factory.example");

        assertNotNull(applicationContext.getBean(DataSource.class));

        JdbcUserRepository userRepository = applicationContext.getBean(JdbcUserRepository.class);
        assertNotNull(userRepository);
        assertNotNull(userRepository.getDataSource());

        MyJdbcTemplate jdbcTemplate = applicationContext.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }
}
