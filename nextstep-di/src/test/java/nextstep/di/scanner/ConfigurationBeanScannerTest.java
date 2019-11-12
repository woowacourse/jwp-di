package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationBeanScannerTest {
    private Scanner2 scanner;

    @BeforeEach
    void setUp() {
        scanner = new ConfigurationBeanScanner();
    }

    @Test
    void configuration_bean_가져오기() {
        Set<BeanDefinition> beans = scanner.scan("nextstep.di.factory.example");
        Set<? extends Class<?>> clazz = beans.stream()
                .map(BeanDefinition::getClazz)
                .collect(Collectors.toSet());

        // DataSource 와 JdbcTemplate
        assertThat(beans.size()).isEqualTo(2);
        assertTrue(clazz.contains(DataSource.class));
    }
}