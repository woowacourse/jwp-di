package nextstep.di.factory;

import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.di.factory.exception.NotFoundComponentScanException;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ApplicationContext {
    private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);

    private List<BeanScanner> beanScanners;
    private String[] path;

    public ApplicationContext(Class<?> configurationClass) {
        this.path = findPath(configurationClass);

        this.beanScanners = Arrays.asList(
                new ClasspathBeanScanner(Arrays.asList(Controller.class, Service.class, Repository.class), this.path),
                new ConfigurationBeanScanner(Collections.singletonList(Configuration.class), this.path));
    }

    private String[] findPath(Class<?> configurationClass) {
        ComponentScan componentScan = configurationClass.getAnnotation(ComponentScan.class);
        String[] path = componentScan.basePackages();
        checkValidPath(path);
        return path;
    }

    private void checkValidPath(String[] path) {
        if (path == null) {
            log.error("component Scan을 찾을 수 없습니다.");
            throw new NotFoundComponentScanException();
        }
    }

    public Map<Class<?>, BeanDefinition> scanBeans() {
        Map<Class<?>, BeanDefinition> preInitiateBeans = new HashMap<>();

        for (BeanScanner beanScanner : beanScanners) {
            Map<Class<?>, BeanDefinition> classBeanDefinitionMap = beanScanner.scanBeans();
            preInitiateBeans.putAll(classBeanDefinitionMap);
        }

        return preInitiateBeans;
    }
}
