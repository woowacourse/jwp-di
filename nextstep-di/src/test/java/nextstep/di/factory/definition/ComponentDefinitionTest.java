package nextstep.di.factory.definition;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.NoInjectClass;
import nextstep.exception.InjectMethodNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComponentDefinitionTest {

    @Test
    void generate_bean_test() {
        ComponentDefinition componentDefinition = new ComponentDefinition(JdbcQuestionRepository.class);
        assertNotNull(componentDefinition.generateBean());
    }

    @Test
    void duplicate_Inject_annotation() {
         assertThrows(InjectMethodNotFoundException.class, () ->new ComponentDefinition(NoInjectClass.class));
    }
}
