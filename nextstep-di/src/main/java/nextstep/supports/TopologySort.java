package nextstep.supports;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.List;
import java.util.Set;


public class TopologySort<T> {
    private final Set<T> inputNodes;
    private final ToNodeGenerator<T> toNodeGenerator;
    private final CycleErrorHandler cycleErrorHandler;

    private List<T> orders;

    public TopologySort(Set<T> inputNodes, ToNodeGenerator<T> toNodeGenerator, CycleErrorHandler cycleErrorHandler) {
        this.inputNodes = inputNodes;
        this.toNodeGenerator = toNodeGenerator;
        this.cycleErrorHandler = cycleErrorHandler;

        calculate();
    }

    private void calculate() {
        final Set<T> visited = Sets.newHashSet();
        final Set<T> finished = Sets.newHashSet();
        orders = Lists.newArrayList();

        for (T root : inputNodes) {
            recursive(root, visited, finished);
        }

        Collections.reverse(orders);
    }

    private void recursive(T node, Set<T> visited, Set<T> finished) {
        handleIfCycleExist(node, visited, finished);

        if (visited.contains(node)) {
            return;
        }

        visited.add(node);

        for (T toNode : toNodeGenerator.getToNodes(node)) {
            recursive(toNode, visited, finished);
        }

        finished.add(node);
        orders.add(node);
    }

    private void handleIfCycleExist(T node, Set<T> visited, Set<T> finished) {
        if (visited.contains(node) && !finished.contains(node)) {
            cycleErrorHandler.errorHandle();
        }
    }

    public List<T> calculateReversedOrders() {
        List<T> copy = copy();
        Collections.reverse(copy);

        return copy;
    }

    // 지금은 외부에서 orders 요소들의 값을 바꾸지 않는다는 가정이 있음
    // 그래서 reference 만 복사를 해도 괜찮음
    private List<T> copy() {
        List<T> copy = Lists.newArrayList();
        copy.addAll(orders);

        return orders;
    }
}