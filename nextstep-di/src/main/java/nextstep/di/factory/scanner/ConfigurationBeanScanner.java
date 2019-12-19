package nextstep.di.factory.scanner;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.beandefinition.ConfigurationBeanDefinition;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner implements BeanScanner {

    private BeanFactory beanFactory;
    private Reflections reflections;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ConfigurationBeanScanner(Set<String> basePackages) {
        reflections = new Reflections(basePackages);
    }

    public void register(Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .findAny()
                .ifPresent(method -> beanFactory.addPreInvokedMethod(method));
    }

    @Override
    public Set<BeanDefinition> scan() {
        Set<Method> beanMethods = getBeanMethods();

        return beanMethods.stream()
                .map(ConfigurationBeanDefinition::new)
                .collect(Collectors.toSet());
    }

    private Set<Method> getBeanMethods() {
        Set<Method> beanMethods = new HashSet<>();
        for (Class<?> aClass : reflections.getTypesAnnotatedWith(Configuration.class)) {
            for (Method declaredMethod : aClass.getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(Bean.class)) {
                    beanMethods.add(declaredMethod);
                }
            }
        }
        return beanMethods;
    }
}
