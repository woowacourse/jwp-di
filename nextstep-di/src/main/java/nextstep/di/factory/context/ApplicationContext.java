package nextstep.di.factory.context;

import com.google.common.collect.Lists;
import nextstep.annotation.ComponentScan;
import nextstep.di.factory.beans.BeanScanner;
import nextstep.di.factory.beans.ComponentBeanScanner;
import nextstep.di.factory.beans.ConfigurationBeanScanner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationContext {
    private BeanFactory beanFactory;
    private List<BeanScanner> beanScanners = Lists.newArrayList();

    public ApplicationContext(Class<?> entryPoint) {
        beanFactory = new BeanFactory();
        beanFactory.addScanner(new ConfigurationBeanScanner(entryPoint));
        ComponentScan componentScan = entryPoint.getAnnotation(ComponentScan.class);
        this.beanScanners = Arrays.stream(componentScan.basePackages())
                .map(ComponentBeanScanner::new)
                .collect(Collectors.toList());
    }

    public void addScanner(BeanScanner beanScanner) {
        this.beanScanners.add(beanScanner);
    }

    public void initialize() {
        for (BeanScanner beanScanner : beanScanners) {
            beanFactory.addScanner(beanScanner);
        }
        beanFactory.initialize();
    }

    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return beanFactory.getBeansWithAnnotation(annotation);
    }

    public <T> T getBean(Class<T> type) {
        return beanFactory.getBean(type);
    }
}
