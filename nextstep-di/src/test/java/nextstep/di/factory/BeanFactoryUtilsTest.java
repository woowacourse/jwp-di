package nextstep.di.factory;

import nextstep.annotation.Inject;
import nextstep.stereotype.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class BeanFactoryUtilsTest {
    @DisplayName("@Inject 애노테이션이 붙어있는 생성자 반환")
    @Test
    void getInjectedConstructor() {
        Constructor<?> constructor = TestClass.class.getConstructors()[0];
        assertThat(BeanFactoryUtils.getInjectedConstructor(TestClass.class)).isEqualTo(constructor);
    }

    @DisplayName("@Inject 애노테이션이 붙어있는 생성자가 없을 경우 null 반환")
    @Test
    void getInjectedConstructor2() {
        assertThat(BeanFactoryUtils.getInjectedConstructor(TestClassWithoutInject.class)).isNull();
    }

    @DisplayName("구현 클래스 반환")
    @Test
    void findConcreteClass() {
        Set<Class<?>> preInstantiateBeans = new HashSet<>(Arrays.asList(TestClass.class, TestClassWithoutInject.class));
        assertThat(BeanFactoryUtils.findConcreteClass(TestClass.class, preInstantiateBeans)).isEqualTo(TestClass.class);
    }

    @DisplayName("인터페이스의 구현 클래스 반환")
    @Test
    void findConcreteClass2() {
        Set<Class<?>> preInstantiateBeans = new HashSet<>(Arrays.asList(TestClass.class, TestClassWithoutInject.class));
        assertThat(BeanFactoryUtils.findConcreteClass(TestInterface.class, preInstantiateBeans)).isEqualTo(TestClass.class);
    }

    @DisplayName("인터페이스의 구현 클래스가 없을 경우 에러")
    @Test
    void findConcreteClass3() {
        Set<Class<?>> preInstantiateBeans = new HashSet<>(Arrays.asList(TestClassWithoutInject.class));
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> BeanFactoryUtils.findConcreteClass(TestInterface.class, preInstantiateBeans));
    }

    @Controller
    private class TestClass implements TestInterface {
        @Inject
        public TestClass() {
        }
    }

    private interface TestInterface {
    }

    @Controller
    private class TestClassWithoutInject {
    }
}
