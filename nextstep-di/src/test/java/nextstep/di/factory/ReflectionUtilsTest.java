package nextstep.di.factory;

import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReflectionUtilsTest {

    @Test
    public void newInstance() {
        assertThat(ReflectionUtils.newInstance(JdbcUserRepository.class)).isInstanceOf(JdbcUserRepository.class);
    }

    @Test
    public void newInstanceThrowsRuntimeException() {
        assertThrows(RuntimeException.class, () -> ReflectionUtils.newInstance(MyQnaService.class));
    }
}