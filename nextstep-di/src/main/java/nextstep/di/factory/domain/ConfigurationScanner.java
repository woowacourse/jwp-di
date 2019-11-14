package nextstep.di.factory.domain;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ConfigurationScanner {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationScanner.class);
    private BeanFactory beanFactory;

    public ConfigurationScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void register(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Configuration.class)) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Bean.class)) {
                    BeanDefinition beanDefinition = new ConfigurationBeanDefinition(method);
                    beanFactory.addBeanDefinition(beanDefinition.getBeanType(), beanDefinition);
                }
            }
            return;
        }
        throw new IllegalArgumentException("클래스에 Configuration 어노테이션이 없습니다");
    }
}
