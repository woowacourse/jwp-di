package nextstep.di.factory.scanner;

import nextstep.di.factory.BeanCreateMatcher;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationBeanScannerTest {
    private BeanCreateMatcher beanCreateMatcher;
    private ConfigurationBeanScanner configurationBeanScanner;

    @BeforeEach
    void setUp() {
        beanCreateMatcher = new BeanCreateMatcher();
        configurationBeanScanner = new ConfigurationBeanScanner("nextstep.di.factory.example");
    }

    @Test
    void register() {
        configurationBeanScanner.register(beanCreateMatcher);
        assertTrue(beanCreateMatcher.containsKey(DataSource.class));
        assertTrue(beanCreateMatcher.containsKey(MyJdbcTemplate.class));
    }
}