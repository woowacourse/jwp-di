package nextstep.di.bean;

import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigBeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(ConfigBeanScanner.class);

    private final Class<?> config;

    public ConfigBeanScanner(Class<?> config) {
        this.config = config;
    }

    public Object[] getBasePackages() {
        return this.config.getAnnotation(ComponentScan.class).basePackages();
    }

    public Map<Class<?>, Method> getConfigBeansToInstantiate() {
        final Map<Class<?>, Method> beans = Stream.of(this.config).map(Class::getDeclaredMethods)
                                                                    .flatMap(Stream::of)
                                                                    .filter(f -> f.isAnnotationPresent(Bean.class))
                                                                    .collect(
                                                                            Collectors.toMap(
                                                                                    Method::getReturnType,
                                                                                    Function.identity()
                                                                            )
                                                                    );
        logger.debug("Scanned Bean Types: {}", beans.keySet());
        return beans;
    }
}