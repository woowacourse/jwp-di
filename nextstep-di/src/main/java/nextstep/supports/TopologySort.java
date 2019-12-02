package nextstep.supports;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;


public class TopologySort<T> {
    private final ToNodeGenerator<T> toNodeGenerator;
    private final CycleErrorHandler cycleErrorHandler;

    private Set<T> visited;
    private Set<T> finished;

    public TopologySort(ToNodeGenerator<T> toNodeGenerator, CycleErrorHandler cycleErrorHandler) {
        this.toNodeGenerator = toNodeGenerator;
        this.cycleErrorHandler = cycleErrorHandler;
    }

    public List<T> calculateReversedOrders(Set<T> inputNodes) {
        List<T> orders = initialize();

        for (T root : inputNodes) {
            recursive(orders, root);
        }
        return orders;
    }

    private List<T> initialize() {
        this.visited = Sets.newHashSet();
        this.finished = Sets.newHashSet();
        return Lists.newArrayList();
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