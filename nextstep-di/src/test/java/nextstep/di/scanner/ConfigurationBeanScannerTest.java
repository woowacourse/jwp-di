package nextstep.di.scanner;

import nextstep.di.factory.BeanFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationBeanScannerTest {
    private static final String TEST_BASE_PACKAGE = "nextstep.di.factory.example";

    @Test
    public void register_simple() {
        ApplicationContext ac = new ApplicationContext(TEST_BASE_PACKAGE);
        assertNotNull(ac.getBean(DataSource.class));
    }

    @Test
    void read_configuration() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.registerPackage(TEST_BASE_PACKAGE);
    }
}
