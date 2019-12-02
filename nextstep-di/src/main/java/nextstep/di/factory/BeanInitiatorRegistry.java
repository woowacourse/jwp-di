package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.initiator.BeanInitiator;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BeanInitiatorRegistry {
    private Map<Class<?>, BeanInitiator> beanInitiators = Maps.newHashMap();

    public void addBeanInitiator(Class<?> clazz, BeanInitiator beanInitiator) {
        beanInitiators.put(clazz, beanInitiator);
    }

    public Set<Class<?>> getPreInstantiatedTypes() {
        return beanInitiators.keySet();
    }

    public BeanInitiator getBeanInitiator(Class<?> type) {
        return Optional.ofNullable(beanInitiators.get(type))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 타입입니다."));
    }
}
