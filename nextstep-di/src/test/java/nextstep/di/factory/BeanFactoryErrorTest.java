package nextstep.di.factory;

import nextstep.di.factory.beans.noerror.OneConstructorBean;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryErrorTest {
    private ApplicationContext beanFactory;

    @Test
    void 기본_생성자만_있는_클래스_빈_생성_확인() {
        beanFactory = new ApplicationContext();
        beanFactory.scan("nextstep.di.factory.beans.noerror");
        beanFactory.initialize();
        assertNotNull(beanFactory.getBean(OneConstructorBean.class));
    }

    @Test
    void 순환참조_에러_확인() {
        beanFactory = new ApplicationContext();
        assertThatThrownBy(() -> beanFactory.scan("nextstep.di.factory.beans.circular"))
                .isInstanceOf(BeanCreateException.class);
    }

    @Test
    void 여러_생성자_중_결정할_수_없는_경우() {
        beanFactory = new ApplicationContext();
        assertThatThrownBy(() -> beanFactory.scan("nextstep.di.factory.beans.error"))
                .isInstanceOf(BeanCreateException.class);
    }
}
