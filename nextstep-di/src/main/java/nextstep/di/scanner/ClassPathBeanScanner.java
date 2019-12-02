package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.di.factory.BeanFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

public class ClassPathBeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ClassPathBeanScanner.class);

    private BeanFactory beanFactory;
    private Reflections reflections;

    public ClassPathBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @SuppressWarnings("unchecked")
    public void scan(Object... basePackage) {
        reflections = new Reflections(basePackage);

        Class[] beanTypes = Arrays.stream(BeanType.values())
                .map(BeanType::getType)
                .toArray(Class[]::new);
        Set<Class<?>> beans = scanBeansAnnotatedWith(beanTypes);

        beanFactory.appendPreInstantiateBeans(beans);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> scanBeansAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
