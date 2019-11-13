package nextstep.di.factory.scanner;

import nextstep.di.factory.BeanCreateMatcher;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassPathBeanScannerTest {
    private ClassPathBeanScanner classPathBeanScanner;
    private BeanCreateMatcher beanCreateMatcher;

    @BeforeEach
    void setUp() {
        beanCreateMatcher = new BeanCreateMatcher();
        classPathBeanScanner = new ClassPathBeanScanner("nextstep.di.factory.example");
    }

    @Test
    void doScan() {
        classPathBeanScanner.doScan(beanCreateMatcher, Controller.class, Service.class, Repository.class);
        assertTrue(beanCreateMatcher.containsKey(QnaController.class));
        assertTrue(beanCreateMatcher.containsKey(MyQnaService.class));
        assertTrue(beanCreateMatcher.containsKey(JdbcQuestionRepository.class));
    }
}