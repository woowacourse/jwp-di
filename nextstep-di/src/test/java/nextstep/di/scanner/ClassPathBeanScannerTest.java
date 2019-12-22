package nextstep.di.scanner;

import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.BeanAnnotations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassPathBeanScannerTest {
    @DisplayName("ClassPathBeanDefinition을 BeanFactory에 등록")
    @Test
    void register() {
        BeanFactory beanFactory = new BeanFactory();
        ClassPathBeanScanner cps = new ClassPathBeanScanner("nextstep.di.factory.example");
        cps.register(beanFactory, BeanAnnotations.getClazz());

        beanFactory.initialize();

        assertThat(beanFactory.getBean(QnaController.class)).isInstanceOf(QnaController.class);
        assertThat(beanFactory.getBean(MyQnaService.class)).isInstanceOf(MyQnaService.class);
        assertThat(beanFactory.getBean(JdbcUserRepository.class)).isInstanceOf(JdbcUserRepository.class);
        assertThat(beanFactory.getBean(JdbcQuestionRepository.class)).isInstanceOf(JdbcQuestionRepository.class);
    }
}
