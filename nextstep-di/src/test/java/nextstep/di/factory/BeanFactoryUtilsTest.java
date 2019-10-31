package nextstep.di.factory;

import nextstep.di.factory.example.DefaultConstructorClass;
import nextstep.di.factory.example.ErrorBean;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

class BeanFactoryUtilsTest {

    @Test
    void 기본생성자_찾아내기() {
        assertThat(BeanFactoryUtils.getInjectedConstructor(DefaultConstructorClass.class)).isNotNull();
    }

    @Test
    void 기본생성자_생성후_인스턴스_타입확인() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object instance = BeanFactoryUtils.getInjectedConstructor(DefaultConstructorClass.class).orElseThrow().newInstance();
        assertThat(instance instanceof DefaultConstructorClass).isTrue();
    }

    @Test
    void Inject_어노테이션없이_생성자가_두개인경우_생성자_null확인() {
        assertThat(BeanFactoryUtils.getInjectedConstructor(ErrorBean.class).isEmpty()).isTrue();
    }
}