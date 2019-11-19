package nextstep.di.scanner;

import nextstep.di.context.ApplicationBeanContext;
import nextstep.di.example.ExampleConfig;
import nextstep.di.example.IntegrationConfig;
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
        BeanScanner beanScanner = new ConfigurationScanner(new ApplicationBeanContext(ExampleConfig.class));
        beanFactory = new SingleBeanFactory(new BeanRegistry(), beanScanner);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    public void register_classpathBeanScanner_통합() {
        BeanRegistry beanRegistry = new BeanRegistry();
        beanFactory = new SingleBeanFactory(beanRegistry,
                new ConfigurationScanner(new ApplicationBeanContext(IntegrationConfig.class)),
                new AnnotatedBeanScanner(new ApplicationBeanContext("nextstep.di.example")));
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