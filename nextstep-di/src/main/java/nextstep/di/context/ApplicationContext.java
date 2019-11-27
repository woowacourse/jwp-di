package nextstep.di.context;

import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {

    private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);

    private BeanFactory beanFactory;
    private ConfigurationBeanScanner cbs;
    private ClasspathBeanScanner cbds;
    private Set<Object> basePackages;

    private ApplicationContext() {
        this.beanFactory = new BeanFactory();
        this.basePackages = Sets.newHashSet();
        this.cbs = new ConfigurationBeanScanner(beanFactory);
        this.cbds = new ClasspathBeanScanner(beanFactory);
    }

    public ApplicationContext(Object... basePackage) {
        this();
        cbs.doScan(basePackage);
        contextConfigurations();
    }

    public Map<Class<?>, Object> getAnnotatedWith(Class<? extends Annotation> annotation) {
        return beanFactory.getAnnotatedWith(annotation);
    }

    public void initialize() {
        basePackages.forEach(basePackage -> {
            log.debug("now scanning basePackage : {}", basePackage);
            cbs.doScan(basePackage);
            cbds.doScan(basePackage);
        });
        beanFactory.initialize();
    }

    public void initialize(Object... basePackage) {
        cbs.doScan(basePackage);
        cbds.doScan(basePackage);
        beanFactory.initialize();
    }

    public <T> T getBean(Class<T> beanClass) {
        return beanFactory.getBean(beanClass);
    }

    private void contextConfigurations() {
        Map<Class<?>, Method> configs = this.cbs.getBeans();
        configs.values().forEach(config -> {
            Class<?> configClass = config.getDeclaringClass();
            log.debug("this is configuration class : {}", configClass);
            configurations(configClass);
        });
    }

    private void configurations(Class<?> configurationClass) {
        cbs.register(configurationClass);
        basePackages.addAll(getScanTargets(configurationClass));
    }

    private Set<Object> getScanTargets(Class<?> clazz) {
        Set<Object> scanTargets = Sets.newHashSet();
        ComponentScan annotation = clazz.getAnnotation(ComponentScan.class);
        scanTargets.addAll(Collections.singleton(annotation.value()));
        return scanTargets;
    }
}
