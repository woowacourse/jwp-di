package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.definition.BeanDefinition;
import nextstep.di.factory.definition.BeanDefinitionConstructor;
import nextstep.di.factory.definition.BeanDefinitionMethod;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static nextstep.di.factory.BeanFactoryUtils.getInjectedConstructor;

public class BeanScanner {
    private static final int DEFAULT_CONSTRUCTOR_PARAMETER_NUMBER = 0;
    private Reflections reflections;

    public BeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, BeanDefinition> getPreInstanticateBeanClasses() {
        Map<Class<?>, BeanDefinition> beanDefinitions = Maps.newHashMap();
        Set<Class<?>> classes = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class, Configuration.class);

        beanDefinitions.putAll(getAllBeanDefinitionConstructorsFrom(classes));
        beanDefinitions.putAll(getAllBeanDefinitionMethodsFrom(getConfigurationClasses(classes)));

        return beanDefinitions;
    }

    private Map<? extends Class<?>, BeanDefinitionMethod> getAllBeanDefinitionMethodsFrom(Set<Class<?>> classes) {
        return classes.stream()
                .map(Class::getMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toMap(Method::getReturnType, BeanDefinitionMethod::new));
    }

    private Map<? extends Class<?>, BeanDefinitionConstructor> getAllBeanDefinitionConstructorsFrom(Set<Class<?>> classes) {
        return classes.stream()
                .collect(Collectors.toMap(aClass -> aClass, aClass -> new BeanDefinitionConstructor(getConstructor(aClass))));
    }

    private Set<Class<?>> getConfigurationClasses(final Set<Class<?>> classes) {
        return classes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toSet());
    }


    private Constructor<?> getConstructor(Class<?> concreteClass) {
        Constructor<?> constructor = getInjectedConstructor(concreteClass);

        if (Objects.isNull(constructor)) {
            constructor = getDefaultConstructor(concreteClass);
        }

        return constructor;
    }


    private Constructor<?> getDefaultConstructor(Class<?> concreteClass) {
        return Arrays.stream(concreteClass.getConstructors())
                .filter(this::isDefaultConstructor)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("기본 생성자가 존재하지 않습니다."));
    }

    private boolean isDefaultConstructor(Constructor<?> beanConstructor) {
        return beanConstructor.getParameterCount() == DEFAULT_CONSTRUCTOR_PARAMETER_NUMBER;
    }

    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        return beans;
    }
}
