package nextstep.di.factory.context;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ApplicationContextTest {
    private ApplicationContext context;

    @Test
    void 컨텍스트_초기화() {
        context = new ApplicationContext(ExampleConfig.class);

        MyJdbcTemplate bean = context.getBean(MyJdbcTemplate.class);
        assertThat(bean).isNotNull();
        assertThat(bean.getDataSource()).isEqualTo(context.getBean(DataSource.class));
    }
}