package nextstep.di.bean;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {
    private final ConfigBeanScanner configBeanScanner;
    private final ClasspathBeanScanner classpathBeanScanner;

    public ApplicationContext(Class<?> config) {
        this.configBeanScanner = new ConfigBeanScanner(config);
        this.classpathBeanScanner = new ClasspathBeanScanner(this.configBeanScanner.getBasePackages());
    }

    public Map<Class<?>, Method> getConfigBeansToInstantiate() {
        return this.configBeanScanner.getConfigBeansToInstantiate();
    }

    public Set<Class<?>> getClasspathBeansToInstantiate() {
        return this.classpathBeanScanner.getClasspathBeansToInstantiate();
    }
}