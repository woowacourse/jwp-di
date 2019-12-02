package nextstep.di.context;

import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClassPathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;

import java.util.List;

public class DiApplicationContext implements ApplicationContext {

    @Override
    public BeanFactory initializeBeanFactory() {
        BeanFactory beanFactory = new BeanFactory();

        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(beanFactory);
        configurationBeanScanner.scan();

        List<String> componentScanPackages = configurationBeanScanner.findPackagesInComponentScan();
        ClassPathBeanScanner classPathBeanScanner = new ClassPathBeanScanner(beanFactory);
        classPathBeanScanner.scan(componentScanPackages);

        beanFactory.initialize();

        return beanFactory;
    }
}
