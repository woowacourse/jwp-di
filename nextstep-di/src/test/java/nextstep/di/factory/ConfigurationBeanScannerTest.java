package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationBeanScannerTest {
    @Test
    public void register_simple() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(ExampleConfig.class);
        beanFactory.initialize2();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    void register_simple2() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(IntegrationConfig.class);
        beanFactory.initialize2();

        assertNotNull(beanFactory.getBean(DataSource.class));
        assertNotNull(beanFactory.getBean(MyJdbcTemplate.class));
    }

    @Test
    void register_simple3() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(MyJdbcConfig.class, ExampleConfig.class);
        beanFactory.initialize2();

        assertNotNull(beanFactory.getBean(DataSource.class));
        assertNotNull(beanFactory.getBean(MyJdbcTemplate.class));
    }
}