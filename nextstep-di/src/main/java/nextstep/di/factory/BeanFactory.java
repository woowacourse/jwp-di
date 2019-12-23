package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.beandefinition.BeanDefinition;
import nextstep.di.beandefinition.BeanDefinitionRegistry;
import nextstep.di.beandefinition.TypeBeanDefinition;
import nextstep.di.exception.BeanIncludingCycleException;
import nextstep.di.exception.MultipleBeanImplementationException;
import nextstep.di.exception.NotExistBeanException;
import nextstep.supports.TopologySort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Map<BeanDefinition, Object> beans = Maps.newHashMap();
    private BeanDefinitionRegistry registry = BeanDefinitionRegistry.create();

    private BeanFactory() {
    }

    public static BeanFactory initializeWith(Set<Class<?>> scannedTypes) {
        log.debug("scannedTypes: {}", scannedTypes);

        BeanFactory factory = new BeanFactory();
        factory.initialize(scannedTypes);

        return factory;
    }

    private void initialize(Set<Class<?>> scannedTypes) {
        initializeRegistry(scannedTypes);

        List<BeanDefinition> beanInstantiationOrder = calculateBeanInstantiationOrder();

        addBeansWithOrder(beanInstantiationOrder);
    }

    private void initializeRegistry(Set<Class<?>> scannedTypes) {
        for (Class<?> type : scannedTypes) {
            registry.register(TypeBeanDefinition.of(type));
        }
    }

    private List<BeanDefinition> calculateBeanInstantiationOrder() {
        TopologySort<BeanDefinition> topologySort = createTopologySort();

        return topologySort.calculateReversedOrders();
    }


    private TopologySort<BeanDefinition> createTopologySort() {
        return new TopologySort<>(
                registry.findAll(),
                definition -> collectDependantBeanDefinitions(definition),
                definition -> {
                    throw BeanIncludingCycleException.of(definition);
                }
        );
    }

    private List<BeanDefinition> collectDependantBeanDefinitions(BeanDefinition definition) {
        return definition.getDependantTypes().stream()
                .map(type -> findExactBeanDefinition(type))
                .collect(Collectors.toList());
    }

    private BeanDefinition findExactBeanDefinition(Class<?> type) {
        Set<BeanDefinition> definitions = registry.findByType(type);

        if (definitions.isEmpty()) {
            throw NotExistBeanException.from(type);
        }

        if (2 <= definitions.size()) {
            List<Class<?>> candidateTypes = definitions.stream()
                    .map(definition -> definition.getBeanType())
                    .collect(Collectors.toList());
            throw MultipleBeanImplementationException.from(type, candidateTypes);
        }

        return definitions.stream()
                .findFirst()
                .get();
    }

    private void addBeansWithOrder(List<BeanDefinition> beanInstantiationOrder) {
        for (BeanDefinition definition : beanInstantiationOrder) {
            addBean(definition);
        }
    }

    private void addBean(BeanDefinition definition) {
        beans.put(definition, definition.create(this));
    }

    public <T> T getBean(Class<T> requiredType) {
        BeanDefinition definition = findExactBeanDefinition(requiredType);

        return (T) beans.get(definition);
    }

    public Map<Class<?>, Object> getBeansSatisfiedWith(Predicate<Class<?>> predicate) {
        return beans.keySet().stream()
                .map(definition -> definition.getBeanType())
                .filter(predicate)
                .collect(Collectors.toMap(type -> type, this::getBean));
    }
}
