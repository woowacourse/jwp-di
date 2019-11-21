package nextstep.di.scanner;

import nextstep.annotation.Configuration;

public class ConfigurationBeanScanner extends ComponentAnnotationScanner {

    private static final Class[] COMPONENT_ANNOTATION = {Configuration.class};

    public ConfigurationBeanScanner(Object... basePackages) {
        super(COMPONENT_ANNOTATION, basePackages);
    }
}
