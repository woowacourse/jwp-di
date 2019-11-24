package nextstep.di.scanner;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentScannerTest {

    @Test
    @DisplayName("ComponentScan으로 basePackage 가져오기")
    void basePackageScanTest() {
        ComponentScanner componentScanner = new ComponentScanner(ExampleConfig.class, IntegrationConfig.class);
        Object[] actual = componentScanner.getBasePackages();
        Object[] expected = new Object[] {"nextstep.di.factory.example"};

        assertThat(actual).isEqualTo(expected);
    }
}