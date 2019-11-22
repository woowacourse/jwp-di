package nextstep.di.scanner;

import com.google.common.collect.Maps;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.ConfigurationBean;
import nextstep.di.factory.BeanFactory;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class ConfigurationBeanScanner implements Scanner {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanScanner.class);

    private BeanFactory beanFactory;
    private Reflections reflections;
    private Map<Class<?>, BeanDefinition> beans;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.beans = Maps.newHashMap();
    }

    public void registerPackage(Object... basePackage) {
        reflections = new Reflections(basePackage);
        initializeDefinitions();
    }

    public void registerBeanInfo() {
        beanFactory.register(beans);
    }

    @SuppressWarnings("unchecked")
    public Set<Class<?>> getClazz() {
        return ScannerUtils.getTypesAnnotateWith(reflections, Configuration.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initializeDefinitions() {
        Set<Class<?>> typesAnnotatedWith = ScannerUtils.getTypesAnnotateWith(reflections, Configuration.class);

        for (Class<?> clazz : typesAnnotatedWith) {
            Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(Bean.class));
            putBeanInfo(clazz, methods);
        }

        log.debug("configuration bean : {}", beans);
    }

    private void putBeanInfo(Class<?> clazz, Set<Method> methods) {
        for (Method method : methods) {
            try {
                ConfigurationBean configurationBean = new ConfigurationBean(clazz.newInstance(), method);
                beans.put(method.getReturnType(), configurationBean);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
