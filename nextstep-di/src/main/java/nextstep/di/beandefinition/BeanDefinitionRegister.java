package nextstep.di.beandefinition;

import java.util.Set;

public interface BeanDefinitionRegister {
    BeanDefinitionRegistry register(Set<Class<?>> scannedTypes);
}
