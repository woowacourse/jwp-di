package nextstep.di.scanner;

import nextstep.di.example.JdbcUserRepository;
import nextstep.di.example.MyJdbcTemplate;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.SingleBeanFactory;
import nextstep.di.registry.BeanRegistry;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationScannerTest {
    BeanFactory beanFactory;

    @Test
    public void register_simple() {
        beanFactory = new SingleBeanFactory(new BeanRegistry(),
                new ConfigurationScanner("nextstep.di.example"));

        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    public void register_classpathBeanScanner_통합() {
        beanFactory = new SingleBeanFactory(new BeanRegistry(),
                new ConfigurationScanner("nextstep.di.example"),
                new AnnotatedBeanScanner("nextstep.di.example"));
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));

        JdbcUserRepository userRepository = beanFactory.getBean(JdbcUserRepository.class);
        assertNotNull(userRepository);
        assertNotNull(userRepository.getDataSource());

        MyJdbcTemplate jdbcTemplate = beanFactory.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }
}