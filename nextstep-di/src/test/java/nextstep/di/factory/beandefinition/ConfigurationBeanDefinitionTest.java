package nextstep.di.factory.beandefinition;

import nextstep.di.factory.example.IntegrationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationBeanDefinitionTest {
    private static final int NONE = 0;
    private ConfigurationBeanDefinition configurationBeanDefinition;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        configurationBeanDefinition = new ConfigurationBeanDefinition(
                new IntegrationConfig(),
                DataSource.class,
                IntegrationConfig.class.getMethod("dataSource")
        );
    }

    @Test
    public void getClassTypeTest() {
        assertThat(configurationBeanDefinition.getClassType()).isEqualTo(DataSource.class);
    }

    @Test
    public void getParameterTypesTest() {
        assertThat(configurationBeanDefinition.getParameterTypes().length).isEqualTo(NONE);
    }

    @Test
    public void instantiateTest() {
        Object instance = configurationBeanDefinition.instantiate();

        assertThat(instance).isInstanceOf(DataSource.class);
    }
}
