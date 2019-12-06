package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.exception.ConfigurationBeanRegisterFailException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public class ConfigurationBeanScanner {
    public ConfigurationBeanScanner() {
    }

    public Set<Class<?>> scan(final Class<?> configureClass) {
        Set<Class<?>> configurationBeans = Sets.newHashSet();

        Method[] declaredMethods = configureClass.getDeclaredMethods();

        Arrays.stream(declaredMethods)
            .filter(this::isBeanMethod)
            .map(declaredMethod -> Optional.ofNullable(declaredMethod.getReturnType()))
            .forEach(maybeReturnType -> maybeReturnType.ifPresentOrElse(configurationBeans::add,
                ConfigurationBeanRegisterFailException::new));

        return configurationBeans;

    }

    private boolean isBeanMethod(Method declaredMethod) {
        return declaredMethod.isAnnotationPresent(Bean.class);
    }

}
