package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.exception.BeanCreationFailException;
import nextstep.di.exception.BeanIncludingCycleException;
import nextstep.di.exception.NotExistBeanException;
import nextstep.supports.TopologySort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiatedTypes;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiatedTypes) {
        this.preInstantiatedTypes = preInstantiatedTypes;
    }

    public void initialize() {
        addBeans(createTopologySort().calculateReversedOrders());
    }

    // 아... 파라미터에만 존재하는 경우도 결과 노드에 포함되는 구나...
    // 그렇다면 파라미터에 자주 나오는 타입은?? 빨리 해결이 되겠지??
    private TopologySort<Class<?>> createTopologySort() {
        return new TopologySort<>(
                preInstantiatedTypes,
                type -> getParameterTypes(BeanFactoryUtils.getBeanConstructor(type)),
                (node) -> {
                    throw new BeanIncludingCycleException(node);
                });
    }

    private void addBeans(List<Class<?>> types) {
        for (Class<?> type : types) {
            addBean(type);
        }
    }

    // 토폴로지 소트에서... 실제로 빈으로 등록되야 할 애들 이외에도 파라미테에 존재하는 애들도 추가함
    // 이런 경우 어디서 잡아주는게 맞는 걸까? (토폴로지 소트가 이런 경우도 잡아주어야 할까? -> 입력으로 주어진 노드 이외를 가르킬때..)
    private void addBean(Class<?> type) {
        log.debug("[addBean] type: {}", type);
        validateCanBeBean(type);

        beans.put(type, instantiate(BeanFactoryUtils.getBeanConstructor(type)));
    }

    private void validateCanBeBean(Class<?> type) {
        if (!type.isInterface() && !preInstantiatedTypes.contains(type)) {
            throw NotExistBeanException.from(type);
        }
        BeanFactoryUtils.findConcreteClass(type, preInstantiatedTypes);
    }

    private Object instantiate(Constructor<?> constructor) {
        return BeanUtils.instantiateClass(constructor, getBeansSatisfiedWith(getParameterTypes(constructor)));
    }

    private List<Class<?>> getParameterTypes(Constructor<?> constructor) {
        try {
            return BeanFactoryUtils.findConcreteClasses(Arrays.asList(constructor.getParameterTypes()), preInstantiatedTypes);
        } catch (NotExistBeanException e) {
            throw BeanCreationFailException.constructWithNotExistParameter(constructor, e.getType());
        }
    }

    private Object[] getBeansSatisfiedWith(List<Class<?>> parameterTypes) {
        return parameterTypes.stream()
                .map(type -> getBean(type))
                .toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getBeansSatisfiedWith(Predicate<Class<?>> predicate) {
        return beans.keySet().stream()
                .filter(predicate)
                .collect(Collectors.toMap(type -> type, this::getBean));
    }
}
