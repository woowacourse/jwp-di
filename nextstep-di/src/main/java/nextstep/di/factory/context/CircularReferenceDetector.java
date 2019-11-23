package nextstep.di.factory.context;

import java.util.Stack;

public class CircularReferenceDetector {
    private final Stack<Class<?>> referencedClasses = new Stack<>();

    public Class<?> add(Class<?> aClass) {
        if (isReferenced(aClass)) {
            throw new BeanCreateException("순환 참조되는 클래스가 존재합니다 : " + referencedClasses.toString());
        }
        return referencedClasses.push(aClass);
    }

    public Class<?> remove() {
        return referencedClasses.pop();
    }

    private boolean isReferenced(Class<?> aClass) {
        return referencedClasses.search(aClass) >= 0;
    }
}
