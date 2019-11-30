package nextstep.di.scanner;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactoryImpl;
import nextstep.di.initiator.ConfigurationBeanInitiator;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;

public class ConfigurationBeanScanner extends BeanScanner {

    public ConfigurationBeanScanner(BeanFactoryImpl beanFactoryImpl, Object... basePackage) {
        super(basePackage);
        initialize(beanFactoryImpl);
    }

    public void initialize(BeanFactoryImpl beanFactoryImpl) {
        for (Class<?> clazz : super.scanAnnotatedWith(Configuration.class)) {
            register(clazz, beanFactoryImpl);
        }
    }

    public void register(Class<?> clazz, BeanFactoryImpl beanFactoryImpl) {
        Object instance = BeanUtils.instantiateClass(clazz);
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> beanFactoryImpl.addBeanInitiator(method.getReturnType(),
                        new ConfigurationBeanInitiator(method, instance))
                );
    }
}
