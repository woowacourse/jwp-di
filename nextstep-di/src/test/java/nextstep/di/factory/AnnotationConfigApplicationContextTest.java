package nextstep.di.factory;

import nextstep.di.factory.example.*;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnnotationConfigApplicationContextTest {

    @Test
    public void integrationConfigTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(IntegrationConfig.class);

        DataSource dataSource = (DataSource) context.getBean(DataSource.class);
        assertNotNull(dataSource);

        MyJdbcTemplate jdbcTemplate = (MyJdbcTemplate) context.getBean(MyJdbcTemplate.class);
        assertNotNull(jdbcTemplate);
        assertNotNull(jdbcTemplate.getDataSource());

        assertThat(jdbcTemplate.getDataSource()).isEqualTo(dataSource);
    }

    @Test
    public void componentToBeanWiringTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ComponentScanConfig.class);

        QnaController qnaController = (QnaController) context.getBean(QnaController.class);
        assertNotNull(qnaController);
    }

    @Test
    public void beanToComponentWiringTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ComponentScanConfig.class);

        BeanInjectedComponent component = (BeanInjectedComponent) context.getBean(BeanInjectedComponent.class);
        assertNotNull(component);
        assertNotNull(component.getDataSource());
        assertThat(component.getDataSource()).isEqualTo(context.getBean(DataSource.class));
    }
}
