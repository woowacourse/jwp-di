package nextstep.di.scanner;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.DefaultBeanDefinition;
import nextstep.di.bean.MethodBeanDefinition;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ConfigurationBeanScanner implements BeanScanner {
    private static final Class[] COMPONENT_ANNOTATIONS = {Configuration.class};

    private final List<BeanDefinition> beanDefinitions;

    public ConfigurationBeanScanner(Object... basePackages) {
        Set<Class<?>> classTypes = getTypesAnnotatedWith(basePackages, COMPONENT_ANNOTATIONS);
        this.beanDefinitions = initBeanDefinitions(classTypes);
    }

    private List<BeanDefinition> initBeanDefinitions(final Set<Class<?>> classTypes) {
        List<BeanDefinition> list = new ArrayList<>();
        classTypes.forEach(classType -> {
            list.add(new DefaultBeanDefinition(classType));
            initMethodBeanDefinitions(list, classType);
        });
        return list;
    }

    private void initMethodBeanDefinitions(final List<BeanDefinition> list, final Class<?> classType) {
        try {
            Object instance = classType.getDeclaredConstructor().newInstance();
            Stream.of(classType.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Bean.class))
                    .forEach(method -> list.add(new MethodBeanDefinition(instance, method.getReturnType(), method)));

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }
}