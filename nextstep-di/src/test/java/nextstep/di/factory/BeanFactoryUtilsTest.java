package nextstep.di.factory;

import nextstep.di.factory.utilsexample.*;
import nextstep.exception.NotFoundConstructorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryUtilsTest {
    @Test
    @DisplayName("인터페이스의 생성자를 찾으려는 경우 예외 발생")
    void findInterfaceConstructor() {
        assertThrows(NotFoundConstructorException.class, () ->
                BeanFactoryUtils.findConstructor(UtilsInterface.class));
    }

    @Test
    @DisplayName("생성자가 없어도 정상 동작")
    void notExistConstructor() {
        assertDoesNotThrow(() ->
                BeanFactoryUtils.findConstructor(NotExistController.class));
    }

    @Test
    @DisplayName("생성자가 하나이면 정상 동장")
    void oneConstructor() {
        assertDoesNotThrow(() ->
                BeanFactoryUtils.findConstructor(OneController.class));
    }

    @Test
    @DisplayName("생성자가 여러개인데 @Inject가 붙은 생성자가 하나이면 정상 동장")
    void oneInjectConstructor() {
        assertDoesNotThrow(() ->
                BeanFactoryUtils.findConstructor(OneInjectController.class));
    }

    @Test
    @DisplayName("@Inject 붙은 생성자가 두개 이상인 경우 예외 발생")
    void multipleInjectedConstructors() {
        assertThrows(NotFoundConstructorException.class, () ->
                BeanFactoryUtils.findConstructor(MultipleInjectController.class));
    }

    @Test
    @DisplayName("@Inject 붙은 생성자가 없고 생성자가 두개 이상인 경우 예외 발생")
    void multipleConstructors() {
        assertThrows(NotFoundConstructorException.class, () ->
                BeanFactoryUtils.findConstructor(MultipleController.class));
    }
}
