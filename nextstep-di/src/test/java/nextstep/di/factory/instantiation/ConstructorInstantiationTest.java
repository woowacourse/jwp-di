package nextstep.di.factory.instantiation;

import nextstep.di.factory.BeanCreateMatcher;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertNotNull(constructorInstantiation.getInstance(beanCreateMatcher));
    }

    @AfterEach
    void tearDown() {
        constructorInstantiation = null;
    }
}