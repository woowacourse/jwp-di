package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

public class ConfigurationBeanScanner implements BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanScanner.class);

    private BeanFactory beanFactory;
    private Set<Class<?>> configClasses = Sets.newHashSet();

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void scanBeans(Object... basePackage) {
        checkEmptyBasePackage(basePackage);

        Reflections reflections = new Reflections(basePackage);
        configClasses = reflections.getTypesAnnotatedWith(Configuration.class);

        configClasses.stream()
                .forEach(clazz
                        -> beanFactory.appendPreInstantiatedMethodsOfBean(findAllMethodsWithAnnotation(clazz, Bean.class)));
        log.info("Scan Beans : {}", this.getClass().getName());
    }

    private Set<Method> findAllMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        return ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(annotation));
    }
}
