package nextstep.di.factory;

import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    private Reflections reflections;
    private BeanScannerConfig beanScannerConfig;

    public BeanScanner(BeanScannerConfig beanScannerConfig) {
        reflections = new Reflections(beanScannerConfig.getBasePackage());
        this.beanScannerConfig = beanScannerConfig;
    }

    public Set<Class<?>> getTypesAnnotatedWith() {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : beanScannerConfig.getAnnotations()) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
