package nextstep.di.factory;

import javax.sql.DataSource;

import com.google.common.collect.Sets;
import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.UserRepository;
import nextstep.exception.NotFoundConcreteClazzException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BeanFactoryUtilsTest {
    @Test
    @DisplayName("생성자에 Inject 어노테이션이 있을 때 해당 생성자 반환")
    void getInjectedConstructor() {
        assertNotNull(BeanFactoryUtils.getInjectedConstructor(MyQnaService.class));
    }

    @Test
    @DisplayName("생성자에 Inject 어노테이션이 없을 때 null 반환")
    void getInjectedConstructorReturnNull() {
        assertNull(BeanFactoryUtils.getInjectedConstructor(JdbcUserRepository.class));
    }

    @Test
    @DisplayName("해당 인터페이스를 구현한 클래스 반환")
    void findConcreteClass() {
        Class concreteClazz = JdbcUserRepository.class;
        Class<?> clazz = BeanFactoryUtils.findConcreteClass(UserRepository.class,
                Sets.newHashSet(concreteClazz));
        assertThat(clazz).isEqualTo(concreteClazz);
    }

    @Test
    @DisplayName("인터페이스가 아닐 때 메서드의 인자인 클래스 반환")
    void findConcreteClassWhenAlreadyConcreteClass() {
        Class concreteClazz = JdbcUserRepository.class;
        Class<?> clazz = BeanFactoryUtils.findConcreteClass(concreteClazz,
                Sets.newHashSet(concreteClazz));
        assertThat(clazz).isEqualTo(concreteClazz);
    }

    @Test
    @DisplayName("인터페이스를 구현한 클래스가 존재하지 않을 때 예외 반환")
    void findConcreteClassWhenNotFoundConcreteClass() {
        assertThrows(NotFoundConcreteClazzException.class, () ->
                BeanFactoryUtils.findConcreteClass(UserRepository.class, Sets.newHashSet()));
    }

    @Test
    @DisplayName("메서드의 실행 결과를 반환")
    void instantiateClass() throws NoSuchMethodException {
        ExampleConfig exampleConfig = new ExampleConfig();
        Object object = BeanFactoryUtils.instantiateClass(exampleConfig.getClass().getMethod("dataSource"));
        assertTrue(DataSource.class.isAssignableFrom(object.getClass()));
    }
}