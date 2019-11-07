package nextstep.di.factory.instantiation;

import nextstep.di.factory.BeanCreateMatcher;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MethodInstantiationTest {
    private BeanCreateMatcher beanCreateMatcher;
    private Class<?> clazz = IntegrationConfig.class;
    private Object clazzInstance = BeanUtils.instantiateClass(clazz);

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        beanCreateMatcher = new BeanCreateMatcher();
        Method dataSource = clazz.getMethod("dataSource");
        Method jdbcTemplate = clazz.getMethod("jdbcTemplate", DataSource.class);
        beanCreateMatcher.put(DataSource.class, new MethodInstantiation(dataSource, clazzInstance));
        beanCreateMatcher.put(MyJdbcTemplate.class, new MethodInstantiation(jdbcTemplate, clazzInstance));
    }

    @Test
    void getInstance() throws NoSuchMethodException {
        Method jdbcTemplate = IntegrationConfig.class.getMethod("jdbcTemplate", DataSource.class);
        MethodInstantiation methodInstantiation = new MethodInstantiation(jdbcTemplate, clazzInstance);
        assertNotNull(methodInstantiation.getInstance(beanCreateMatcher));
    }

    // TODO: 테스트 통과시켜야 함.
    @Test
    void getSameInstance() throws NoSuchMethodException {
        Method jdbcTemplate = clazz.getMethod("jdbcTemplate", DataSource.class);
        Method dataSource = clazz.getMethod("dataSource");
        MethodInstantiation jdbcMethodInstantiation = new MethodInstantiation(jdbcTemplate, clazzInstance);
        MethodInstantiation dataSourceInstantiation = new MethodInstantiation(dataSource, clazzInstance);

        DataSource expected = ((MyJdbcTemplate) jdbcMethodInstantiation.getInstance(beanCreateMatcher)).getDataSource();
        DataSource target = ((DataSource) dataSourceInstantiation.getInstance(beanCreateMatcher));

        assertEquals(expected, target);
    }
}