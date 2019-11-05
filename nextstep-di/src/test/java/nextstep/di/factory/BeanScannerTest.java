package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeanScannerTest {
    @Test
    void getPreInstantiateBeansTest() {
        assertEquals(
                (new BeanScanner("nextstep.di.factory.example")).getPreInstantiateBeans(),
                new HashSet<Class<?>>() {{
                    add(JdbcQuestionRepository.class);
                    add(QnaController.class);
                    add(JdbcUserRepository.class);
                    add(MyQnaService.class);
                }}
        );
    }
}