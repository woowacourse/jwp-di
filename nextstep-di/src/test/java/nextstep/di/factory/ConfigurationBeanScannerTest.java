package nextstep.di.factory;

import nextstep.di.factory.example.core.ExampleConfig;
import org.junit.jupiter.api.Test;

import javax.activation.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationBeanScannerTest {
    @Test
    public void register_simple() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(ExampleConfig.class);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }
}