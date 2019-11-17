package nextstep.di.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConfigurationBeanScannerTest {
    private BeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner();
        Set<Class<?>> scannedCongiurationBeans = configurationBeanScanner.scan();
        beanFactory = new BeanFactory(scannedCongiurationBeans);
        beanFactory.initialize();
    }

    @Test
    void scan() {
        DataSource bean = beanFactory.getBean(DataSource.class);
        assertNotNull(bean);
    }
}
