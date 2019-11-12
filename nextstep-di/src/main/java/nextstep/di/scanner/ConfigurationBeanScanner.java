package nextstep.di.scanner;

import nextstep.annotation.Configuration;

import java.util.Set;

public class ConfigurationBeanScanner implements BeanScanner {

    private static final Class[] COMPONENT_ANNOTATION = {Configuration.class};

    private final Set<Class<?>> classTypes;

    // TODO: 2019/11/13 개선 방안.. 현재 역할이 불분명
    public ConfigurationBeanScanner(Object... basePackages) {
        this.classTypes = getTypesAnnotatedWith(basePackages, COMPONENT_ANNOTATION);
    }

    @Override
    public Set<Class<?>> getClassTypes() {
        return classTypes;
    }
}