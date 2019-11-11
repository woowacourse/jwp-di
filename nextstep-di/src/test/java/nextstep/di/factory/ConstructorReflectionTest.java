package nextstep.di.factory;

import nextstep.annotation.Bean;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.TestService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstructorReflectionTest {

    @Test
    void checkEmptyConstructorTest() {
        assertThat(JdbcUserRepository.class.getDeclaredConstructors().length).isEqualTo(1);
    }

    @Test
    void method_bean() {
        Class<?> clazz = TestService.class;
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(Bean.class)) {

            }
        }

    }

}
