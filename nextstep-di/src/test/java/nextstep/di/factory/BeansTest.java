package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeansTest {

    private Beans beans;

    @BeforeEach
    public void setUp() {
        beans = new Beans();
        beans.put(JdbcUserRepository.class, new JdbcUserRepository());
    }

    @Test
    public void superGet() {
        assertThat(beans.get(JdbcUserRepository.class, JdbcUserRepository::new))
                .isInstanceOf(JdbcUserRepository.class);
    }

    @Test
    public void superGet2() {
        assertThat(beans.get(JdbcQuestionRepository.class, JdbcQuestionRepository::new))
                .isInstanceOf(JdbcQuestionRepository.class);
    }
}