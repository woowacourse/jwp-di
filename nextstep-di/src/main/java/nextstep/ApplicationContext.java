package nextstep;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;
import nextstep.exception.EmptyBasePackagesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationContext {
    private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);

    private Class<?>[] configClasses;
    private List<String> basePackages;

    public ApplicationContext(Class<?>... configClasses) {
        this.configClasses = configClasses;
    }

    public BeanFactory initializeBeans() {
        basePackages = findBasePackages(configClasses);

        checkEmptyBasePackage();

        BeanFactory beanFactory = new BeanFactory();

        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(beanFactory);
        configurationBeanScanner.scanBeans(basePackages);

        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        classpathBeanScanner.scanBeans(basePackages);

        beanFactory.initialize();

        log.info("Initialized Beans!");
        return beanFactory;
    }

    private void checkEmptyBasePackage() {
        if (basePackages.size() == 0) {
            log.debug("Empty Base Package");
            throw new EmptyBasePackagesException();
        }
    }

    private List<String> findBasePackages(Class<?>[] configClasses) {
        List<String> basePackages = new ArrayList<>();

        Arrays.stream(configClasses)
                .forEach(configClazz -> basePackages.addAll(findBasePackage(configClazz)));

        log.info("Find Base Package");
        return basePackages;
    }

    private List<String> findBasePackage(Class<?> clazz) {
        ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);

        if (componentScan.value().length == 0) {
            return Arrays.asList(componentScan.basePackages());
        }
        return Arrays.asList(componentScan.value());
    }
}
