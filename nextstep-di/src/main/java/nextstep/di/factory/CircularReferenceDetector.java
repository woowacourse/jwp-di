package nextstep.di.factory;

import java.util.Stack;

public class CircularReferenceDetector<T> {
    private final Stack<T> referencedClasses = new Stack<>();

    public T add(T aClass) {
        if (isReferenced(aClass)) {
            throw new BeanCreateException("순환 참조되는 클래스가 존재합니다 : " + referencedClasses.toString());
        }
        return referencedClasses.push(aClass);
    }

    public T remove() {
        return referencedClasses.pop();
    }

    private boolean isReferenced(T aClass) {
        return referencedClasses.search(aClass) >= 0;
    }
}
