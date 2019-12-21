package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.JdbcQuestionRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextTest {
    @Test
    void 컴포넌트_스캔(){
        ApplicationContext applicationContext = new ApplicationContext(ExampleConfig.class);
        assertThat(applicationContext.getBean(JdbcQuestionRepository.class)).isNotNull();
    }
}