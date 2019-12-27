package nextstep.di.factory.scanner;

import nextstep.di.factory.definition.BeanDefinition;

import java.util.Set;

public interface BeanScanner {
    Set<BeanDefinition> scan();
}
