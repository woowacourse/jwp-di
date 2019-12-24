package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.BeanAnnotations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassPathBeanScannerTest {
    @DisplayName("ClassPathBeanDefinition을 BeanFactory에 등록")
    @Test
    void register() {
        Set<String> samplePackages = Sets.newHashSet("nextstep.di.factory.example");

        BeanFactory beanFactory = new BeanFactory();
        ClassPathBeanScanner cps = new ClassPathBeanScanner(samplePackages);
        cps.register(beanFactory, BeanAnnotations.getClazz());

        beanFactory.initialize();

        assertThat(beanFactory.getBean(QnaController.class)).isInstanceOf(QnaController.class);
        assertThat(beanFactory.getBean(MyQnaService.class)).isInstanceOf(MyQnaService.class);
        assertThat(beanFactory.getBean(JdbcUserRepository.class)).isInstanceOf(JdbcUserRepository.class);
        assertThat(beanFactory.getBean(JdbcQuestionRepository.class)).isInstanceOf(JdbcQuestionRepository.class);
    }
}
