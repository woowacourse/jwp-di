package nextstep.di.factory;

import nextstep.exception.CircularReferenceException;

import java.util.Stack;

public class CircularChecker {
    private Stack<Class<?>> referencedClasses = new Stack<>();

    public void check(Class<?> param) {
        if (isReferenced(param)) {
            throw new CircularReferenceException();
        }

        referencedClasses.push(param);
    }

    private boolean isReferenced(Class<?> param) {
        return referencedClasses.search(param) >= 0;
    }

    public void remove() {
        referencedClasses.pop();
    }
}
