package nextstep.di.factory.scanner;

import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.bean.BeanDefinition;
import nextstep.di.factory.bean.MethodBeanDefinition;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurationBeanScanner implements Scanner {
    private static final Class<Bean> METHOD_BEAN_TARGET_ANNOTATION = Bean.class;
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanScanner.class);
    private static final Class[] AVAILABLE_ANNOTATIONS = {Configuration.class};
    private Set<BeanDefinition> beanDefinitions = new HashSet<>();
    private Reflections reflections;

    public ConfigurationBeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public void resister(Class<?>... configurationClasses) {
        beanDefinitions.addAll(Collections.unmodifiableSet(createBeanDefinitions(Set.of(configurationClasses))));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<BeanDefinition> scan() {
        beanDefinitions.addAll(getTypesAnnotatedWith(AVAILABLE_ANNOTATIONS));
        return beanDefinitions;
    }

    @SuppressWarnings("unchecked")
    private Set<BeanDefinition> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> configurationClasses = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            configurationClasses.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return createBeanDefinitions(configurationClasses);
    }

    private Set<BeanDefinition> createBeanDefinitions(Set<Class<?>> configurationClasses) {
        return configurationClasses.stream()
                .map(this::createEachClassDefinitions)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<BeanDefinition> createEachClassDefinitions(Class<?> configurationClass) {
        return Arrays.stream(configurationClass.getMethods())
                .flatMap(Stream::of)
                .filter(method -> method.isAnnotationPresent(METHOD_BEAN_TARGET_ANNOTATION))
                .map(MethodBeanDefinition::new)
                .collect(Collectors.toSet());
    }
}
