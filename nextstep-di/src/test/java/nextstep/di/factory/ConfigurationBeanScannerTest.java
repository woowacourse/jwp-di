package nextstep.di.factory;

import nextstep.di.factory.beans.BeanRecipe;
import nextstep.di.factory.beans.ConfigurationBeanScanner;
import nextstep.di.factory.example.ExampleConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationBeanScannerTest {

    @Test
    public void register_simple() {
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(ExampleConfig.class);
        Set<BeanRecipe> scan = cbs.scan();

        assertThat(scan.size()).isEqualTo(2);

        assertThat(hasBeanType(scan, DataSource.class)).isTrue();
        assertThat(hasBeanType(scan, MyJdbcTemplate.class)).isTrue();
    }

    private boolean hasBeanType(Set<BeanRecipe> beanRecipes, Class<?> type) {
        return beanRecipes.stream()
                .anyMatch(beanRecipe -> beanRecipe.getBeanType().equals(type));
    }
}
