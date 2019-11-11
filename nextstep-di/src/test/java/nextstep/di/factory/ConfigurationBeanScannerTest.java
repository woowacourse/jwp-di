package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationBeanScannerTest {
    @Test
    @DisplayName("@ComponentScan 해줬을 경우 스캔 확인")
    void scan_Test() {
        ConfigurationBeanScanner scanner = new ConfigurationBeanScanner(IntegrationConfig.class);
        Set<Class<?>> actual = Set.of(IntegrationConfig.class, ExampleConfig.class);
        Set<Class<?>> expected = scanner.getClassTypes();
        assertThat(expected).containsAll(actual);
    }

    @Test
    void Configuration_없을경우_예외처리() {
        assertThrows(IllegalArgumentException.class, () -> new ConfigurationBeanScanner(ExampleConfig.class));
    }
}