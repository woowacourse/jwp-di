package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.Configuration;
import org.reflections.Reflections;

import java.util.Set;

public class ConfigurationBeanScanner implements BeanScanner {
    private Reflections reflections;
    private Set<Class<?>> configurations = Sets.newHashSet();

    public ConfigurationBeanScanner(Class<?> initPoint) {
        this.reflections = new Reflections(initPoint.getPackageName());
    }

    @Override
    public Set<Class<?>> scan() {
        configurations.addAll(reflections.getTypesAnnotatedWith(Configuration.class));
        return configurations;
    }
}
