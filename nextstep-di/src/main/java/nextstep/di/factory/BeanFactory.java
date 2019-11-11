package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.initiator.BeanInitiator;
import nextstep.di.scanner.BeanScanners;
import nextstep.supports.TopologySort;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BeanFactory {
    private BeanScanners beanScanners;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(BeanScanners beanScanners) {
        this.beanScanners = beanScanners;
    }

    public void initialize() {
        addBeans(createTopologySort().calculateReversedOrders(beanScanners.getPreInstantiatedTypes()));
    }

    private TopologySort<Class<?>> createTopologySort() {
        return new TopologySort<>(
                type -> getParameterTypes(beanScanners.getBeanInitiator(type)),
                () -> {
                    throw new IllegalArgumentException("사이클..!!");
                });
    }

    private void addBeans(List<Class<?>> types) {
        for (Class<?> type : types) {
            addBean(type);
        }
    }

    private void addBean(Class<?> type) {
        beans.put(type, instantiate(beanScanners.getBeanInitiator(type)));
    }

    private Object instantiate(BeanInitiator beanInitiator) {
        return beanInitiator.instantiate(getBeans(getParameterTypes(beanInitiator)));
    }

    private List<Class<?>> getParameterTypes(BeanInitiator beanInitiator) {
        return BeanFactoryUtils.findConcreteClasses(
                beanInitiator.getParameterTypes(),
                beanScanners.getPreInstantiatedTypes());
    }

    private Object[] getBeans(List<Class<?>> parameterTypes) {
        return parameterTypes.stream()
                .map(type -> getBean(type))
                .toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Set<Class<?>> getBeanTypes(Predicate<Class<?>> predicate) {
        return beans.keySet().stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }
}
