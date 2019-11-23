package nextstep.di.scanner;

import com.google.common.collect.Maps;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class ConfigurationBeanScanner extends AbstractBeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ClasspathBeanScanner.class);
    private static final Class[] COMPONENT_SCAN = {Configuration.class};

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public void register(Class<?> clazz) {
        beanFactory.registerConfigBean(findBeanMethods(clazz));
    }

    @Override
    public void doScan(Object... basePackage) {
        reflections = new Reflections(basePackage);
        beanFactory.registerConfigBean(getBeans());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<Class<?>, Method> getBeans() {
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
}
