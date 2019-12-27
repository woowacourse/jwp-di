package nextstep.di.factory;

import nextstep.annotation.Configuration;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class ConfigurationBeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanScanner.class);

    private static final Class CONFIGURATION_ANNOTATION = Configuration.class;


    private Reflections reflections;

    public ConfigurationBeanScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> scanBeans() {
        Set<Class<?>> preInitiatedBeans = new HashSet<>();

        for (Object o : reflections.getTypesAnnotatedWith(CONFIGURATION_ANNOTATION)) {
            Class<?> clazz = (Class) o;
            preInitiatedBeans.add(clazz);
        }

        return preInitiatedBeans;
    }
}