package nextstep.di.factory;

import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    private static final String STEREO_TYPE_BASE_PACKAGE = "nextstep.stereotype";

    private Reflections reflections;
    private Set<Class<? extends Annotation>> stereoTypes;

    public BeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
        stereoTypes = getStereoTypes();
    }

    @SuppressWarnings("unchecked")
    private Set<Class<? extends Annotation>> getStereoTypes() {
        return new Reflections(STEREO_TYPE_BASE_PACKAGE)
                .getTypesAnnotatedWith(Retention.class)
                .stream()
                .map(stereoType -> (Class<? extends Annotation>) stereoType)
                .collect(Collectors.toSet());
    }

    public Set<Class<?>> getPreInstantiateBeans() {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> stereoType : stereoTypes) {
            beans.addAll(reflections.getTypesAnnotatedWith(stereoType));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
