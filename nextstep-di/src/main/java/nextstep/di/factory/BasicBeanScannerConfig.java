package nextstep.di.factory;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public class BasicBeanScannerConfig implements BeanScannerConfig {
    private Object[] basePackage;
    private List<Class<? extends Annotation>> annotations;

    public BasicBeanScannerConfig(List<Class<? extends Annotation>> annotations, Object... basePackage) {
        this.basePackage = basePackage;
        this.annotations = annotations;
    }

    @Override
    public Object[] getBasePackage() {
        return basePackage;
    }

    @Override
    public List<Class<? extends Annotation>> getAnnotations() {
        return Collections.unmodifiableList(annotations);
    }
}
