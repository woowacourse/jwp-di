package nextstep.di;

import nextstep.annotation.Bean;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ConfigurationBeanScanner;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationBeanScannerTest {

    @Test
    void findPackagesInComponentScan() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(new BeanFactory());
        configurationBeanScanner.scan();

        List<String> basePackages = configurationBeanScanner.findPackagesInComponentScan();

        assertThat(basePackages.size()).isEqualTo(3);
        assertThat(basePackages.contains("nextstep.di.factory.example.controller")).isTrue();
        assertThat(basePackages.contains("nextstep.di.factory.example.service")).isTrue();
        assertThat(basePackages.contains("nextstep.di.factory.example.repository")).isTrue();
    }

    @Test
    void findMethodsWithAnnotation() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(new BeanFactory());
        configurationBeanScanner.scan();

        List<Method> methods = configurationBeanScanner.findMethodsWithAnnotation(Bean.class);

        assertThat(methods.size()).isEqualTo(3);
    }
}