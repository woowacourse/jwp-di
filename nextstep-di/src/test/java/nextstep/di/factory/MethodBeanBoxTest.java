package nextstep.di.factory;

import nextstep.di.factory.example.IntegrationConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class MethodBeanBoxTest {

    private static final Logger log = LoggerFactory.getLogger(MethodBeanBoxTest.class);

    @Test
    void hasParams() throws NoSuchMethodException {
        Method method = IntegrationConfig.class.getMethod("jdbcTemplate", DataSource.class);
        BeanBox beanBox = new MethodBeanBox(method);
        assertTrue(beanBox.hasParams());
    }

    @Test
    void hasNoParams() throws NoSuchMethodException {
        Method method = IntegrationConfig.class.getMethod("dataSource");
        BeanBox beanBox = new MethodBeanBox(method);
        assertFalse(beanBox.hasParams());
    }

    @Test
    void getInvokerHasConstructorAndParams() throws NoSuchMethodException {
        Method method = IntegrationConfig.class.getMethod("jdbcTemplate", DataSource.class);
        BeanBox beanBox = new MethodBeanBox(method);
        log.debug("Constructor : {}", beanBox.getInvoker());
        assertNotNull(beanBox.getInvoker());
    }

    @Test
    void getInvokerHasNoConstructor() throws NoSuchMethodException {
        Method method = IntegrationConfig.class.getMethod("dataSource");
        BeanBox beanBox = new MethodBeanBox(method);
        log.debug("Constructor : {}", beanBox.getInvoker());
        assertNotNull(beanBox.getInvoker());
    }

    @Test
    void instantiateWithParameterNotInstantiated() throws NoSuchMethodException {
        Method method = IntegrationConfig.class.getMethod("jdbcTemplate", DataSource.class);
        BeanBox beanBox = new MethodBeanBox(method);
        assertThrows(RuntimeException.class, beanBox::instantiate);
    }

    @Test
    void instantiateHasNoParameter() throws NoSuchMethodException {
        Method method = IntegrationConfig.class.getMethod("dataSource");
        BeanBox beanBox = new MethodBeanBox(method);
        assertNotNull(beanBox.instantiate());
    }

    @Test
    void getParameterCountsWhichMethodHasOne() throws NoSuchMethodException {
        Method method = IntegrationConfig.class.getMethod("jdbcTemplate", DataSource.class);
        BeanBox beanBox = new MethodBeanBox(method);
        assertEquals(beanBox.getParameterCount(), 1);
    }

    @Test
    void getParameterCountsWhichMethodHasNot() throws NoSuchMethodException {
        Method method = IntegrationConfig.class.getMethod("dataSource");
        BeanBox beanBox = new MethodBeanBox(method);
        assertEquals(beanBox.getParameterCount(), 0);
    }
}