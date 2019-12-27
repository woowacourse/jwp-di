package nextstep.di.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentScannerTest {
    private ComponentScanner componentScanner;
    @BeforeEach
    void setUp() {
        componentScanner = new ComponentScanner("nextstep");
    }

    @Test
    void scan() {
        assertThat(componentScanner.findBasePackages()[0]).isEqualTo("nextstep.annotation");
    }
}