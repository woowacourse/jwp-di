package nextstep.di.factory;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationBeanScannerTest {
    @Test
    public void register_simple() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.scanConfigurationBeans();
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }
}