package nextstep.di;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

public class ConfigurationTest {
    @Test
    void name() {
        Reflections reflections = new Reflections("nextstep.di.factory");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Configuration.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            for (Method method : aClass.getMethods()) {
                boolean annotationPresent = method.isAnnotationPresent(Bean.class);
                if (annotationPresent) {
                    System.out.println("method = " + method);
                }
            }
        }
    }
}
