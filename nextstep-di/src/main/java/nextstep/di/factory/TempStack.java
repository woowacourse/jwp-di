package nextstep.di.factory;

import java.util.ArrayDeque;
import java.util.Deque;

public class TempStack {
    private Deque<Class<?>> deque = new ArrayDeque<>();

    public Object temp() {
        return null;
    }
}
