package nextstep.di.factory;

import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigurationBeanScannerTest {
    private ConfigurationBeanScanner configurationBeanScanner;

    @BeforeEach
    void setUp() {
        configurationBeanScanner = new ConfigurationBeanScanner();
    }

    @Test
    @DisplayName("설정파일의 Bean 메소드를 찾아서 등록")
    void register() {
        Set<Class<?>> classes = configurationBeanScanner.scan(IntegrationConfig.class);
        assertTrue(classes.contains(DataSource.class));
        assertTrue(classes.contains(MyJdbcTemplate.class));
    }
}
