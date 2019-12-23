package nextstep.di.factory;

import nextstep.di.exception.BeanWithoutConstructorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanFactoryUtilsTest {

    @Test
    @DisplayName("생성자가 존재하지 않는 타입")
    void getBeanConstructor() {
        assertThrows(BeanWithoutConstructorException.class,
                () -> BeanFactoryUtils.getBeanConstructor(WithoutConstructor.class));
    }

    class WithoutConstructor {
    }
}