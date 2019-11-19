package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.annotation.Configuration;
import nextstep.di.BeanDefinition;
import nextstep.di.context.ApplicationBeanContext;
import nextstep.di.exception.BeanDefinitionException;
import nextstep.di.factory.BeanFactoryUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

public class ConfigurationScanner implements BeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationScanner.class);

    private Reflections reflections;
    private Set<BeanDefinition> beans;

    public ConfigurationScanner(ApplicationBeanContext applicationBeanContext) {
        this.reflections = new Reflections(applicationBeanContext.getRoot());
    }

    @Override
    public Set<BeanDefinition> doScan() {
        if (beans != null) {
            return beans;
        }

        beans = Sets.newHashSet();
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Configuration.class)) {
            try {
                Object declaredObject = clazz.newInstance();
                beans.addAll(BeanFactoryUtils.getMethodBeanDefinitions(clazz, declaredObject));
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("{} clazz instance fail. error message : {}", clazz, e.getMessage());
                throw new BeanDefinitionException(e);
            }
        }
        beans = Collections.unmodifiableSet(beans);

        return beans;
    }
}
