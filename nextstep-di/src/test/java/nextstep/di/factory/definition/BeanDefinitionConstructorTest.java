package nextstep.di.factory.definition;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanDefinitionConstructorTest {

    @Test
    @DisplayName("생성자 파라미터 받아오는 테스트")
    void getParametersTest() throws NoSuchMethodException {
        BeanDefinitionConstructor beanDefinitionConstructor = new BeanDefinitionConstructor(SummerIsKingEmperor.class.getConstructor(String.class));
        assertThat(beanDefinitionConstructor.getParameterTypes()).isEqualTo(new Class<?>[]{String.class});
    }

    @Test
    @DisplayName("빈 생성 테스트")
    void createBeanTest() throws NoSuchMethodException {
        BeanDefinitionConstructor beanDefinitionConstructor = new BeanDefinitionConstructor(SummerIsKingEmperor.class.getConstructor(String.class));
        assertThat(beanDefinitionConstructor.createBean("starkim kills you")).isInstanceOf(SummerIsKingEmperor.class);
    }

    @Test
    @DisplayName("빈 생성 파라미터 오류 실패 테스트")
    void createBeanTest2() throws NoSuchMethodException {
        BeanDefinitionConstructor beanDefinitionConstructor = new BeanDefinitionConstructor(SummerIsKingEmperor.class.getConstructor(String.class));
        assertThrows(IllegalArgumentException.class, beanDefinitionConstructor::createBean);
    }
}