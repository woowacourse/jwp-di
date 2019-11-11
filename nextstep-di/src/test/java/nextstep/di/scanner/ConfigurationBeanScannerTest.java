package nextstep.di.scanner;

import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationBeanScannerTest {
    private ConfigurationBeanScanner beanScanner = new ConfigurationBeanScanner("nextstep.di.factory.example");
    private final List<Class<?>> executeResult = Arrays.asList(DataSource.class,
            MyJdbcTemplate.class);

    @Test
    void getInstantiatedTypes() {
        for (Class<?> instantiatedType : beanScanner.getInstantiatedTypes()) {
            assertThat(executeResult).contains(instantiatedType);
        }
    }

    @Test
    void isContainsBean() {
        for (Class<?> clazz : executeResult) {
            assertThat(beanScanner.isContainsBean(clazz)).isTrue();
        }
    }
}