package nextstep.di.beandefinition;

import com.google.common.collect.Sets;
import nextstep.di.exception.MultipleBeanImplementationException;
import nextstep.di.exception.NotExistBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanDefinitionRegistry {
    private static final Logger log = LoggerFactory.getLogger(BeanDefinitionRegistry.class);

    private Set<BeanDefinition> definitions;

    BeanDefinitionRegistry(Set<BeanDefinition> definitions) {
        this.definitions = definitions;
    }

    public static BeanDefinitionRegistry create() {
        return new BeanDefinitionRegistry(Sets.newHashSet());
    }

    public void register(BeanDefinition definition) {
        log.debug("add {}", definition);
        definitions.add(definition);
    }

    public Set<BeanDefinition> findByType(Class<?> type) {
        return definitions.stream()
                .filter(definition -> type.isAssignableFrom(definition.getBeanType()))
                .collect(Collectors.toSet());
    }

    public BeanDefinition findExactBeanDefinition(Class<?> type) {
        Set<BeanDefinition> definitions = findByType(type);

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

    public Set<BeanDefinition> findAll() {
        return Sets.newHashSet(definitions);
    }

    public static BeanDefinitionRegistry merge(BeanDefinitionRegistry registry1, BeanDefinitionRegistry registry2) {
        Set<BeanDefinition> newDefinitions = Sets.newHashSet(registry1.definitions);
        newDefinitions.addAll(registry2.definitions);

        return new BeanDefinitionRegistry(newDefinitions);
    }
}
