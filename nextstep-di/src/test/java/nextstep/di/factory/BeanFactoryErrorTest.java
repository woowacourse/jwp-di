package nextstep.di.factory;

import nextstep.di.factory.error.BadBean;
import nextstep.di.factory.error.Circular;
import nextstep.di.factory.error.GoodBean;
import nextstep.di.factory.error.OneConstructorBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryErrorTest {
    private BeanFactory beanFactory;
    private Reflections reflections;

    @BeforeEach
    void setUp() {
        reflections = new Reflections("nextstep.di.factory.error");
    }

    @Test
    void 기본_생성자만_있는_클래스_빈_생성_확인() {
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(GoodBean.class);
        beanFactory = new BeanFactory(types);
        beanFactory.initialize();
        assertNotNull(beanFactory.getBean(OneConstructorBean.class));
    }

    @Test
    void 순환참조_에러_확인() {
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Circular.class);
        beanFactory = new BeanFactory(types);
        assertThatThrownBy(() -> beanFactory.initialize()).isInstanceOf(BeanCreateException.class);
    }

    @Test
    void 여러_생성자_중_결정할_수_없는_경우() {
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(BadBean.class);
        beanFactory = new BeanFactory(types);
        assertThatThrownBy(() -> beanFactory.initialize()).isInstanceOf(BeanCreateException.class);
    }
}
