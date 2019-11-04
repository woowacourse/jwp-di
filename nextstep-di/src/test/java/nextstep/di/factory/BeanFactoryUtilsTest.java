package nextstep.di.factory;

import nextstep.di.factory.error.ExampleBean;
import nextstep.di.factory.error.NoAnnotatedMultiConstructorsBean;
import nextstep.di.factory.error.OneConstructorBean;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

class BeanFactoryUtilsTest {

    @Test
    void 기본생성자_찾아내기() {
        assertThat(BeanFactoryUtils.getInjectedConstructor(OneConstructorBean.class)).isNotNull();
    }

    @Test
    void 기본생성자_생성후_인스턴스_타입확인() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object instance = BeanFactoryUtils.getInjectedConstructor(ExampleBean.class).orElseThrow().newInstance();
        assertThat(instance instanceof ExampleBean).isTrue();
    }

    @Test
    void Inject_어노테이션없이_생성자가_두개인경우_생성자_null확인() {
        assertThat(BeanFactoryUtils.getInjectedConstructor(NoAnnotatedMultiConstructorsBean.class).isEmpty()).isTrue();
    }
}