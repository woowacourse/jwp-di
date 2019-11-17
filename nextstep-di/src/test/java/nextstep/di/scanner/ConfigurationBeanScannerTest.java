package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.config.ExampleConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationBeanScannerTest {
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        List<Object> basePackages = Collections.singletonList("nextstep.di.factory.example");
        scanner = new ConfigurationBeanScanner(basePackages.toArray());
    }

    @Test
    void configuration_bean_가져오기() {
        Set<BeanDefinition> beans = scanner.scan();
        Set<? extends Class<?>> clazz = beans.stream()
                .map(BeanDefinition::getClazz)
                .collect(Collectors.toSet());

        assertTrue(clazz.contains(DataSource.class));
        assertTrue(clazz.contains(MyJdbcTemplate.class));
    }
}