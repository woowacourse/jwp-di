package nextstep.di.factory;

import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationContextTest {
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        applicationContext = new ApplicationContext(IntegrationConfig.class);
    }

    @Test
    void initialize() {
        Set<Class<?>> preInstanticateClazz = applicationContext.initialize();
        assertTrue(preInstanticateClazz.contains(QnaController.class));
        assertTrue(preInstanticateClazz.contains(MyQnaService.class));
        assertTrue(preInstanticateClazz.contains(MyJdbcTemplate.class));
        assertTrue(preInstanticateClazz.contains(DataSource.class));
    }
}
