package nextstep.di.factory.instantiation;

import com.google.common.collect.Maps;
import nextstep.di.factory.BeanCreateMatcher;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QuestionRepository;
import nextstep.di.factory.example.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void getSameInstance() {
        Map<Class<?>, Object> beans = Maps.newHashMap();
        constructorInstantiation = new ConstructorInstantiation(MyQnaService.class);
        Object instance = constructorInstantiation.getInstance(beanCreateMatcher, beans);
        QuestionRepository questionRepository = ((MyQnaService) instance).getQuestionRepository();
        UserRepository userRepository = ((MyQnaService) instance).getUserRepository();

        constructorInstantiation = new ConstructorInstantiation(JdbcUserRepository.class);
        Object target = constructorInstantiation.getInstance(beanCreateMatcher, beans);
        assertEquals(userRepository, target);

        constructorInstantiation = new ConstructorInstantiation(JdbcQuestionRepository.class);
        target = constructorInstantiation.getInstance(beanCreateMatcher, beans);
        assertEquals(questionRepository, target);
    }
}