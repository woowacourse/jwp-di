package nextstep.supports;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TopologySortTest {
    @DisplayName("2개짜리 사이클")
    @Test
    void calculateReversedOrders() {
        final Node[] nodes = {
                new Node(0, new int[]{1}),
                new Node(1, new int[]{0}),
        };
        TopologySort<Node> topologySort = createTopologySort(nodes);

        assertThrows(RuntimeException.class, () -> topologySort.calculateReversedOrders());
    }

    @DisplayName("3개짜리 사이클")
    @Test
    void calculateReversedOrders_() {
        final Node[] nodes = {
                new Node(0, new int[]{1}),
                new Node(1, new int[]{2}),
                new Node(2, new int[]{0}),
        };
        TopologySort<Node> topologySort = createTopologySort(nodes);

        assertThrows(RuntimeException.class, () -> topologySort.calculateReversedOrders());
    }

    @DisplayName("부모가 여러명인 경우")
    @Test
    void calculateReversedOrders_hasParents() {
        // Arrange
        final Node[] nodes = {
                new Node(0, new int[]{2}),
                new Node(1, new int[]{2}),
                new Node(2, new int[]{}),
        };
        TopologySort<Node> topologySort = createTopologySort(nodes);

        List<Node> reversedOrders = topologySort.calculateReversedOrders();

        // Assert
        assertReversedOrders(nodes, reversedOrders);
    }

    @DisplayName("자식이 여러명인 경우")
    @Test
    void calculateReversedOrders_hasChildren() {
        // Arrange
        final Node[] nodes = {
                new Node(0, new int[]{1, 2}),
                new Node(1, new int[]{}),
                new Node(2, new int[]{})
        };
        TopologySort<Node> topologySort = createTopologySort(nodes);

        List<Node> reversedOrders = topologySort.calculateReversedOrders();

        // Assert
        assertReversedOrders(nodes, reversedOrders);
    }

    @DisplayName("두 그룹의 그래프인 경우")
    @Test
    void calculateReversedOrders_separatedGroups() {
        // Arrange
        final Node[] nodes = {
                // group1
                new Node(0, new int[]{2}),
                new Node(1, new int[]{2}),
                new Node(2, new int[]{}),
                // group2
                new Node(3, new int[]{4, 5}),
                new Node(4, new int[]{}),
                new Node(5, new int[]{})

        };
        TopologySort<Node> topologySort = createTopologySort(nodes);

        List<Node> reversedOrders = topologySort.calculateReversedOrders();

        // Assert
        assertReversedOrders(nodes, reversedOrders);
    }

    private void assertReversedOrders(Node[] nodes, List<Node> reversedOrders) {
        for (Node from : nodes) {
            for (Node to : getNodeToNodeGenerator(nodes).getToNodes(from)) {
                // from 이 to 보다 먼저오는지
                assertThat(getIdx(reversedOrders, from) > getIdx(reversedOrders, to)).isTrue();
            }
        }
    }

    private int getIdx(List<Node> nodes, Node node) {
        return nodes.indexOf(node);
    }

    private TopologySort<Node> createTopologySort(Node[] nodes) {
        return new TopologySort<>(
                Sets.newHashSet(Arrays.asList(nodes)),
                getNodeToNodeGenerator(nodes),
                getErrorHandler()
        );
    }

    private CycleErrorHandler getErrorHandler() {
        return () -> {
            throw new RuntimeException("빵");
        };
    }

    private ToNodeGenerator<Node> getNodeToNodeGenerator(Node[] nodes) {
        return (node) -> {
            // idx -> node
            return Arrays.stream(node.toIdxs)
                    .boxed()
                    .map(toIdx -> nodes[toIdx])
                    .collect(Collectors.toList());
        };
    }

    class Node {
        public int idx;
        public int[] toIdxs;

        public Node(int idx, int[] toIdxs) {
            this.idx = idx;
            this.toIdxs = toIdxs;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return idx == node.idx &&
                    Arrays.equals(toIdxs, node.toIdxs);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(idx);
            result = 31 * result + Arrays.hashCode(toIdxs);
            return result;
        }
    }
}
