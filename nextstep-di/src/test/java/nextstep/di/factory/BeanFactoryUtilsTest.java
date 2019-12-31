package nextstep.di.factory;

import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.repository.JdbcQuestionRepository;
import nextstep.di.factory.example.repository.JdbcUserRepository;
import nextstep.di.factory.example.repository.UserRepository;
import nextstep.di.factory.example.service.MyQnaService;
import nextstep.di.factory.exception.DoesNotAllowMultipleInjectedConstructorException;
import nextstep.di.factory.outside.MultipleInjectedService;
import nextstep.di.factory.outside.OutsideService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BeanFactoryUtilsTest {

    @Test
    @DisplayName("@Inject 애너테이션이 선언된 생성자를 반환한다.")
    void getInjectedConstructor() {
        Set<Constructor> constructor = BeanFactoryUtils.getInjectedConstructors(QnaController.class);
        Class<?>[] parameterTypes = constructor.iterator().next().getParameterTypes();

        assertThat(constructor.size()).isEqualTo(1);
        assertThat(parameterTypes.length).isEqualTo(1);
        assertThat(parameterTypes).contains(MyQnaService.class);
    }

    @Test
    @DisplayName("@Inject 애너테이션이 여러개 선언 된 클래스인 경우 예외를 반환한다.")
    void getInjectedConstructor_amongManyInjectedConstructors_returnFirstConstructor() {
        assertThatThrownBy(() -> BeanFactoryUtils.getInjectedConstructors(MultipleInjectedService.class))
                .isInstanceOf(DoesNotAllowMultipleInjectedConstructorException.class);
    }

    @Test
    @DisplayName("@Inject 애너테이션이 없는 경우에는 null을 반환한다.")
    void getInjectedConstructor_IfInjectedConstructorIsEmpty_returnNull() {
        Set<Constructor> injectedConstructors = BeanFactoryUtils.getInjectedConstructors(JdbcQuestionRepository.class);
        assertThat(injectedConstructors).isEmpty();
    }

    @Test
    @DisplayName("인터페이스에 대한 BeanDefinition을 찾아준다.")
    void findConcreteDefinitionOfInterface() {
        Map<Class<?>, BeanDefinition> definitions = BeanDefinitionFactory.createBeanDefinitions(BeanScanner.scanConfiguration("nextstep.di.factory.example"));

        BeanDefinition beanDefinition = BeanFactoryUtils.findBeanDefinition(UserRepository.class, definitions);
        assertThat(beanDefinition.getType()).isEqualTo(JdbcUserRepository.class);
    }

    @Test
    @DisplayName("해당하는 클래스에 대한 BeanDefinition을 찾아준다.")
    void findConcreteDefinitionOfClass() {
        Map<Class<?>, BeanDefinition> definitions = BeanDefinitionFactory.createBeanDefinitions(BeanScanner.scanConfiguration("nextstep.di.factory.example"));

        BeanDefinition beanDefinition = BeanFactoryUtils.findBeanDefinition(MyQnaService.class, definitions);
        assertThat(beanDefinition.getType()).isEqualTo(MyQnaService.class);
    }

    @Test
    @DisplayName("존재하지 않는 BeanDefinition을 찾는 경우 예외를 던진다.")
    void failToFindDefinition() {
        Map<Class<?>, BeanDefinition> definitions = BeanDefinitionFactory.createBeanDefinitions(BeanScanner.scanConfiguration("nextstep.di.factory.example"));

        assertThatThrownBy(() -> BeanFactoryUtils.findBeanDefinition(OutsideService.class, definitions))
                .isInstanceOf(IllegalStateException.class);
    }
}