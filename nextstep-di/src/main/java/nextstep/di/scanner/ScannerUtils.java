package nextstep.di.scanner;

import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ScannerUtils {
    private static final Logger log = LoggerFactory.getLogger(ScannerUtils.class);

    @SuppressWarnings("unchecked")
    public static Set<Class<?>> getTypesAnnotateWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> annotationBeans = Sets.newHashSet();

        for (Class<? extends Annotation> annotation : annotations) {
            annotationBeans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", annotationBeans);
        return annotationBeans;
    }
}
