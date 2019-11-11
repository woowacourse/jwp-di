package nextstep.di.factory.support;

import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.exception.ReflectionUtilException;
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
        assertThrows(ReflectionUtilException.class, () -> ReflectionUtils.newInstance(MyQnaService.class));
    }
}