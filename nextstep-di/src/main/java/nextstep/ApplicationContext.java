package nextstep;

import java.util.List;

import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;

public class ApplicationContext {
    public static BeanFactory initializeBeans() {
        BeanFactory beanFactory = new BeanFactory();

        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(beanFactory);
        configurationBeanScanner.scanConfigurationBeans();

        List<String> basePackages = configurationBeanScanner.appendBasePackage();

        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        classpathBeanScanner.scanBeans(basePackages);

        beanFactory.initialize();
        return beanFactory;
    }
}
