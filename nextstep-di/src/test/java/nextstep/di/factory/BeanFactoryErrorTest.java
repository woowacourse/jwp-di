package nextstep.di.factory;

import nextstep.di.factory.beans.noerror.OneConstructorBean;
import nextstep.stereotype.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryErrorTest {
    private BeanFactory beanFactory;
    private Reflections reflections;

    @Test
    void 기본_생성자만_있는_클래스_빈_생성_확인() {
        reflections = new Reflections("nextstep.di.factory.beans.noerror");
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Controller.class);
        beanFactory = new BeanFactory(types);
        beanFactory.initialize();
        assertNotNull(beanFactory.getBean(OneConstructorBean.class));
    }

    @Test
    void 순환참조_에러_확인() {
        reflections = new Reflections("nextstep.di.factory.beans.circular");
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Controller.class);
        beanFactory = new BeanFactory(types);
        assertThatThrownBy(() -> beanFactory.initialize()).isInstanceOf(BeanCreateException.class);
    }

    @Test
    void 여러_생성자_중_결정할_수_없는_경우() {
        reflections = new Reflections("nextstep.di.factory.beans.error");
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Controller.class);
        beanFactory = new BeanFactory(types);
        assertThatThrownBy(() -> beanFactory.initialize()).isInstanceOf(BeanCreateException.class);
    }
}
