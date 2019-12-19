package nextstep.di.factory.scanner;

import nextstep.di.factory.beandefinition.BeanDefinition;

import java.util.Set;

public interface BeanScanner {

    Set<BeanDefinition> scan();
}
