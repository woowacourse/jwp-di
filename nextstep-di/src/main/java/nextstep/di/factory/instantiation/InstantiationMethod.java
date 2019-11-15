package nextstep.di.factory.instantiation;

import nextstep.di.factory.BeanCreateMatcher;

import java.util.Map;
import java.util.Optional;

public interface InstantiationMethod {
    Object getInstance(BeanCreateMatcher matcher, Map<Class<?>, Object> beans);

    default Object getParameterBean(BeanCreateMatcher beanCreateMatcher, Map<Class<?>, Object> beans, Class<?> parameterType) {
        InstantiationMethod instantiationMethod = beanCreateMatcher.get(parameterType);
        Optional<Object> maybeParameterBean = Optional.ofNullable(beans.putIfAbsent(parameterType, instantiationMethod.getInstance(beanCreateMatcher, beans)));
        return maybeParameterBean.orElseGet(() -> beans.get(parameterType));
    }
}
