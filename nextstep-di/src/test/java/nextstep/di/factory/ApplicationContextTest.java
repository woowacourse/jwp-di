package nextstep.di.factory;

import nextstep.di.factory.example.*;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApplicationContextTest {
    private ApplicationContext applicationContext;

    @BeforeEach
    public void setUp() {
        applicationContext = new ApplicationContext(IntegrationConfig.class);
        applicationContext.initialize();
    }

    @Test
    public void getBeanTest() {
        assertNotNull(applicationContext.getBean(DataSource.class));
        assertNotNull(applicationContext.getBean(JdbcQuestionRepository.class));

        MyJdbcTemplate jdbcTemplate = applicationContext.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());
    }

    @Test
    public void getTypesAnnotatedWithTest() {
        Set<Class<?>> typesAnnotatedWithController = applicationContext.getTypesAnnotatedWith(Controller.class);
        assertThat(typesAnnotatedWithController).contains(QnaController.class);
        assertThat(typesAnnotatedWithController.size()).isEqualTo(1);

        Set<Class<?>> typesAnnotatedWithService = applicationContext.getTypesAnnotatedWith(Service.class);
        assertThat(typesAnnotatedWithService).contains(MyQnaService.class);
        assertThat(typesAnnotatedWithService.size()).isEqualTo(1);
    }
}
