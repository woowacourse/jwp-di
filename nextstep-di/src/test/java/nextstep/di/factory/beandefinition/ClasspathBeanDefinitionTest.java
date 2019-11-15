package nextstep.di.factory.beandefinition;

import nextstep.di.factory.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClasspathBeanDefinitionTest {
    private ClasspathBeanDefinition classpathBeanDefinition;

    @BeforeEach
    public void setUp() {
        classpathBeanDefinition = new ClasspathBeanDefinition(MyQnaService.class);
    }

    @Test
    public void getClassTypeTest() {
        assertThat(classpathBeanDefinition.getClassType()).isEqualTo(MyQnaService.class);
    }

    @Test
    public void getParameterTypesTest() {
        Class<?>[] parameterTypes = {UserRepository.class, QuestionRepository.class};

        assertThat(classpathBeanDefinition.getParameterTypes()).isEqualTo(parameterTypes);
    }

    @Test
    public void instantiateTest() {
        Object instance = classpathBeanDefinition.instantiate(
                new JdbcUserRepository(),
                new JdbcQuestionRepository()
        );

        assertThat(instance).isExactlyInstanceOf(MyQnaService.class);
    }
}
