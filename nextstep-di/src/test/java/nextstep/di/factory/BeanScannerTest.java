package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.IntegrationConfig;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScannerTest {
    private static final String NEXTSTEP_DI_FACTORY_EXAMPLE = "nextstep.di.factory.example";

    @Test
    void 빈어노테이션이_달린_메서드_스캔_테스트() throws NoSuchMethodException {
        BeanScanner beanScanner = new BeanScanner(NEXTSTEP_DI_FACTORY_EXAMPLE);
        Set<Method> beanMethods = beanScanner.getPreInstanticateBeanMethods();

        Set<Method> expected = Sets.newHashSet();
        expected.add(ExampleConfig.class.getMethod("dataSource"));
        expected.add(IntegrationConfig.class.getMethod("dataSource"));
        expected.add(IntegrationConfig.class.getMethod("jdbcTemplate", DataSource.class));

        assertThat(beanMethods.size()).isEqualTo(expected.size());
        assertThat(beanMethods.containsAll(expected)).isTrue();
    }
}