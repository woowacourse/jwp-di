package nextstep.di.factory.domain;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;

import java.lang.reflect.Method;

public class ConfigurationScanner {
    private BeanFactory beanFactory;

    public ConfigurationScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void register(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Configuration.class)) {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(Bean.class)) {
                    BeanDefinition beanDefinition = new ConfigurationBean(method);
                    beanFactory.addBeanDefinition(beanDefinition.getBeanType(), beanDefinition);
                }
            }
            return;
        }
        throw new IllegalArgumentException("클래스에 Configuration 어노테이션이 없습니다");
    }
}
