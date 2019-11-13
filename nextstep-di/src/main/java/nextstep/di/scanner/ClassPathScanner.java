package nextstep.di.scanner;

import nextstep.annotation.ComponentScan;

public class ClassPathScanner {
    private static final Class<ComponentScan> SCAN_CLASS = ComponentScan.class;

    private Class<?> configClass;

    public ClassPathScanner(Class<?> configClass) {
        checkScanClass(configClass);
        this.configClass = configClass;
    }

    private void checkScanClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(ComponentScan.class)) {
            throw new RuntimeException("ComponentScan annotation does not exist!");
        }
    }

    public String[] getBasePackages() {
        ComponentScan annotation = configClass.getAnnotation(SCAN_CLASS);
        return annotation.basePackages();
    }
}
