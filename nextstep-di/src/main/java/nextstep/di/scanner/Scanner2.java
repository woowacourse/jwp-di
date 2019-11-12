package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;

import java.util.Set;

public interface Scanner2 {
    Set<BeanDefinition> scan(String basePackage);
}
