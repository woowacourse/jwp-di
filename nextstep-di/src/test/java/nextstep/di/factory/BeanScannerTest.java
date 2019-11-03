package nextstep.di.factory;

import nextstep.BeanScanner;
import nextstep.di.factory.example.*;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeanScannerTest {

    private BeanScanner beanScanner = new BeanScanner("nextstep.di.factory");
    private Set<Class<?>> scan = beanScanner.scan(Controller.class, Service.class, Repository.class);

    @ParameterizedTest
    @ValueSource(classes = {QnaController.class, MyQnaService.class, JdbcUserRepository.class, JdbcQuestionRepository.class})
    void 스캔_목록에_존재(Class<?> clazz) {
        assertTrue(scan.contains(clazz));
    }

    @ParameterizedTest
    @ValueSource(classes = {TestController.class, TestService.class, TestRepository.class, QuestionRepository.class, UserRepository.class})
    void 스캔_목록에_존재하지_않음(Class<?> clazz) {
        assertFalse(scan.contains(clazz));
    }
}
