package nextstep.di.factory;

import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationBeanScannerTest {
    private ConfigurationBeanScanner configurationBeanScanner;

    @BeforeEach
    void setUp() {
        configurationBeanScanner = new ConfigurationBeanScanner("nextstep.di.factory.example");
    }

    @Test
    void beanScan() {
        BeanCreateMatcher createMatcher = configurationBeanScanner.scanBean(new BeanCreateMatcher());
        assertTrue(createMatcher.containsKey(MyJdbcTemplate.class));
    }
}