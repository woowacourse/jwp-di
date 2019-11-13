package nextstep.di.scanner;

import nextstep.di.factory.example.VoidConfig;
import nextstep.di.factory.scan.ComponentScanConfig;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClassPathScannerTest {
    @Test
    void getBasePackages() {
        ClassPathScanner scanner = new ClassPathScanner(ComponentScanConfig.class);
        assertThat(scanner.getBasePackages())
                .contains("nextstep.di.factory.scan")
                .contains("nextstep.di.factory.example");
    }

    @Test
    void notFoundComponentScan() {
        assertThatThrownBy(() -> new ClassPathScanner(VoidConfig.class)).isInstanceOf(RuntimeException.class);
    }
}