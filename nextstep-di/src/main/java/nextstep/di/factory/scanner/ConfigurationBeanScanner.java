package nextstep.di.factory.scanner;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanCreateMatcher;
import nextstep.di.factory.instantiation.MethodInstantiation;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationBeanScanner.class);

    private final Reflections reflection;

    public ConfigurationBeanScanner(Object... basePackage) {
        this.reflection = new Reflections(basePackage);
    }

    public void register(BeanCreateMatcher beanCreateMatcher) {
        Set<Class<?>> typesAnnotatedWith = reflection.getTypesAnnotatedWith(Configuration.class);
        typesAnnotatedWith.forEach(configurationClazz -> registerMethodBean(beanCreateMatcher, configurationClazz));
        logger.debug("Beans : {}", beanCreateMatcher);
    }

    private void registerMethodBean(BeanCreateMatcher beanCreateMatcher, Class<?> configurationClazz) {
        Set<Method> methods = Arrays.stream(configurationClazz.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(Bean.class))
            .collect(Collectors.toSet());
        for (Method method : methods) {
            beanCreateMatcher.put(method.getReturnType(),
                new MethodInstantiation(method, BeanUtils.instantiateClass(configurationClazz)));
        }
    }
}
