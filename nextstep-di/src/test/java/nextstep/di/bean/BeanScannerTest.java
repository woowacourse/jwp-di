package nextstep.di.bean;

import nextstep.di.bean.example.*;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeanScannerTest {
    @Test
    void getClasspathBeansToInstantiateTest() {
        assertEquals(
                new HashSet<Class<?>>() {{
                    add(JdbcQuestionRepository.class);
                    add(QnaController.class);
                    add(JdbcUserRepository.class);
                    add(MyQnaService.class);
                }},
                (new BeanScanner("nextstep.di.bean.example")).getClasspathBeansToInstantiate()
        );
    }

    @Test
    void getConfigBeansToInstantiateTest() {
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
                (new BeanScanner("nextstep.di.bean.example")).getConfigBeansToInstantiate()
        );
    }
}