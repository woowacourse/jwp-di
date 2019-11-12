package nextstep.di.scanner;

import nextstep.di.example.ExampleConfig;
import nextstep.di.example.IntegrationConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ComponentScannerTest {

    @Test
    @DisplayName("@ComponentScan basePackages 스캔 확인")
    void scan_Test() {
        Object[] expected = new Object[]{"nextstep.di.example"};
        ComponentScanner scanner = new ComponentScanner(IntegrationConfig.class);

        Object[] actual = scanner.getBasePackages();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void ComponentScan_없을경우_예외처리() {
        assertThrows(IllegalStateException.class, () -> new ComponentScanner(ExampleConfig.class));
    }
}