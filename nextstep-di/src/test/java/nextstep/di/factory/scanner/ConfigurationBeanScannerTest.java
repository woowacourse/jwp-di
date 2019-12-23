package nextstep.di.factory.scanner;

import nextstep.di.factory.definition.BeanDefinition;
import nextstep.di.factory.example.IntegrationConfig;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationBeanScannerTest {

    @Test
    void scan() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(IntegrationConfig.class);
        Set<BeanDefinition> beanDefinitions = configurationBeanScanner.scan();
        assertThat(beanDefinitions.size()).isEqualTo(2);
    }
}
