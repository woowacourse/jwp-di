package nextstep.di.factory.definition;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanDefinitionMethodTest {

    @Test
    @DisplayName("파라미터가 없는 메서드의 getParameters() 테스트")
    void getParametersTest() throws NoSuchMethodException {
        BeanDefinitionMethod beanDefinitionMethod = new BeanDefinitionMethod(KingbbodeIsRealKingGodGeneralEmperor.class.getMethod("hiSummer"));
        assertThat(beanDefinitionMethod.getParameterTypes()).isEqualTo(new Class[]{KingbbodeIsRealKingGodGeneralEmperor.class});
    }

    @Test
    @DisplayName("파라미터가 있는 메서드의 getParameters() 테스트")
    void getParametersTest2() throws NoSuchMethodException {
        BeanDefinitionMethod beanDefinitionMethod = new BeanDefinitionMethod(KingbbodeIsRealKingGodGeneralEmperor.class.getMethod("hiSummerWithParams", String.class));
        assertThat(beanDefinitionMethod.getParameterTypes()).isEqualTo(new Class[]{KingbbodeIsRealKingGodGeneralEmperor.class, String.class});
    }

    @Test
    @DisplayName("빈 생성 테스트")
    void createBeanTest() throws NoSuchMethodException {
        BeanDefinitionMethod beanDefinitionMethod = new BeanDefinitionMethod(KingbbodeIsRealKingGodGeneralEmperor.class.getMethod("hiSummerWithParams", String.class));
        KingbbodeIsRealKingGodGeneralEmperor kingbbodeIsRealKingGodGeneralEmperor = new KingbbodeIsRealKingGodGeneralEmperor();

        Object hello1 = beanDefinitionMethod.createBean(kingbbodeIsRealKingGodGeneralEmperor, "hello");
        assertThat(hello1).isInstanceOf(String.class);
    }

    @Test
    @DisplayName("빈 생성 파라미터 오류 실패 테스트")
    void createBeanTest2() throws NoSuchMethodException {
        BeanDefinitionMethod beanDefinitionMethod = new BeanDefinitionMethod(KingbbodeIsRealKingGodGeneralEmperor.class.getMethod("hiSummerWithParams", String.class));
        KingbbodeIsRealKingGodGeneralEmperor kingbbodeIsRealKingGodGeneralEmperor = new KingbbodeIsRealKingGodGeneralEmperor();

        assertThrows(IllegalArgumentException.class, () -> beanDefinitionMethod.createBean(kingbbodeIsRealKingGodGeneralEmperor));
    }

}