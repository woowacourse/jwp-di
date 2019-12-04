package nextstep.di.factory;

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
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

public class ConfigurationBeanScanner {
    private BeanFactory beanFactory;

    private Reflections reflections;

    private List<String> basePackages = Lists.newArrayList("");

    Set<Class<?>> configClasses = Sets.newHashSet();

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        reflections = new Reflections(basePackages);
        findConfigClasses(Configuration.class);
    }

    public void scanConfigurationBeans() {
        appendBasePackage();
        scan();
    }

    private void findConfigClasses(Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            configClasses.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
    }

    private Set<Method> scan() {
        Set<Method> methods = Sets.newHashSet();
        for (Class clazz : configClasses) {
            methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(Bean.class));
            beanFactory.appendPreInstantiatedMethodsOfBean(methods);
        }
        return methods;
    }

    private void appendBasePackage() {
        for (Class<?> clazz : configClasses) {
            ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
            basePackages.addAll(Arrays.asList(componentScan.basePackages()));
            basePackages.addAll(Arrays.asList(componentScan.value()));
        }
    }
}
