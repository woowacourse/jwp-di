package nextstep.di.scanner;

import nextstep.annotation.Configuration;

import java.util.Set;

public class ConfigurationBeanScanner extends BeanScanner {
    private final Set<Class<?>> beans;

    @SuppressWarnings("unchecked")
    public ConfigurationBeanScanner(final Object... basePackage) {
        super(basePackage);
        beans = getTypesAnnotatedWith(Configuration.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Class<?>> getBeans() {
        return beans;
    }
}
