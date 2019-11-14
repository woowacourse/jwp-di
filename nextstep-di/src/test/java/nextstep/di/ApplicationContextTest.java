package nextstep.di;

import nextstep.di.factory.example.MyQnaService;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationContextTest {

    @Test
    void create() {
        // given
        ApplicationContext ctx = new ApplicationContext(ApplicationContextRoot.class);

        // when & then
        assertThat(ctx.getBean(DataSource.class)).isNotNull();
        assertThat(ctx.getBean(MyQnaService.class)).isNotNull();
    }
}
