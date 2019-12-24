package nextstep.di.factory;

import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

import java.util.*;

public class ApplicationContext {
    private List<BeanScanner> beanScanners;
    private String[] path;

    public ApplicationContext(Class<?> configurationClass) {
        ComponentScan componentScan = configurationClass.getAnnotation(ComponentScan.class);
        this.path = componentScan.basePackages();
        this.beanScanners = Arrays.asList(
                new ClasspathBeanScanner(Arrays.asList(Controller.class, Service.class, Repository.class), path),
                new ConfigurationBeanScanner(Collections.singletonList(Configuration.class), path));
    }

    public Map<Class<?>, BeanDefinition> scanBeans() {
        Map<Class<?>, BeanDefinition> preInitiateBeans = new HashMap<>();
        for (BeanScanner beanScanner : beanScanners) {
            preInitiateBeans.putAll(beanScanner.scanBeans());
        }

        return preInitiateBeans;
    }
}
