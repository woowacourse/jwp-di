package nextstep.di.factory.support;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.stereotype.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SupportedClassTest {
    private SupportedClass supportedClass;

    @BeforeEach
    public void setUp() {
        supportedClass = new SupportedClass();
    }

    @Test
    public void getClassByAnnotationTest() {
        supportedClass.addSupportedClass(JdbcQuestionRepository.class);
        supportedClass.addSupportedClass(JdbcUserRepository.class);

        assertThat(supportedClass.getClassByAnnotation(Repository.class))
                .contains(JdbcQuestionRepository.class)
                .contains(JdbcUserRepository.class);
    }
}