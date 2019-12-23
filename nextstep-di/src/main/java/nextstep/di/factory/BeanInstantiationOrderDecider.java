package nextstep.di.factory;

import nextstep.di.beandefinition.BeanDefinition;
import nextstep.di.beandefinition.BeanDefinitionRegistry;
import nextstep.di.exception.BeanIncludingCycleException;
import nextstep.supports.TopologySort;

import java.util.List;
import java.util.stream.Collectors;

public class BeanInstantiationOrderDecider {
    private final BeanDefinitionRegistry registry;

    private BeanInstantiationOrderDecider(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public static BeanInstantiationOrderDecider of(BeanDefinitionRegistry registry) {
        return new BeanInstantiationOrderDecider(registry);
    }

    public List<BeanDefinition> decideOrder() {
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
                .map(type -> registry.findExactBeanDefinition(type))
                .collect(Collectors.toList());
    }
}
