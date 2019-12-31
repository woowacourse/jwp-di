package nextstep.di.beandefinition;

import nextstep.di.exception.BeanWithoutConstructorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TypeBeanDefinitionTest {

    @Test
    @DisplayName("생성자가 존재하지 않는 타입")
    void of() {
        assertThrows(BeanWithoutConstructorException.class,
            () -> TypeBeanDefinition.of(WithoutConstructor.class));
    }

    // 생성자 여러개 존재하는 경우

    // @Injected 인 생성자가 존재하는 경우

    class WithoutConstructor {
    }
}