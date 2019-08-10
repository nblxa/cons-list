package io.github.nblxa;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static io.github.nblxa.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ConsListBenchmarkTest {
    @Test
    public void testConsList() {
        ConsList<ConsList<Integer>> perms = ConsListBenchmark.permutations(list(1, 2, 2, 3));
        assertThat(perms)
            .isNotEmpty()
            .hasSize(12)
            .containsExactlyInAnyOrder(
                list(1, 2, 2, 3), list(2, 1, 2, 3), list(2, 2, 1, 3),
                list(1, 2, 3, 2), list(2, 1, 3, 2), list(2, 2, 3, 1),
                list(1, 3, 2, 2), list(2, 3, 1, 2), list(2, 3, 2, 1),
                list(3, 1, 2, 2), list(3, 2, 1, 2), list(3, 2, 2, 1)
            );
    }

    @Test
    public void testCopyOnWriteArrayList() {
        List<List<Integer>> perms = ConsListBenchmark.permutations(cowal(1, 2, 2, 3));
        assertThat(perms)
            .isNotEmpty()
            .hasSize(12)
            .containsExactlyInAnyOrder(
                cowal(1, 2, 2, 3), cowal(2, 1, 2, 3), cowal(2, 2, 1, 3),
                cowal(1, 2, 3, 2), cowal(2, 1, 3, 2), cowal(2, 2, 3, 1),
                cowal(1, 3, 2, 2), cowal(2, 3, 1, 2), cowal(2, 3, 2, 1),
                cowal(3, 1, 2, 2), cowal(3, 2, 1, 2), cowal(3, 2, 2, 1)
            );
    }

    private static CopyOnWriteArrayList<Integer> cowal(Integer... ints) {
        return new CopyOnWriteArrayList<>(ints);
    }
}

