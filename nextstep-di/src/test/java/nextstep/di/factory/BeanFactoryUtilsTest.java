package nextstep.di.factory;

import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.repository.JdbcQuestionRepository;
import nextstep.di.factory.example.service.MyQnaService;
import nextstep.di.factory.outside.MultipleInjectedService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNull;

class BeanFactoryUtilsTest {
    @Test
    @DisplayName("@Inject 애너테이션이 선언된 생성자를 반환한다.")
    void getInjectedConstructor() {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(QnaController.class);
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        assertThat(parameterTypes.length).isEqualTo(1);
        assertThat(parameterTypes).contains(MyQnaService.class);
    }

    @Test
    @DisplayName("@Inject 애너테이션이 여러개 선언 된 클래스인 경우 예외를 반환한다.")
    void getInjectedConstructor_amongManyInjectedConstructors_returnFirstConstructor() {
        assertThatThrownBy(() -> BeanFactoryUtils.getInjectedConstructor(MultipleInjectedService.class))
                .isInstanceOf(DoesNotAllowMultipleInjectedConstructorException.class);
    }

    @Test
    @DisplayName("@Inject 애너테이션이 없는 경우에는 null을 반환한다.")
    void getInjectedConstructor_IfInjectedConstructorIsEmpty_returnNull() {
        assertNull(BeanFactoryUtils.getInjectedConstructor(JdbcQuestionRepository.class));
    }
}