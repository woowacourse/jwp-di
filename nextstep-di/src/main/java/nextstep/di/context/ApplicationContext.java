package nextstep.di.context;

import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {

    private BeanFactory beanFactory;
    private ConfigurationBeanScanner cbs;
    private ClasspathBeanScanner cbds;
    private Set<Object> basePackages;

    public ApplicationContext() {
        this.beanFactory = new BeanFactory();
        this.basePackages = Sets.newHashSet();
        this.cbs = new ConfigurationBeanScanner(beanFactory);
        this.cbds = new ClasspathBeanScanner(beanFactory);
    }

    public Map<Class<?>, Object> getAnnotatedWith(Class<? extends Annotation> annotation) {
        return beanFactory.getAnnotatedWith(annotation);
    }

    public void configurations(Class<?> configurationClass) {
        cbs.register(configurationClass);
        basePackages.addAll(getScanTargets(configurationClass));
    }

    private Set<Object> getScanTargets(Class<?> clazz) {
        Set<Object> scanTargets = Sets.newHashSet();
        ComponentScan annotation = clazz.getAnnotation(ComponentScan.class);
        scanTargets.addAll(Collections.singleton(annotation.value()));
        return scanTargets;
    }

    public void initialize() {
        basePackages.forEach(basePackage -> {
            cbs.doScan(basePackage);
            cbds.doScan(basePackage);
        });
        this.beanFactory.initialize();
    }

    public void initialize(Object... basePackage) {
        cbs.doScan(basePackage);
        cbds.doScan(basePackage);
        this.beanFactory.initialize();
    }

    public <T> T getBean(Class<T> beanClass) {
        return beanFactory.getBean(beanClass);
    }
}
