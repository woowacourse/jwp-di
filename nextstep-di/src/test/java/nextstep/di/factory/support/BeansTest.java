package nextstep.di.factory.support;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.exception.BeanNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeansTest {

    private Beans beans;
    private JdbcUserRepository jdbcUserRepository;

    @BeforeEach
    public void setUp() {
        beans = new Beans();
        jdbcUserRepository = new JdbcUserRepository();
        beans.put(JdbcUserRepository.class, () -> jdbcUserRepository);
    }

    @Test
    public void get() {
        assertThat(beans.get(JdbcUserRepository.class))
                .isInstanceOf(JdbcUserRepository.class);
    }

    @Test
    public void getIfNotExists() {
        assertThrows(BeanNotExistException.class, () -> beans.get(QnaController.class));
    }

    @Test
    public void instantiate() {
        beans.put(JdbcUserRepository.class, JdbcUserRepository::new);
        assertThat(beans.get(JdbcUserRepository.class)).isEqualTo(jdbcUserRepository);
    }

    @Test
    public void instantiateIfNotExist() {
        beans.put(JdbcQuestionRepository.class, JdbcQuestionRepository::new);
        assertThat(beans.get(JdbcQuestionRepository.class)).isInstanceOf(JdbcQuestionRepository.class);
    }
}