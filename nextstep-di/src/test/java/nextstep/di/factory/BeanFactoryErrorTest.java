package nextstep.di.factory;

import nextstep.di.factory.beans.noerror.OneConstructorBean;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryErrorTest {
    private ApplicationContext applicationContext;

    @Test
    void 기본_생성자만_있는_클래스_빈_생성_확인() {
        applicationContext = new ApplicationContext();
        applicationContext.scan("nextstep.di.factory.beans.noerror");
        applicationContext.initialize();
        assertNotNull(applicationContext.getBean(OneConstructorBean.class));
    }

    @Test
    void 순환참조_에러_확인() {
        applicationContext = new ApplicationContext();
        assertThatThrownBy(() -> applicationContext.scan("nextstep.di.factory.beans.circular"))
                .isInstanceOf(BeanCreateException.class);
    }

    @Test
    void 여러_생성자_중_결정할_수_없는_경우() {
        applicationContext = new ApplicationContext();
        assertThatThrownBy(() -> applicationContext.scan("nextstep.di.factory.beans.error"))
                .isInstanceOf(BeanCreateException.class);
    }
}
