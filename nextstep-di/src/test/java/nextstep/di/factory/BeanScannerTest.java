package nextstep.di.factory;

import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScannerTest {
    private static final String NEXTSTEP_DI_FACTORY_EXAMPLE = "nextstep.di.factory.example";

    @Test
    void getPreInstanticateBeans() {
        BeanScanner scanner = new BeanScanner(NEXTSTEP_DI_FACTORY_EXAMPLE);
        Set<Class<?>> preInstanticateBeans = scanner.getPreInstanticateBeans();

        assertThat(preInstanticateBeans.size()).isEqualTo(4);
        assertThat(preInstanticateBeans.contains(QnaController.class)).isTrue();
        assertThat(preInstanticateBeans.contains(MyJdbcTemplate.class)).isFalse();
    }
}