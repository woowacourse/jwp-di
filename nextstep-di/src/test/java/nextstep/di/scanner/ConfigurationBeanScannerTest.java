package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationBeanScannerTest {

    @Test
    @DisplayName("@ComponentScan 스캔 확인")
    void scan_Test() {
        ConfigurationBeanScanner scanner = new ConfigurationBeanScanner(IntegrationConfig.class);
        Set<Class<?>> actual = scanner.getBeanDefinitions().stream()
                .map(BeanDefinition::getBeanClass)
                .collect(Collectors.toSet());

        Set<Class<?>> expected = Set.of(IntegrationConfig.class, ExampleConfig.class);

        assertThat(actual).containsAll(expected);
    }
}