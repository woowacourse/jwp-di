package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.annotation.Configuration;
import nextstep.di.BeanDefinition;
import nextstep.di.exception.BeanDefinitionException;
import nextstep.di.factory.BeanFactoryUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ConfigurationScanner implements BeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationScanner.class);

    private Reflections reflections;

    public ConfigurationScanner(Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    @Override
    public Set<BeanDefinition> doScan() {
        Set<BeanDefinition> beans = Sets.newHashSet();

        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Configuration.class)) {
            try {
                Object declaredObject = clazz.newInstance();
                beans.addAll(BeanFactoryUtils.getMethodBeanDefinitions(clazz, declaredObject));
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("{} clazz instance fail. error message : {}", clazz, e.getMessage());
                throw new BeanDefinitionException(e);
            }
        }

        return beans;
    }
}
