package nextstep.di.scanner;

import com.google.common.collect.Maps;
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
import java.util.Map;
import java.util.Set;

public class ConfigurationBeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanScanner.class);

    private BeanFactory beanFactory;
    private Reflections reflections;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @SuppressWarnings("unchecked")
    public void register(Class<?> configClazz) {
        if (!configClazz.isAnnotationPresent(Configuration.class)) {
            throw new ClassNotConfigurationException();
        }

        Map<Class<?>, Method> configMethods = Maps.newHashMap();

        Set<Method> methods = ReflectionUtils.getAllMethods(configClazz, ReflectionUtils.withAnnotation(Bean.class));

        for (Method method : methods) {
            configMethods.put(method.getReturnType(), method);
        }

        beanFactory.register(configMethods);
    }

    @SuppressWarnings("unchecked")
    public Set<Class<?>> getBeans() {
        return getTypesAnnotateWith(Configuration.class);
    }

    @SuppressWarnings("unchecked")
    public Set<Class<?>> getTypesAnnotateWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> annotationBeans = Sets.newHashSet();

        for (Class<? extends Annotation> annotation : annotations) {
            annotationBeans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        log.debug("Scan Beans Type : {}", annotationBeans);
        return annotationBeans;
    }
}
