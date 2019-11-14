package nextstep.di.factory.domain.beandefinition;

import nextstep.annotation.Bean;
import nextstep.di.factory.example.ExampleConfig;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationBeanDefinitionTest {

    @Test
    public void instanceHasNoParameters() {
        Class<?> clazz = ExampleConfig.class;
        Method[] methods = clazz.getMethods();
        List<Method> beanMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Bean.class)) {
                beanMethods.add(method);
            }
        }

        ConfigurationBeanDefinition configurationBeanDefinition = new ConfigurationBeanDefinition(beanMethods.get(0));
        Object instance = configurationBeanDefinition.makeInstance();
        assertThat(instance).isInstanceOf(BasicDataSource.class);
    }
}