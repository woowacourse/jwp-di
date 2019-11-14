package nextstep.di.scanner;

import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.ExampleConfig;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationBeanScannerTest {
    @Test
    public void register_simple() {
        BeanFactory beanFactory = new BeanFactory();
        ClasspathBeanScanner beanScanner = new ClasspathBeanScanner(beanFactory);
        beanScanner.doScan("samples");
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(ExampleConfig.class);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }
}