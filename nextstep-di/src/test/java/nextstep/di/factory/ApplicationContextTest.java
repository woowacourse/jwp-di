package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApplicationContextTest {
    private ApplicationContext applicationContext;

    @Test
    public void register_simple() {
        applicationContext = new ApplicationContext(ExampleConfig.class);
        applicationContext.initialize();

        assertNotNull(applicationContext.getBean(DataSource.class));
    }

    @Test
    public void register_classpathBeanScanner_통합() {
        applicationContext = new ApplicationContext(IntegrationConfig.class);
        applicationContext.initialize();
        
        assertNotNull(applicationContext.getBean(DataSource.class));

        JdbcUserRepository userRepository = applicationContext.getBean(JdbcUserRepository.class);
        assertNotNull(userRepository);
        assertNotNull(userRepository.getDataSource());
        assertEquals(userRepository.getDataSource(), applicationContext.getBean(DataSource.class));

        MyJdbcTemplate jdbcTemplate = applicationContext.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
        assertEquals(jdbcTemplate.getDataSource(), applicationContext.getBean(DataSource.class));
    }
}
