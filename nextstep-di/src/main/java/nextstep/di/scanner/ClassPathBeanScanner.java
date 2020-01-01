package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.di.definition.BeanDefinition;
import nextstep.di.definition.ClassPathBeanDefinition;
import nextstep.di.factory.BeanFactory;
import nextstep.stereotype.BeanAnnotations;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ClassPathBeanScanner implements BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ClassPathBeanScanner.class);
    private static final Set<Class<? extends Annotation>> ANNOTATIONS = BeanAnnotations.getClazz();

    private Reflections reflections;

    public ClassPathBeanScanner(Set<String> basePackage) {
        reflections = new Reflections(basePackage);
    }

    @Override
    public void register(BeanFactory beanFactory) {
        Set<BeanDefinition> classPathBeanDefinitions = scan().stream()
                .map(ClassPathBeanDefinition::new)
                .collect(toSet());

        beanFactory.register(classPathBeanDefinitions);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> scan() {
        Set<Class<?>> beans = Sets.newHashSet();

        for (Class<? extends Annotation> annotation : ANNOTATIONS) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);

        return beans;
    }
}
