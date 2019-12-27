package nextstep.di.factory;

import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConfigurationBeanScannerTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() throws Exception {
        String path = "nextstep.di.factory.example";
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(path);
        BeanScanner beanScanner = new BeanScanner(Arrays.asList(Controller.class, Service.class, Repository.class), path);

        Set<Class<?>> allClazz = new HashSet<>();
        allClazz.addAll(configurationBeanScanner.scanBeans());
        allClazz.addAll(beanScanner.scanBeans());

        beanFactory = new BeanFactory(allClazz);
        beanFactory.initialize();
    }

    @Test
    public void register_simple() {
        assertNotNull(beanFactory.getBean(DataSource.class));
        assertNotNull(beanFactory.getBean(MyJdbcTemplate.class));
        assertNotNull(beanFactory.getBean(MyQnaService.class));
    }

}