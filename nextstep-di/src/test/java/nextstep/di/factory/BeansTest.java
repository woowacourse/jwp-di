package nextstep.di.factory;

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

    @BeforeEach
    public void setUp() {
        beans = new Beans();
        beans.put(JdbcUserRepository.class, new JdbcUserRepository());
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
        assertThat(beans.instantiate(JdbcUserRepository.class, JdbcUserRepository::new))
                .isInstanceOf(JdbcUserRepository.class);
    }

    @Test
    public void instantiateIfNotExist() {
        assertThat(beans.instantiate(JdbcQuestionRepository.class, JdbcQuestionRepository::new))
                .isInstanceOf(JdbcQuestionRepository.class);
    }
}