package nextstep.di.factory.instantiation;

import nextstep.di.factory.BeanCreateMatcher;

public interface InstantiationMethod {
    Object getInstance(BeanCreateMatcher beanCreateMatcher);
}
