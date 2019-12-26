package nextstep.di.beans.scanner;

import com.google.common.collect.Sets;
import nextstep.di.beans.BeanType;
import nextstep.di.beans.specification.AnnotatedTypeBeanSpecification;
import nextstep.di.beans.specification.BeanSpecification;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotatedTypeBeanScanner implements BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(AnnotatedTypeBeanScanner.class);

    private Reflections reflections;

    public AnnotatedTypeBeanScanner(Object[] basePackage) {
        reflections = new Reflections(basePackage);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<BeanSpecification> scan() {
        Class[] beanTypes = Arrays.stream(BeanType.values())
                .map(BeanType::getType)
                .toArray(Class[]::new);
        return scanBeansAnnotatedWith(beanTypes);
    }

    @SuppressWarnings("unchecked")
    private Set<BeanSpecification> scanBeansAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<BeanSpecification> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }

    private Set<BeanSpecification> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation).stream()
                .map(AnnotatedTypeBeanSpecification::new)
                .collect(Collectors.toSet());
    }
}
