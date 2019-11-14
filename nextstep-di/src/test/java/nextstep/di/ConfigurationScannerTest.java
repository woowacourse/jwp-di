package nextstep.di;

import nextstep.annotation.Bean;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationScannerTest {

    @Test
    void findPackagesInComponentScan() {
        ConfigurationScanner configurationScanner = new ConfigurationScanner();
        List<String> basePackages = configurationScanner.findPackagesInComponentScan();

        assertThat(basePackages.size()).isEqualTo(3);
        assertThat(basePackages.contains("nextstep.di.factory.example.controller")).isTrue();
        assertThat(basePackages.contains("nextstep.di.factory.example.service")).isTrue();
        assertThat(basePackages.contains("nextstep.di.factory.example.repository")).isTrue();
    }

    @Test
    void findMethodsWithAnnotation() {
        ConfigurationScanner configurationScanner = new ConfigurationScanner();
        List<Method> methods = configurationScanner.findMethodsWithAnnotation(Bean.class);

        assertThat(methods.size()).isEqualTo(3);
    }
}