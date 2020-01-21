package nextstep.di.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class IntegrationBeanScanner {
    private static final Logger log = LoggerFactory.getLogger(IntegrationBeanScanner.class);

    private final List<BeanScanner> beanScanners;

    public IntegrationBeanScanner(BeanScanner... beanScanners) {
        this.beanScanners = Arrays.asList(beanScanners);
    }

    public void scanBeans(List<String> basePackages) {
        this.beanScanners.forEach(beanScanner -> beanScanner.scanBeans(basePackages));
        log.info("Scan all beans!");
    }
}
