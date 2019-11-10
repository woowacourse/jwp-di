package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScannerTest {

    private BeanScanner beanScanner;

    @BeforeEach
    void setup() {
        beanScanner=new BeanScanner("nextstep.di.factory.example");
    }

    @Test
    void getPreInstanticateClass() {
        Set<Class<?>> preInstantiateClass = beanScanner.getPreInstanticateClass();
        Set<Class<?>> beans = Sets.newHashSet(QnaController.class, MyQnaService.class,
            JdbcQuestionRepository.class, JdbcUserRepository.class);

        assertThat(beans).isEqualTo(preInstantiateClass);
    }
}