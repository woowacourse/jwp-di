package nextstep.di.scanner;

import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyQnaService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClassPathScannerTest {
    @Test
    void getBasePackages() {
        ClassPathScanner scanner = new ClassPathScanner(IntegrationConfig.class);
        assertThat(scanner.getPackages())
                .contains("nextstep.di.factory.example.scan")
                .contains("nextstep.di.factory.example");
    }

    @Test
    void doesNotHaveComponentScanAnnotation() {
        assertThatThrownBy(() -> new ClassPathScanner(MyQnaService.class))
                .isInstanceOf(RuntimeException.class);
    }
}