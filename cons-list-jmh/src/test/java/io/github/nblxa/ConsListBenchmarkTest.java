package io.github.nblxa;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static io.github.nblxa.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ConsListBenchmarkTest {
    @Test
    public void test_consList() {
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
    public void test_arrayList() {
        List<ArrayList<Integer>> perms = ConsListBenchmark.permutations(arrList(1, 2, 2, 3));
        assertThat(perms)
            .isNotEmpty()
            .hasSize(12)
            .containsExactlyInAnyOrder(
                arrList(1, 2, 2, 3), arrList(2, 1, 2, 3), arrList(2, 2, 1, 3),
                arrList(1, 2, 3, 2), arrList(2, 1, 3, 2), arrList(2, 2, 3, 1),
                arrList(1, 3, 2, 2), arrList(2, 3, 1, 2), arrList(2, 3, 2, 1),
                arrList(3, 1, 2, 2), arrList(3, 2, 1, 2), arrList(3, 2, 2, 1)
            );
    }

    @Test
    public void test_linkedList() {
        List<LinkedList<Integer>> perms = ConsListBenchmark.permutations(lnkList(1, 2, 2, 3));
        assertThat(perms)
            .isNotEmpty()
            .hasSize(12)
            .containsExactlyInAnyOrder(
                lnkList(1, 2, 2, 3), lnkList(2, 1, 2, 3), lnkList(2, 2, 1, 3),
                lnkList(1, 2, 3, 2), lnkList(2, 1, 3, 2), lnkList(2, 2, 3, 1),
                lnkList(1, 3, 2, 2), lnkList(2, 3, 1, 2), lnkList(2, 3, 2, 1),
                lnkList(3, 1, 2, 2), lnkList(3, 2, 1, 2), lnkList(3, 2, 2, 1)
            );
    }

    private static ArrayList<Integer> arrList(Integer... ints) {
        return new ArrayList<>(Arrays.asList(ints));
    }

    private static LinkedList<Integer> lnkList(Integer... ints) {
        return new LinkedList<>(Arrays.asList(ints));
    }
}