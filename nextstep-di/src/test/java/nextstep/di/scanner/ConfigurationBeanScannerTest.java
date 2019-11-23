package nextstep.di.scanner;

import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.ExampleConfig;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationBeanScannerTest {
    private static final String TEST_BASE_PACKAGE = "nextstep.di.factory.example";

    @Test
    public void register_simple() {
        ApplicationContext ac = new ApplicationContext(ExampleConfig.class);
        assertNotNull(ac.getBean(DataSource.class));
    }

    @Test
    void read_configuration() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.registerPackage(TEST_BASE_PACKAGE);
    }
}
