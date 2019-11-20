package nextstep.di.scanner;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class ConfigurationBeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ClasspathBeanScanner.class);
    private static final Class[] COMPONENT_SCAN = {ComponentScan.class};

    private Reflections reflections;
    private BeanFactory beanFactory;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void register(Class<?> clazz) {
        beanFactory.registerBean(findBeanMethods(clazz));
    }

    public void doScan(Object... basePackage) {
        reflections = new Reflections(basePackage);
        beanFactory.registerBean(getComponentScan());
    }

    @SuppressWarnings("unchecked")
    private Map<Class<?>, Method> getComponentScan() {
        Set<Class<?>> typesAnnotatedWith = getTypesAnnotatedWith(COMPONENT_SCAN);
        Map<Class<?>, Method> configs = Maps.newHashMap();
        for (Class<?> annotatedClass : typesAnnotatedWith) {
            configs.putAll(findBeanMethods(annotatedClass));
        }
        return configs;
    }

    private Map<Class<?>, Method> findBeanMethods(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Configuration.class)) {
            throw new ClassNotConfigurationException();
        }

        Map<Class<?>, Method> configs = Maps.newHashMap();

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Bean.class)) {
                Class<?> returnType = method.getReturnType();
                configs.put(returnType, method);
                log.debug("return type : {}, method : {}", returnType, configs.get(returnType));
            }
        }

        return configs;
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
