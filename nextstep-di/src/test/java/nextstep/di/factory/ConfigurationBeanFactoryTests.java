package nextstep.di.factory;

import nextstep.annotation.Configuration;
import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;

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
}
