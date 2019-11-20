package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.di.factory.BeanFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

public abstract class AbstractBeanScanner implements BeanScanner {

    private static final Logger log = LoggerFactory.getLogger(AbstractBeanScanner.class);

    Reflections reflections;
    BeanFactory beanFactory;

    AbstractBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public abstract void doScan(Object... basePackage);

    @SuppressWarnings("unchecked")
    protected abstract <T> Map<Class<?>, T> getBeans();

    @SuppressWarnings("unchecked")
    Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
