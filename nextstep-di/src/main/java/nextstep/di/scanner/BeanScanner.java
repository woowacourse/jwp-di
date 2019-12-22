package nextstep.di.scanner;

import nextstep.di.definition.BeanDefinition;

import java.util.Set;

public interface BeanScanner {
    Set<BeanDefinition> scan();
}
