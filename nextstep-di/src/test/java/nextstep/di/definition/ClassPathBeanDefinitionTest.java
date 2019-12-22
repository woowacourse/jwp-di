package nextstep.di.definition;

import nextstep.di.factory.example.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassPathBeanDefinitionTest {

    @DisplayName("인스턴스화 확인")
    @Test
    void instantiate() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        BeanDefinition qrbd = new ClassPathBeanDefinition(JdbcQuestionRepository.class);
        BeanDefinition urbd = new ClassPathBeanDefinition(JdbcUserRepository.class);

        assertThat(qrbd.instantiate()).isInstanceOf(QuestionRepository.class);
        assertThat(urbd.instantiate()).isInstanceOf(UserRepository.class);
    }

    @DisplayName("생성자에 @Inject 있는 경우에, 인스턴스화 확인")
    @Test
    void instantiateWithInjectAnnotation() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        BeanDefinition beanDefinition = new ClassPathBeanDefinition(MyQnaService.class);

        assertThat(beanDefinition.instantiate(new JdbcUserRepository(), new JdbcQuestionRepository()))
                .isInstanceOf(MyQnaService.class);
    }

    @DisplayName("생성자의 파라미터 타입을 반환")
    @Test
    void getParameterType() {
        BeanDefinition beanDefinition = new ClassPathBeanDefinition(MyQnaService.class);

        assertThat(beanDefinition.getParameterTypes()).contains(UserRepository.class, QuestionRepository.class);
    }

    @DisplayName("생성자에 @Inject 없는 경우에, 생성자 파라미터 타입을 반환")
    @Test
    void getParameterTypeWithoutInjectAnnotation() {
        BeanDefinition beanDefinition = new ClassPathBeanDefinition(JdbcQuestionRepository.class);

        assertThat(beanDefinition.getParameterTypes()).isNull();
    }
}
