package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConfigurationBeanScannerTest {

    @Test
    public void register_simple() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(beanFactory);
        configurationBeanScanner.register(ExampleConfig.class);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }
}
