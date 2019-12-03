package nextstep.di.context;

import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.BeanScanner;
import nextstep.di.scanner.ClassPathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;

import java.util.List;

public class DiApplicationContext implements ApplicationContext {

    private final BeanFactory beanFactory = new BeanFactory();

    @Override
    public BeanFactory initializeBeanFactory() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner();
        scanAndRegisterBeans(configurationBeanScanner);

        List<String> componentScanPackages = configurationBeanScanner.findPackagesInComponentScan();
        ClassPathBeanScanner classPathBeanScanner = new ClassPathBeanScanner();
        scanAndRegisterBeans(classPathBeanScanner, componentScanPackages);

        beanFactory.initialize();

        return beanFactory;
    }

    private void scanAndRegisterBeans(BeanScanner beanScanner, Object... basePackage) {
        beanScanner.scan(basePackage);
        beanScanner.registerBeans(beanFactory);
    }
}
