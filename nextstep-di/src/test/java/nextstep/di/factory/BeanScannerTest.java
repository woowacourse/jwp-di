package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BeanScannerTest {
    private BeanScanner beanScanner;

    @BeforeEach
    void setUp() {
        beanScanner = new BeanScanner("nextstep.di.factory.example");
    }

    @Test
    void beanScan() {
        BeanCreateMatcher createMatcher = beanScanner.scanBean(Controller.class);
        assertTrue(createMatcher.containsKey(QnaController.class));
    }

    @Test
    void beanScanAll() {
        BeanCreateMatcher beanCreateMatcher = beanScanner.scanBean(Controller.class, Service.class, Repository.class);
        assertTrue(beanCreateMatcher.containsKey(MyQnaService.class));
        assertTrue(beanCreateMatcher.containsKey(JdbcQuestionRepository.class));
        assertTrue(beanCreateMatcher.containsKey(MyJdbcTemplate.class));
    }

    @AfterEach
    void tearDown() {
        beanScanner = null;
    }
}