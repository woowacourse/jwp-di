package nextstep.di;

import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactoryUtils;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner implements BeanScanner {
    private Reflections reflections;

    public ConfigurationBeanScanner(Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    @Override
    public Set<BeanSpecification> scan() {
        return this.reflections.getTypesAnnotatedWith(Configuration.class).stream()
                .map(this::getBeanSpecifications)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<BeanSpecification> getBeanSpecifications(Class<?> clazz) {
        Constructor<?> constructor = BeanFactoryUtils.getDefaultConstructor(clazz);
        constructor.setAccessible(true);
        Object instance = BeanFactoryUtils.instantiate(constructor);

        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(method -> new ConfigurationBeanSpecification(instance, method))
                .collect(Collectors.toSet());
    }
}
