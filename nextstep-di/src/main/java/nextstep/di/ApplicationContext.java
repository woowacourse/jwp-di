package nextstep.di;

import com.google.common.collect.Lists;
import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.BeanScanner;

import java.util.Arrays;
import java.util.List;

public class ApplicationContext {
    private BeanFactory beanFactory;
    private List<BeanScanner> beanScanners = Lists.newArrayList();

    public ApplicationContext(Class<?>... configurations) {
        List<Object> basePackage = Lists.newArrayList();
        for (Class<?> configuration : configurations) {
            basePackage.addAll(Arrays.asList(configuration.getAnnotation(ComponentScan.class).value()));
        }
    }

    public void addBeanScanner(BeanScanner beanScanner) {
        beanScanners.add(beanScanner);
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
