package nextstep.di.beandefinition;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.stream.Collectors;

public class BeanDefinitionRegistry {
    private Set<BeanDefinition> definitions;

    BeanDefinitionRegistry(Set<BeanDefinition> definitions) {
        this.definitions = definitions;
    }

    public static BeanDefinitionRegistry create() {
        return new BeanDefinitionRegistry(Sets.newHashSet());
    }

    public void register(BeanDefinition definition) {
        definitions.add(definition);
    }

    public Set<BeanDefinition> findByType(Class<?> type) {
        return definitions.stream()
                .filter(definition -> type.isAssignableFrom(definition.getBeanType()))
                .collect(Collectors.toSet());
    }

    public Set<BeanDefinition> findAll() {
        return Sets.newHashSet(definitions);
    }
}
