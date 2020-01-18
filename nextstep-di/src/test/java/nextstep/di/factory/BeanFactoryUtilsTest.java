package nextstep.di.factory;

import nextstep.annotation.Inject;
import nextstep.di.exception.BeanWithoutConstructorException;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanFactoryUtilsTest {

    @Test
    @DisplayName("생성자가 존재하지 않는 타입")
    void getBeanConstructor() {
        assertThrows(BeanWithoutConstructorException.class,
            () -> BeanFactoryUtils.getBeanConstructor(WithoutConstructor.class));
    }

    @Test
    @DisplayName("@Inject 가 달린 생성자가 존재하는 경우")
    void getBeanConstructor_hasInjectedConstructor() {
        Constructor<?> constructor = BeanFactoryUtils.getBeanConstructor(QnaController.class);

        assertThat(constructor.isAnnotationPresent(Inject.class)).isTrue();
    }

    class WithoutConstructor {
    }
}