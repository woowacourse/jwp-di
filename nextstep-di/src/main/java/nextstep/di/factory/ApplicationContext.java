package nextstep.di.factory;

import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
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
        ComponentScan componentScan = configurationClass.getAnnotation(ComponentScan.class);
        String[] path = componentScan.basePackages();
        checkValidPath(path);
        this.path = path;

        this.beanScanners = Arrays.asList(
                new ClasspathBeanScanner(Arrays.asList(Controller.class, Service.class, Repository.class), this.path),
                new ConfigurationBeanScanner(Collections.singletonList(Configuration.class), this.path));
    }

    private void checkValidPath(String[] path) {
        if (path == null) {
            log.error("compinent Scan을 찾을 수 없습니다.");
            throw new NotFoundComponentScanException();
        }
    }

    public Map<Class<?>, BeanDefinition> scanBeans() {
        Map<Class<?>, BeanDefinition> preInitiateBeans = new HashMap<>();
        for (BeanScanner beanScanner : beanScanners) {
            preInitiateBeans.putAll(beanScanner.scanBeans());
        }

        return preInitiateBeans;
    }
}
