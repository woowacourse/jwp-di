package nextstep.supports;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TopologySortTest {

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


    @DisplayName("2개짜리 사이클")
    @Test
    void calculateReversedOrders() {
        final Node[] nodes = {
                new Node(0, new int[]{1}),
                new Node(1, new int[]{0}),
        };
        Set<Node> inputNodes = Sets.newHashSet(Arrays.asList(
                nodes
        ));

        TopologySort<Node> topologySort = new TopologySort<Node>(
                inputNodes,
                (node) -> {
                    // idx -> node
                    return Arrays.stream(node.toIdxs)
                            .boxed()
                            .map(toIdx -> nodes[toIdx])
                            .collect(Collectors.toList());
                },
                () -> {
                    throw new RuntimeException("빵");
                }
        );

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
        Set<Node> inputNodes = Sets.newHashSet(Arrays.asList(
                nodes
        ));

        TopologySort<Node> topologySort = new TopologySort<Node>(
                inputNodes,
                (node) -> {
                    // idx -> node
                    return Arrays.stream(node.toIdxs)
                            .boxed()
                            .map(toIdx -> nodes[toIdx])
                            .collect(Collectors.toList());
                },
                () -> {
                    throw new RuntimeException("빵");
                }
        );

        assertThrows(RuntimeException.class, () -> topologySort.calculateReversedOrders());
    }

    @DisplayName("정상적.. ")
    @Test
    void calculateReversedOrders_2() {
        // Arrange
        final Node[] nodes = {
                new Node(0, new int[]{1}),
                new Node(1, new int[]{2}),
                new Node(2, new int[]{}),
        };
        Set<Node> inputNodes = Sets.newHashSet(Arrays.asList(
                nodes
        ));

        TopologySort<Node> topologySort = new TopologySort<Node>(
                inputNodes,
                (node) -> {
                    // idx -> node
                    return Arrays.stream(node.toIdxs)
                            .boxed()
                            .map(toIdx -> nodes[toIdx])
                            .collect(Collectors.toList());
                },
                () -> {
                    throw new RuntimeException("빵");
                }
        );

        //
        List<Node> reversedOrders = topologySort.calculateReversedOrders();

        // Assert
        for (Node from : nodes) {
            List<Node> toNodes = Arrays.stream(from.toIdxs)
                    .boxed()
                    .map(toIdx -> nodes[toIdx])
                    .collect(Collectors.toList());
            for (Node to : toNodes) {
                // from 이 to 보다 먼저오는지
                assertThat(getIdx(reversedOrders, from) > getIdx(reversedOrders, to)).isTrue();
            }
        }
    }


    @DisplayName("정상적.. 복잡")
    @Test
    void calculateReversedOrders_3() {
        // Arrange
        final Node[] nodes = {
                new Node(0, new int[]{1}),
                new Node(1, new int[]{2, 3}),
                new Node(2, new int[]{4}),
                new Node(3, new int[]{4}),
                new Node(4, new int[]{5}),
                new Node(5, new int[]{}),
        };
        Set<Node> inputNodes = Sets.newHashSet(Arrays.asList(
                nodes
        ));

        TopologySort<Node> topologySort = new TopologySort<Node>(
                inputNodes,
                (node) -> {
                    // idx -> node
                    return Arrays.stream(node.toIdxs)
                            .boxed()
                            .map(toIdx -> nodes[toIdx])
                            .collect(Collectors.toList());
                },
                () -> {
                    throw new RuntimeException("빵");
                }
        );

        //
        List<Node> reversedOrders = topologySort.calculateReversedOrders();

        // Assert
        for (Node from : nodes) {
            List<Node> toNodes = Arrays.stream(from.toIdxs)
                    .boxed()
                    .map(toIdx -> nodes[toIdx])
                    .collect(Collectors.toList());
            for (Node to : toNodes) {
                // from 이 to 보다 먼저오는지
                assertThat(getIdx(reversedOrders, from) > getIdx(reversedOrders, to)).isTrue();
            }
        }
    }

    private int getIdx(List<Node> nodes, Node node) {
        return nodes.indexOf(node);
    }
}