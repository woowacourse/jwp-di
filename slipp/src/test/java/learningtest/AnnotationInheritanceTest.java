package learningtest;

import com.google.common.collect.Sets;
import learningtest.annotations.Child;
import learningtest.annotations.Parent;
import learningtest.targets.ImplementationOfInterfeceWithParent;
import learningtest.targets.InterfaceWithParent;
import learningtest.targets.TargetWithChild;
import learningtest.targets.TargetWithParent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationInheritanceTest {
    private final Reflections reflections = new Reflections("learningtest");

    @Test
    @DisplayName("특정 어노테이션을 상속하는 어노테이션 검사하기")
    void test() {
        // 말 그대로.. 어노테이션이 달린 인터페이스들을 가져온다.
        System.out.println(Arrays.toString(ImplementationOfInterfeceWithParent.class.getAnnotatedInterfaces()));

        System.out.println(Arrays.toString(TargetWithParent.class.getAnnotations()));
        System.out.println(Arrays.toString(TargetWithParent.class.getDeclaredAnnotations()));
    }

    @Test
    @DisplayName("Reflections 을 통해서 @Parent 을 표시한 모든 클래스 찾기")
    void test2() {
        System.out.println(reflections.getTypesAnnotatedWith(Child.class));
        System.out.println(reflections.getTypesAnnotatedWith(Parent.class));

        Set<Class<?>> classesIncludeParentAnnotation = reflections.getTypesAnnotatedWith(Parent.class).stream()
                .filter(clazz -> !clazz.isAnnotation())
                .filter(clazz -> !clazz.isInterface())
                .collect(Collectors.toSet());
        System.out.println(classesIncludeParentAnnotation);
    }

    @Test
    @DisplayName("Reflections.getTypesAnnotatedWith() 확인하기")
    void getTypesAnnotatedWith() {
        Set<Class<?>> expectedTargetsExactlyAnnotatedWithParent = Sets.newHashSet(Arrays.asList(
                TargetWithParent.class,
                InterfaceWithParent.class,
                ImplementationOfInterfeceWithParent.class
        ));

        Reflections reflections = new Reflections("learningtest.targets");
        assertThat(reflections.getTypesAnnotatedWith(Parent.class)).isEqualTo(expectedTargetsExactlyAnnotatedWithParent);

        // Reflections 에 관계있는 어노테이션이 있는 패키지를 포함시켜야지 해당 어노테이션을 포함하는 클래스도 찾아줌
        // ex. @Child 가 @Parent 를 포함하는데, annotations 패키지도 포함 시켜야 이 정보가 반영됨
        Set<Class<?>> expectedTargetsIncludingParent = Sets.newHashSet(Arrays.asList(
                TargetWithParent.class,
                InterfaceWithParent.class,
                ImplementationOfInterfeceWithParent.class,
                TargetWithChild.class,
                Child.class
        ));

        Reflections reflectionsWithAnnotations = new Reflections("learningtest.targets", "learningtest.annotations");
        assertThat(reflectionsWithAnnotations.getTypesAnnotatedWith(Parent.class)).isEqualTo(expectedTargetsIncludingParent);


    }

    private <T extends Annotation> T retrieveAnnotation(Class<T> annotationClass, Class<?> target) {
        T annotation = target.getAnnotation(annotationClass);
        if (annotation == null) {
            throw new NullPointerException("해당 어노테이션이 존재하지 않습니다. annotationClass: " + annotationClass);
        }

        return annotation;
    }
}
