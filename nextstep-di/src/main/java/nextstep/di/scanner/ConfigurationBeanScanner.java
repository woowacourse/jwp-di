package nextstep.di.scanner;

import com.google.common.collect.Maps;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

public class ConfigurationBeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ClasspathBeanScanner.class);

    private BeanFactory beanFactory;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void register(Class<?> clazz) {
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

        beanFactory.registerBean(configs);
    }
}
