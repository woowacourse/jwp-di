package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;

import java.util.Set;

public interface Scanner {
    Set<BeanDefinition> scan();
}
