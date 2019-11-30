package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.exception.CycleException;
import nextstep.di.initiator.BeanInitiator;
import nextstep.supports.TopologySort;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BeanFactoryImpl implements BeanFactory {
    private BeanInitiatorRegistry beanInitiatorRegistry = new BeanInitiatorRegistry();
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactoryImpl() {
    }

    public void addBeanInitiator(Class<?> clazz, BeanInitiator beanInitiator) {
        beanInitiatorRegistry.addBeanInitiator(clazz, beanInitiator);
    }

    public void initialize() {
        addBeans(createTopologySort().calculateReversedOrders(beanInitiatorRegistry.getPreInstantiatedTypes()));
    }

    private TopologySort<Class<?>> createTopologySort() {
        return new TopologySort<>(
                type -> getParameterTypes(beanInitiatorRegistry.getBeanInitiator(type)),
                () -> {
                    throw new CycleException();
                });
    }

    private void addBeans(List<Class<?>> types) {
        for (Class<?> type : types) {
            addBean(type);
        }
    }

    private void addBean(Class<?> type) {
        beans.put(type, instantiate(beanInitiatorRegistry.getBeanInitiator(type)));
    }

    private Object instantiate(BeanInitiator beanInitiator) {
        return beanInitiator.instantiate(getBeans(getParameterTypes(beanInitiator)));
    }

    private List<Class<?>> getParameterTypes(BeanInitiator beanInitiator) {
        return BeanFactoryUtils.findConcreteClasses(
                beanInitiator.getParameterTypes(),
                beanInitiatorRegistry.getPreInstantiatedTypes());
    }

    private Object[] getBeans(List<Class<?>> parameterTypes) {
        return parameterTypes.stream()
                .map(type -> getBean(type))
                .toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    @Override
    public Set<Class<?>> getBeanTypes(Predicate<Class<?>> predicate) {
        return beans.keySet().stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }
}
