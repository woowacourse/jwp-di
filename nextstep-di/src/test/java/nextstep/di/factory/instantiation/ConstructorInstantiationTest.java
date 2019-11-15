package nextstep.di.factory.instantiation;

import com.google.common.collect.Maps;
import nextstep.di.factory.BeanCreateMatcher;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcQuestionWithJdbcRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.config.IntegrationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConstructorInstantiationTest {
    private ConstructorInstantiation constructorInstantiation;
    private BeanCreateMatcher beanCreateMatcher;

    @BeforeEach
    void setUp() {
        beanCreateMatcher = new BeanCreateMatcher();
        beanCreateMatcher.put(MyQnaService.class, new ConstructorInstantiation(MyQnaService.class));
        beanCreateMatcher.put(JdbcUserRepository.class, new ConstructorInstantiation(JdbcUserRepository.class));
        beanCreateMatcher.put(JdbcQuestionRepository.class, new ConstructorInstantiation(JdbcQuestionRepository.class));
    }

    @Test
    void getInstance() {
        constructorInstantiation = new ConstructorInstantiation(MyQnaService.class);
        assertNotNull(constructorInstantiation.getInstance(beanCreateMatcher, Maps.newHashMap()));
    }

    @Test
    void getInstanceWithMethodInstantiation() throws NoSuchMethodException {
        Class<?> clazz = IntegrationConfig.class;
        Object clazzInstance = BeanUtils.instantiateClass(clazz);
        Method dataSource = clazz.getMethod("dataSource");
        Method jdbcTemplate = clazz.getMethod("jdbcTemplate", DataSource.class);
        beanCreateMatcher.put(DataSource.class, new MethodInstantiation(dataSource, clazzInstance));
        beanCreateMatcher.put(MyJdbcTemplate.class, new MethodInstantiation(jdbcTemplate, clazzInstance));
        beanCreateMatcher.put(JdbcQuestionWithJdbcRepository.class, new ConstructorInstantiation(JdbcQuestionWithJdbcRepository.class));

        constructorInstantiation = new ConstructorInstantiation(JdbcQuestionWithJdbcRepository.class);
        assertNotNull(constructorInstantiation.getInstance(beanCreateMatcher, Maps.newHashMap()));
    }
}