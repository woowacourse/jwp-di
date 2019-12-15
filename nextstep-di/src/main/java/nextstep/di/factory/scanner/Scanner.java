package nextstep.di.factory.scanner;

import nextstep.di.factory.bean.BeanDefinition;

import java.util.Set;

public interface Scanner {
    Set<BeanDefinition> scan();
}
