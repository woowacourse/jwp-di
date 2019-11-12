package nextstep.di.scanner;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
import nextstep.di.initiator.ConfigurationBeanInitiator;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;

public class ConfigurationBeanScanner extends BeanScanner {

    public ConfigurationBeanScanner(BeanFactory beanFactory, Object... basePackage) {
        super(basePackage);
        initialize(beanFactory);
    }

    public void initialize(BeanFactory beanFactory) {
        for (Class<?> clazz : super.scanAnnotatedWith(Configuration.class)) {
            register(clazz, beanFactory);
        }
    }

    public void register(Class<?> clazz, BeanFactory beanFactory) {
        Object instance = BeanUtils.instantiateClass(clazz);
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> beanFactory.addBeanInitiator(method.getReturnType(),
                        new ConfigurationBeanInitiator(method, instance))
                );
    }
}
