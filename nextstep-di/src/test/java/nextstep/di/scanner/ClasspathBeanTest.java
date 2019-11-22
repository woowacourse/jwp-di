package nextstep.di.scanner;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class ClasspathBeanTest {
    private static final Logger log = LoggerFactory.getLogger(ClasspathBeanTest.class);

    private ClasspathBean classpathBean;

    @BeforeEach
    void setUp() {
        classpathBean = new ClasspathBean(MyQnaService.class);
    }

    @Test
    @DisplayName("파라미터가 있는 생성자의 파라미터 수")
    void getParameterTypes() {
        Class[] parameterType = classpathBean.getParameterTypes();
        log.debug("parameterTypes:{}", (Object) parameterType);
        assertThat(parameterType.length).isEqualTo(2);
    }

    @Test
    @DisplayName("파라미터가 없는 생성자")
    void getParameterTypes_notParams() {
        classpathBean = new ClasspathBean(JdbcQuestionRepository.class);
        Class[] parameterType = classpathBean.getParameterTypes();
        log.debug("parameterTypes_notParams:{}", (Object) parameterType);
        assertThat(parameterType.length).isEqualTo(0);
    }

    @Test
    @DisplayName("파라미터가 있는 생성자의 인스턴스화")
    void getInstance() {
        Object[] params = {new JdbcUserRepository(new BasicDataSource()), new JdbcQuestionRepository()};
        assertThat(classpathBean.getInstance(params).getClass()).isEqualTo(MyQnaService.class);
    }

    @Test
    @DisplayName("파라미터가 없는 생성자의 인스턴스화")
    void getInstance_notParams() {
        classpathBean = new ClasspathBean(JdbcQuestionRepository.class);
        Object[] params = {};
        assertThat(classpathBean.getInstance(params).getClass()).isEqualTo(JdbcQuestionRepository.class);
    }
}