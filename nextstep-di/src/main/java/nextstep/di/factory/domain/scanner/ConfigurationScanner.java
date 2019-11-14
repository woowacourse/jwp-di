package nextstep.di.factory.domain.scanner;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.domain.BeanFactory;
import nextstep.di.factory.domain.beandefinition.BeanDefinition;
import nextstep.di.factory.domain.beandefinition.ConfigurationBeanDefinition;
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
                    logger.debug("add BeanDefinition {} of Method {}", beanDefinition, method);
                }
            }
            return;
        }
        throw new IllegalArgumentException("클래스에 Configuration 어노테이션이 없습니다");
    }
}
