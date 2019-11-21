package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.example.IntegrationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MethodBeanScannerTest {

    private List<BeanDefinition> beanDefinitions;

    @BeforeEach
    void setUp() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(IntegrationConfig.class);
        beanDefinitions = configurationBeanScanner.getBeanDefinitions();
    }

    @Test
    @DisplayName("@Bean이 있는 method 스캔 성공")
    void beanMethodScanTest() {
        MethodBeanScanner methodBeanScanner = new MethodBeanScanner(beanDefinitions);
        Set<? extends Class<?>> actual = methodBeanScanner.getBeanDefinitions().stream()
                .map(BeanDefinition::getBeanClass)
                .collect(Collectors.toSet());

        Set<? extends Class<?>> expected = Stream.of(IntegrationConfig.class.getDeclaredMethods())
                .map(Method::getReturnType)
                .collect(Collectors.toSet());

        assertThat(actual).isEqualTo(expected);
    }
}