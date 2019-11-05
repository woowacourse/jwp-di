package nextstep.di.scanner;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BeanScannerTest {

    @Test
    void beanScanner가_모든_Bean을_정상적으로_스캔한다() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");
        assertEquals(beanScanner.getBeans().size(), 4);
    }

    @Test
    void Controller_어노테이션_클래스를_정상적으로_스캔한다() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");
        assertTrue(beanScanner.getBeans().contains(QnaController.class));
    }

    @Test
    void Service_어노테이션_클래스를_정상적으로_스캔한다() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");
        assertTrue(beanScanner.getBeans().contains(QnaController.class));
    }

    @Test
    void Repository_어노테이션_클래스를_정상적으로_스캔한다() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");
        assertTrue(beanScanner.getBeans().contains(JdbcQuestionRepository.class));
        assertTrue(beanScanner.getBeans().contains(JdbcUserRepository.class));
    }
}