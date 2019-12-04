package nextstep.di.scanner;

import java.util.List;
import javax.sql.DataSource;

import nextstep.di.factory.BeanFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationBeanScannerTest {
    @Test
    public void register_simple() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.scanConfigurationBeans();
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    void appendBasePackages() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        List<String> basePackages = cbs.appendBasePackage();

        assertTrue(basePackages.contains("nextstep.di.factory.example"));
        assertTrue(basePackages.contains("nextstep.di.factory.scannerdi"));
    }
}