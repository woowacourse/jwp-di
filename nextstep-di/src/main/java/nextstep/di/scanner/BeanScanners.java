package nextstep.di.scanner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import nextstep.di.BeanInitiator;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BeanScanners {
    private final List<BeanScanner> beanScanners = Lists.newArrayList();

    public BeanScanners(BeanScanner... beanScanners) {
        this.beanScanners.addAll(Arrays.asList(beanScanners));
    }

    public Set<Class<?>> getPreInstantiatedTypes() {
        Set<Class<?>> classes = Sets.newHashSet();
        for (BeanScanner beanScanner : beanScanners) {
            classes.addAll(beanScanner.getInstantiatedTypes());
        }
        return classes;
    }

    public BeanInitiator getBeanInitiator(Class<?> type) {
        return beanScanners.stream().filter(beanScanner -> beanScanner.isContainsBean(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 타입입니다."))
                .getBeanInitiator(type);
    }
}
