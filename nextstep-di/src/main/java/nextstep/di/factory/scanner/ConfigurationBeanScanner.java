package nextstep.di.factory.scanner;

import nextstep.annotation.Bean;
import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.beandefinition.ConfigurationBeanDefinition;
import nextstep.exception.BeanCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationBeanScanner.class);

    private final Class<?>[] configurations;

    public ConfigurationBeanScanner(Class<?>... configurations) {
        this.configurations = configurations;
    }

    public Set<BeanDefinition> scan() {
        return Arrays.stream(configurations)
                .flatMap(config -> Arrays.stream(config.getMethods()))
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(method -> new ConfigurationBeanDefinition(getDeclaringObject(method), method.getReturnType(), method))
                .collect(Collectors.toSet());
    }

    private Object getDeclaringObject(Method method) {
        try {
            return method.getDeclaringClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new BeanCreationException(e);
        }
    }
}
