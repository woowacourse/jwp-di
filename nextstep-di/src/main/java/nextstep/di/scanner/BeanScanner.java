package nextstep.di.scanner;

import nextstep.di.BeanDefinition;

import java.util.Set;

public interface BeanScanner {
    Set<BeanDefinition> doScan();
}
