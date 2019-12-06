package nextstep.di.factory;

import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationContextTest {
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        applicationContext = new ApplicationContext(IntegrationConfig.class);
    }

    @Test
    void initialize() {
        applicationContext.initialize();
        assertNotNull(applicationContext.getBean(QnaController.class));
        assertNotNull(applicationContext.getBean(MyQnaService.class));
        assertNotNull(applicationContext.getBean(MyJdbcTemplate.class));
        assertNotNull(applicationContext.getBean(DataSource.class));
    }
}
