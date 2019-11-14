package nextstep.di.factory.domain.beandefinition;

import nextstep.di.factory.example.JdbcUserRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstructorBeanTest {

    @Test
    public void makeInstance() throws NoSuchMethodException {
        Class<?> clazz = JdbcUserRepository.class;
        BeanDefinition beanDefinition = new AnnotationBeanDefinition(JdbcUserRepository.class, clazz.getConstructor());
        assertThat(beanDefinition.makeInstance()).isInstanceOf(JdbcUserRepository.class);
    }
}