package nextstep.di.factory;

import java.lang.annotation.Annotation;
import java.util.List;

public interface BeanScannerConfig {
    Object[] getBasePackage();

    List<Class<? extends Annotation>> getAnnotations();
}
