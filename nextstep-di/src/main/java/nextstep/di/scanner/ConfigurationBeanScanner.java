package nextstep.di.scanner;

import com.google.common.collect.Maps;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.initiator.BeanInitiator;
import nextstep.di.initiator.ConfigurationBeanInitiator;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ConfigurationBeanScanner extends BeanScannerImpl {
    private Map<Class<?>, ConfigurationBeanInitiator> configurationBeans;

    public ConfigurationBeanScanner(Object... basePackage) {
        super(basePackage);
        configurationBeans = Maps.newHashMap();
        initialize();
    }

    public void initialize() {
        for (Class<?> clazz : super.scanAnnotatedWith(Configuration.class)) {
            register(clazz);
        }
    }

    public void register(Class<?> clazz) {
        Object instance = BeanUtils.instantiateClass(clazz);
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> configurationBeans.put(method.getReturnType(),
                        new ConfigurationBeanInitiator(method, instance))
                );
    }

    @Override
    public Set<Class<?>> getInstantiatedTypes() {
        return configurationBeans.keySet();
    }

    @Override
    public boolean isContainsBean(Class<?> clazz) {
        return configurationBeans.containsKey(clazz);
    }

    @Override
    public BeanInitiator getBeanInitiator(Class<?> clazz) {
        return configurationBeans.get(clazz);
    }
}
