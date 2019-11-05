package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanScannerTest {
    @Test
    public void getTypesAnnotatedWithTest() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");

        Set<Class<?>> typesAnnotatedWithController = beanScanner.getTypesAnnotatedWith(Controller.class);
        assertThat(typesAnnotatedWithController).contains(QnaController.class);
        assertThat(typesAnnotatedWithController).doesNotContain(MyQnaService.class);

        Set<Class<?>> typesAnnotatedWithService = beanScanner.getTypesAnnotatedWith(Service.class);
        assertThat(typesAnnotatedWithService).contains(MyQnaService.class);
        assertThat(typesAnnotatedWithService).doesNotContain(JdbcQuestionRepository.class);
    }
}