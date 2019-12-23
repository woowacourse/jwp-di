package nextstep.di.factory;

import nextstep.di.factory.anotherpackage.Config;
import nextstep.di.factory.circularreference.CircularConfig;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.notbean.NotBean;
import nextstep.exception.BeanNotFoundException;
import nextstep.exception.CircularReferenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextTest {
    private ApplicationContext applicationContext;

    @Test
    void initialize() {
        applicationContext = new ApplicationContext(IntegrationConfig.class);
        applicationContext.initialize();
        assertNotNull(applicationContext.getBean(QnaController.class));
        assertNotNull(applicationContext.getBean(MyQnaService.class));
        assertNotNull(applicationContext.getBean(MyJdbcTemplate.class));
        assertNotNull(applicationContext.getBean(DataSource.class));
    }

    @Test
    @DisplayName("다른 패키지의 빈을 찾으려고 할 때 ")
    void another_Bean() {
        applicationContext = new ApplicationContext(Config.class);
        applicationContext.initialize();
        assertThrows(BeanNotFoundException.class, () -> applicationContext.getBean(MyQnaService.class));
    }

    @Test
    @DisplayName("순환 참조 일 때")
    void circular_reference() {
        applicationContext = new ApplicationContext(CircularConfig.class);
        assertThrows(CircularReferenceException.class, () -> applicationContext.initialize());
    }
}
