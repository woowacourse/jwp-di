package nextstep.di.scanner;

import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.exception.EmptyBasePackagesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationBeanScannerTest {
    private BeanFactory beanFactory;
    private ConfigurationBeanScanner beanScanner;

    @BeforeEach
    void setUp() {
        beanFactory = new BeanFactory();
        beanScanner = new ConfigurationBeanScanner(beanFactory);
    }

    @Test
    void scanBeans() {
        beanScanner.scanBeans("nextstep.di.factory.example");
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
        assertNotNull(beanFactory.getBean(MyJdbcTemplate.class));
    }

    @Test
    void scanBeansWithEmptyBasePackages() {
        assertThrows(EmptyBasePackagesException.class, beanScanner::scanBeans);
    }
}