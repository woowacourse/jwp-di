package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.scanner.ConfigurationBeanScanner;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationBeanScannerTest {

    @Test
    void register_simple() {
        final ConfigurationBeanScanner beanScanner = new ConfigurationBeanScanner("nextstep.di.factory.example");
        final BeanFactory beanFactory = new BeanFactory(beanScanner);

        assertThat(beanFactory.getBean(ExampleConfig.class)).isNotNull();
    }
}