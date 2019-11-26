package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationBeanScannerTest {
    private ConfigurationBeanScanner configurationBeanScanner;

    @Test
    public void register_simple() {
        configurationBeanScanner = new ConfigurationBeanScanner(ExampleConfig.class);

        assertTrue(configurationBeanScanner.doScan()
                .stream()
                .anyMatch(beanDefinition -> beanDefinition.sameBeanClass(DataSource.class))
        );
    }

    @Test
    void register_simple2() {
        configurationBeanScanner = new ConfigurationBeanScanner(IntegrationConfig.class);

        assertTrue(configurationBeanScanner.doScan()
                .stream()
                .allMatch(beanDefinition -> beanDefinition.sameBeanClass(DataSource.class) ||
                        beanDefinition.sameBeanClass(MyJdbcTemplate.class))
        );
    }

    @Test
    void register_simple3() {
        configurationBeanScanner = new ConfigurationBeanScanner(MyJdbcConfig.class, ExampleConfig.class);

        assertTrue(configurationBeanScanner.doScan()
                .stream()
                .allMatch(beanDefinition -> beanDefinition.sameBeanClass(DataSource.class) ||
                        beanDefinition.sameBeanClass(MyJdbcTemplate.class))
        );
    }
}