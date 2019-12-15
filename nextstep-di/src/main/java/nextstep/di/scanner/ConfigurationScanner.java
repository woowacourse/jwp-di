package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.stereotype.Component;
import org.apache.commons.lang3.ArrayUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationScanner {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationScanner.class);

    private static final String[] COMPONENT_PACKAGE_PATHS = {"nextstep.annotation", "nextstep.stereotype"};

    private Set<Class<?>> configs;

    private ConfigurationScanner(Class<?> config) {
        this(Sets.newHashSet(config));
    }

    private ConfigurationScanner(Set<Class<?>> configs) {
        configs.stream()
                .forEach(config -> validate(config));

        this.configs = configs;
    }

    public static ConfigurationScanner from(Set<Class<?>> configs) {
        return new ConfigurationScanner(configs);
    }

    public static ConfigurationScanner of(Class<?> config) {
        return new ConfigurationScanner(config);
    }

    private void validate(Class<?> config) {
        if (!config.isAnnotationPresent(Configuration.class)) {
            throw new IllegalStateException("@Configuration 이 달린 클래스만 가능합니다. input: " + config);
        }
    }

    // scan classes of @Component
    public Set<Class<?>> scan() {
        Set<Class<?>> scannedClasses;
        // 관련된 패키지 경로 모두 찾기
        Reflections reflections = reflectionsWithComponentScannedPackages();

        // 2. ComponentScan 으로 찾은 모든 패키지 (음... @ComponentScan 에 있는 경로끼리 서로 참조한다면??
        // 결국은 ... @Configuration 을 통해서 할 것이기에.. Configuration 클래스로 이미 했는지를 판단하면 될 듯)
        // 순환 참조가 있는 경우도 있지 않을까? -> 일단 config 들을 모아놓기 (관련된 패키지 경로에 존재하는)
        scannedClasses = reflections.getTypesAnnotatedWith(Component.class).stream()
                .filter(type -> !type.isInterface())
                .filter(type -> !type.isAnnotation())
                .collect(Collectors.toSet());

        // 1. 본인 등록하기
        scannedClasses.addAll(configs);
        return scannedClasses;
    }

    private Reflections reflectionsWithComponentScannedPackages() {
        Set<Class<?>> preConfigs = null;
        Set<Class<?>> curConfigs = Sets.newHashSet(configs);

        // config 의 집합이 동일하면, 그 안에 존재하는 컴포넌트들도 동일
        while (!curConfigs.equals(preConfigs)) {
            preConfigs = curConfigs;

            curConfigs = createReflectionsWithDefaultPackages(curConfigs)
                    .getTypesAnnotatedWith(Configuration.class);

            // for @Configuration classes without @ComponentScan
            curConfigs.addAll(this.configs);
        }
        log.debug("curConfigs: {}", curConfigs);
        log.debug("packages: {}", Arrays.toString(getComponentScanPackages(curConfigs)));

        return createReflectionsWithDefaultPackages(curConfigs);
    }

    private Reflections createReflectionsWithDefaultPackages(Set<Class<?>> configs) {
        return new Reflections(COMPONENT_PACKAGE_PATHS, getComponentScanPackages(configs));
    }

    private String[] getComponentScanPackages(Set<Class<?>> configs) {
        return configs.stream()
                .map(config -> getComponentScanPackages(config))
                .reduce(new String[]{}, (arr1, arr2) -> ArrayUtils.addAll(arr1, arr2));
    }

    private String[] getComponentScanPackages(Class<?> config) {
        ComponentScan componentScan = config.getAnnotation(ComponentScan.class);

        if (Objects.isNull(componentScan)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        String[] packages = ArrayUtils.addAll(componentScan.basePackages(), componentScan.value());
        if (ArrayUtils.isEmpty(packages)) {
            return new String[]{config.getPackageName()};
        }
        return packages;
    }
}
