package nextstep.di.factory;

import nextstep.annotation.ComponentScan;

import java.util.Arrays;

public class BeanScannerUtils {
    public static Object[] findBasePackage(Object... basePackage) {
        int PATH_EMPTY_COUNT = 0;

        Object[] basePackagePaths = Arrays.stream(basePackage)
            .filter(object -> object instanceof Class)
            .filter(object -> ((Class) object).isAnnotationPresent(ComponentScan.class))
            .map(object -> (ComponentScan) ((Class) object).getAnnotation(ComponentScan.class))
            .map(ComponentScan::basePackages)
            .distinct()
            .toArray();
        if (basePackagePaths.length != PATH_EMPTY_COUNT) {
            return basePackagePaths;
        }
        return basePackage;
    }
}
