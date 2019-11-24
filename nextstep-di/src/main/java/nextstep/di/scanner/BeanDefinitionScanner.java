package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;

import java.util.Set;

public interface BeanDefinitionScanner {
    Set<BeanDefinition> scan();
}
