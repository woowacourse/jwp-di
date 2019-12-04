package nextstep.di.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

public class ConfigurationBeanScanner implements BeanScanner {
    private BeanFactory beanFactory;

    private Reflections reflections;

    private List<String> basePackages = Lists.newArrayList("");

    Set<Class<?>> configClasses = Sets.newHashSet();

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        reflections = new Reflections(basePackages);
        configClasses.addAll(getTypesAnnotatedWith(Configuration.class));
    }

    @Override
    public void scanBeans(Object... basePackage) {
        for (Class clazz : configClasses) {
            Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(Bean.class));
            beanFactory.appendPreInstantiatedMethodsOfBean(methods);
        }
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

    public void scanConfigurationBeans() {
        for (Class clazz : configClasses) {
            Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(Bean.class));
            beanFactory.appendPreInstantiatedMethodsOfBean(methods);
        }
    }

    public List<String> appendBasePackage() {
        for (Class<?> clazz : configClasses) {
            ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
            basePackages.addAll(Arrays.asList(componentScan.basePackages()));
            basePackages.addAll(Arrays.asList(componentScan.value()));
        }
        return basePackages;
    }
}
