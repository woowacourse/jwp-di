package nextstep.di.bean;

import nextstep.di.bean.example.IntegrationConfig;
import nextstep.di.bean.example.MyJdbcTemplate;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigBeanScannerTest {
    @Test
    void getConfigBeansToInstantiateTest() {
        final ConfigBeanScanner configBeanScanner = new ConfigBeanScanner(IntegrationConfig.class);
        assertEquals(
                new HashMap<Class<?>, Method>() {{
                    try {
                        put(DataSource.class, IntegrationConfig.class.getDeclaredMethod("dataSource"));
                        put(
                                MyJdbcTemplate.class,
                                IntegrationConfig.class.getDeclaredMethod("jdbcTemplate", DataSource.class)
                        );
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }},
                configBeanScanner.getConfigBeansToInstantiate()
        );
    }
}