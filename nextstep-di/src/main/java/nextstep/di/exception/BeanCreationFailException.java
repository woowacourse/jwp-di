package nextstep.di.exception;

import java.lang.reflect.Constructor;

public class BeanCreationFailException extends RuntimeException {
    public BeanCreationFailException(String s) {
        super(s);
    }

    public static BeanCreationFailException constructWithNotExistParameter(Constructor<?> constructor, Class<?> paramType) {
        Class<?> declaringType = constructor.getDeclaringClass();

        String s = String.format("%s 의 생성자에 필요한 빈(%s)이 존재하지 않습니다. \nconstructor: %s", declaringType, paramType, constructor);

        return new BeanCreationFailException(s);
    }
}
