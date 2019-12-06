package nextstep.di.factory;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

public class BeanDefinition {
    private final Set<Class<?>> preInstantiateComponents;
    private final Set<Class<?>> preInstantiateConfigurationBeans;

    public BeanDefinition(final Set<Class<?>> preInstantiateComponents, final Set<Class<?>> preInstantiateConfigurationBeans) {
        this.preInstantiateComponents = preInstantiateComponents;
        this.preInstantiateConfigurationBeans = preInstantiateConfigurationBeans;
    }

    public Set<Class<?>> getPreInstantiateComponents() {
        return Collections.unmodifiableSet(preInstantiateComponents);
    }

    public Set<Class<?>> getPreInstantiateConfigurationBeans() {
        return Collections.unmodifiableSet(preInstantiateConfigurationBeans);
    }
}
