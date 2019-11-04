package nextstep.di.factory;

import nextstep.exception.BeanFactoryInitializeException;
import nextstep.stereotype.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryInitializerTest {
    @Test
    @DisplayName("생성자가 없어도 정상 동작")
    void notExistConstructor() {
        assertDoesNotThrow(() ->
                BeanFactoryInitializer.init(Arrays.asList(Controller.class),
                        "nextstep.di.factory.exception.notexist"));
    }

    @Test
    @DisplayName("생성자가 하나이면 정상 동장")
    void oneConstructor() {
        assertDoesNotThrow(() ->
                BeanFactoryInitializer.init(Arrays.asList(Controller.class),
                        "nextstep.di.factory.exception.one"));
    }

    @Test
    @DisplayName("생성자가 여러개인데 @Inject가 붙은 생성자가 하나이면 정상 동장")
    void oneInjectConstructor() {
        assertDoesNotThrow(() ->
                BeanFactoryInitializer.init(Arrays.asList(Controller.class),
                        "nextstep.di.factory.exception.oneinject"));
    }

    @Test
    @DisplayName("@Inject 붙은 생성자가 두개 이상인 경우 예외 발생")
    void multipleInjectedConstructors() {
        assertThrows(BeanFactoryInitializeException.class, () ->
                BeanFactoryInitializer.init(Arrays.asList(Controller.class),
                        "nextstep.di.factory.exception.multipleinject"));
    }

    @Test
    @DisplayName("@Inject 붙은 생성자가 없고 생성자가 두개 이상인 경우 예외 발생")
    void multipleConstructors() {
        assertThrows(BeanFactoryInitializeException.class, () ->
                BeanFactoryInitializer.init(Arrays.asList(Controller.class),
                        "nextstep.di.factory.exception.multiple"));
    }
}
