package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.annotation.Configuration;
import nextstep.di.context.ApplicationBeanContext;
import nextstep.di.exception.BeanDefinitionException;
import nextstep.di.factory.BeanFactoryUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

public class ConfigurationScanner {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationScanner.class);

    private Reflections reflections;
    private Set<MethodBeanDefinition> beans;

    public ConfigurationScanner(ApplicationBeanContext applicationBeanContext) {
        this.reflections = new Reflections(applicationBeanContext.getRoot());
    }

    public Set<MethodBeanDefinition> doScan() {
        if (beans != null) {
            return beans;
        }

        beans = Sets.newHashSet();
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Configuration.class)) {
            try {
                Object declaredObject = clazz.newInstance();
                beans.addAll(BeanFactoryUtils.getConfigurationBeanDefinitions(clazz, declaredObject));
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("{} clazz instance fail. error message : {}", clazz, e.getMessage());
                throw new BeanDefinitionException(e);
            }
        }
        beans = Collections.unmodifiableSet(beans);

        return beans;
    }
}
