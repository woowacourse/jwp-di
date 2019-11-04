package nextstep.supports;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;


public class TopologySort<T> {
    private final Set<T> inputNodes;
    private final ToNodeGenerator<T> toNodeGenerator;
    private final CycleErrorHandler cycleErrorHandler;

    private final Set<T> visited;
    private final Set<T> finished;


    public TopologySort(Set<T> inputNodes, ToNodeGenerator<T> toNodeGenerator, CycleErrorHandler cycleErrorHandler) {
        this.inputNodes = inputNodes;
        this.toNodeGenerator = toNodeGenerator;
        this.cycleErrorHandler = cycleErrorHandler;

        visited = Sets.newHashSet();
        finished = Sets.newHashSet();
    }

    public List<T> calculateReversedOrders() {
        List<T> orders = Lists.newArrayList();

        for (T root : inputNodes) {
            recursive(orders, root);
        }
        return orders;
    }

    private void recursive(List<T> orders, T node) {
        handleIfCycleExist(node);

        if (visited.contains(node)) {
            return;
        }

        visited.add(node);

        for (T toNode : toNodeGenerator.getToNodes(node)) {
            recursive(orders, toNode);
        }

        finished.add(node);
        orders.add(node);
    }

    private void handleIfCycleExist(T node) {
        if (visited.contains(node) && !finished.contains(node)) {
            cycleErrorHandler.errorHandle();
        }
    }
}