package learningtest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Target 이 array 를 받고 있어서 이렇게 전달 하는 듯
@Target({ElementType.TYPE})
// 존재해야지 vm 에 의해서 해석됨. (reflect 를 통해서 사용할 수 있음)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parent {
    String name() default "default name";
    String message() default "default message";
}
