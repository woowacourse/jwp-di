package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ConfigurationBeanScannerTest {

    @Test
    public void register_simple() {
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(ExampleConfig.class);
        Set<Class<?>> configurations = cbs.scan();

        assertThat(configurations).isNotNull();
        assertThat(configurations.contains(ExampleConfig.class)).isTrue();
        assertThat(configurations.contains(IntegrationConfig.class)).isTrue();
    }
}
