package nextstep.context;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;
import nextstep.di.scanner.IntegrationBeanScanner;
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

        IntegrationBeanScanner integrationBeanScanner = new IntegrationBeanScanner(
                new ConfigurationBeanScanner(beanFactory), new ClasspathBeanScanner(beanFactory));

        integrationBeanScanner.scanBeans(basePackages);
        beanFactory.initialize();

        log.info("Initialized beans!");
        return beanFactory;
    }

    private void checkEmptyBasePackage() {
        if (basePackages.size() == 0) {
            log.debug("Empty base package");
            throw new EmptyBasePackagesException();
        }
    }

    private List<String> findBasePackages(Class<?>[] configClasses) {
        List<String> basePackages = new ArrayList<>();

        Arrays.stream(configClasses)
                .forEach(configClazz -> basePackages.addAll(findBasePackage(configClazz)));

        log.info("Find base package");
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
