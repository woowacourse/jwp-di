package nextstep.di.factory.instantiation;

import nextstep.di.factory.BeanCreateMatcher;

import java.util.Map;
import java.util.Optional;

public interface InstantiationMethod {
    Object getInstance(BeanCreateMatcher beanCreateMatcher, Map<Class<?>, Object> beans);

    default Object getBean(BeanCreateMatcher beanCreateMatcher, Map<Class<?>, Object> beans, Class<?> parameterType) {
        return Optional.ofNullable(beans.putIfAbsent(parameterType,
                beanCreateMatcher.get(parameterType).getInstance(beanCreateMatcher, beans)))
                .orElseGet(() -> beans.get(parameterType));
    }
}
