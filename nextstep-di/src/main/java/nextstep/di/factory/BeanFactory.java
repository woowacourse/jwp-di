package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.beandefinition.BeanDefinition;
import nextstep.di.beandefinition.BeanDefinitionRegister;
import nextstep.di.beandefinition.BeanDefinitionRegistry;
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
        registry = BeanDefinitionRegister.register(scannedTypes);
    }

    private List<BeanDefinition> calculateBeanInstantiationOrder() {
        return BeanInstantiationOrderDecider.of(registry)
                .decideOrder();
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
        BeanDefinition definition = registry.findExactBeanDefinition(requiredType);

        return (T) beans.get(definition);
    }

    public Map<Class<?>, Object> getBeansSatisfiedWith(Predicate<Class<?>> predicate) {
        return beans.keySet().stream()
                .map(definition -> definition.getBeanType())
                .filter(predicate)
                .collect(Collectors.toMap(type -> type, this::getBean));
    }
}
