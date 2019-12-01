package nextstep.di.factory;

import nextstep.di.factory.example.repository.JdbcUserRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstructorReflectionTest {

    @Test
    void checkEmptyConstructorTest() {
        assertThat(JdbcUserRepository.class.getDeclaredConstructors().length).isEqualTo(1);
    }

}
