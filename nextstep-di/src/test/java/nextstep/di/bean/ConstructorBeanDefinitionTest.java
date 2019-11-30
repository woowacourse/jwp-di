package nextstep.di.bean;

import nextstep.di.factory.example.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConstructorBeanDefinitionTest {

    private static final BeanDefinition BEAN_DEFINITION = new ConstructorBeanDefinition(MyQnaService.class);

    @Test
    void getParameterTypes() {
        assertThat(BEAN_DEFINITION.getParameterTypes())
                .contains(UserRepository.class, QuestionRepository.class);
    }

    @Test
    void instantiate() {
        MyQnaService myQnaService = (MyQnaService) BEAN_DEFINITION.instantiate(new JdbcUserRepository(), new JdbcQuestionRepository());
        assertThat(myQnaService).isNotNull();
    }

    @Test
    void getClazz() {
        assertThat(BEAN_DEFINITION.getClazz()).isEqualTo(MyQnaService.class);
    }
}