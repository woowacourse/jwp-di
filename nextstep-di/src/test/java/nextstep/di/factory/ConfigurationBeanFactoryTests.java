package nextstep.di.factory;

import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConfigurationBeanFactoryTests {
    private BeanDefinition beanDefinition;
    private ConfigurationBeanFactory configurationBeanFactory;

    @BeforeEach
    void setUp() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");
        Set<Class<?>> preInstantiateComponents = beanScanner.scan();
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner();
        Set<Class<?>> preInstantiateConfigurationBeans = configurationBeanScanner.scan(IntegrationConfig.class);

        beanDefinition = new BeanDefinition(preInstantiateComponents, preInstantiateConfigurationBeans);
        configurationBeanFactory = new ConfigurationBeanFactory(beanDefinition, IntegrationConfig.class);
    }

    @Test
    void initialize() {
        ComponentFactory componentFactory = new ComponentFactory(beanDefinition);
        Map<Class<?>, Object> components = componentFactory.initialize();
        Map<Class<?>, Object> configurationBeans = configurationBeanFactory.initialize(components);

        assertNotNull(configurationBeans.get(MyJdbcTemplate.class));
        assertNotNull(configurationBeans.get(DataSource.class));
    }

    @Test
    @DisplayName("Config 클래스에서 component의 DI가 가능한지 테스트")
    void component_configuration_di() {
        ComponentFactory componentFactory = new ComponentFactory(beanDefinition);
        Map<Class<?>, Object> components = componentFactory.initialize();
        assertDoesNotThrow(() -> configurationBeanFactory.initialize(components));
    }
}
