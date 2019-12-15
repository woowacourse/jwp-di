package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.annotation.Configuration;
import nextstep.di.scanner.package1.ComponentPackage1;
import nextstep.di.scanner.package1.ConfigWithoutScan;
import nextstep.di.scanner.package2.ComponentPackage2;
import nextstep.di.scanner.package2.ConfigScanDefault;
import nextstep.di.scanner.package2.ConfigScanPackage1;
import nextstep.di.scanner.package3.ConfigScanPackage2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationScannerTest {
    private static final Set<Class<?>> package1Classes = Sets.newHashSet(Arrays.asList(
            ComponentPackage1.class,
            ConfigWithoutScan.class
    ));

    private static final Set<Class<?>> package2Classes = Sets.newHashSet(Arrays.asList(
            ComponentPackage2.class,
            ConfigScanDefault.class,
            ConfigScanPackage1.class
    ));

    @Test
    @DisplayName("@Configuration 이 달리지 않은 클래스로 초기화")
    void constructor_notConfigurationClass() {
        assertThrows(IllegalStateException.class,
                () -> ConfigurationScanner.from(Sets.newHashSet(ClassWithoutConfiguration.class)));
    }

    @Test
    @DisplayName("@Configuration 이 달린 클래스로 초기화")
    void constructor_WithConfigurationClass() {
        assertDoesNotThrow(() -> ConfigurationScanner.from(Sets.newHashSet(ClassWithConfiguration.class)));
    }

    @Test
    void scan_withoutComponentScan() {
        Set<Class<?>> expectedClasses = Sets.newHashSet(Arrays.asList(
                ConfigWithoutScan.class
        ));
        ConfigurationScanner configurationScanner = ConfigurationScanner.of(ConfigWithoutScan.class);

        assertThat(configurationScanner.scan()).isEqualTo(expectedClasses);
    }

    @Test
    void scan_withDefaultComponentScan() {
        Set<Class<?>> expectedClasses = package2Classes;
        ConfigurationScanner configurationScanner = ConfigurationScanner.of(ConfigScanDefault.class);

        assertThat(configurationScanner.scan()).isEqualTo(expectedClasses);
    }

    @Test
    void scan_ComponentScanToPackage1() {
        Set<Class<?>> expectedClasses = Sets.newHashSet((Class<?>) ConfigScanPackage1.class);
        expectedClasses.addAll(package1Classes);

        ConfigurationScanner configurationScanner = ConfigurationScanner.of(ConfigScanPackage1.class);

        assertThat(configurationScanner.scan()).isEqualTo(expectedClasses);
    }

    @Test
    @DisplayName("@ComponentScan 을 한 패키지 내부에 @ComponentScan 이 존재할 경우 스캔이 적용되는지 확인")
    void scan_indirectPackages() {
        // 먼저 ConfigScanPackage2 가 동작한다.
        // 그런데 package2 에는 ConfigScanPackage1 이 존재한다.
        // 결과적으로는 ConfigScanPackage2 + package2Classes + package1Classes 가 찾아진다
        Set<Class<?>> expectedClasses = Sets.newHashSet(ConfigScanPackage2.class);
        expectedClasses.addAll(package1Classes);
        expectedClasses.addAll(package2Classes);

        ConfigurationScanner configurationScanner = ConfigurationScanner.of(ConfigScanPackage2.class);

        assertThat(configurationScanner.scan()).isEqualTo(expectedClasses);
    }

    class ClassWithoutConfiguration {
    }

    @Configuration
    class ClassWithConfiguration {
    }

}