package nextstep.supports;

import java.util.List;

public interface ToNodeGenerator<T> {
    List<T> getToNodes(T node);
}
