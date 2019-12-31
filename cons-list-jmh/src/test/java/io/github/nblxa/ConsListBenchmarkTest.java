package io.github.nblxa;

import io.github.nblxa.benchmark.*;
import io.github.nblxa.cons.ConsList;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsListBenchmarkTest {
    private static Klass object;
    private static Klass abstractCollection;
    private static Klass abstractList;
    private static Klass abstractSequentialList;
    private static Klass identityLinkedList;

    @BeforeClass
    public static void setUpClass() {
        object = new Klass("object");
        abstractCollection = new Klass("abstractCollection", object);
        abstractList = new Klass("abstractList", abstractCollection);
        abstractSequentialList = new Klass("abstractSequentialList", abstractList);
        identityLinkedList = new Klass("identityLinkedList", abstractSequentialList);
    }

    @Test
    public void test_consList() {
        ListLineage consListLineage = new ConsListLineage();
        assertThat(consListLineage.lineage(identityLinkedList))
            .containsExactly(identityLinkedList, abstractSequentialList, abstractList,
                abstractCollection, object);
    }

    @Test
    public void test_arrayList() {
        ListLineage arrayListLineage = new ArrayListLineage();
        assertThat(arrayListLineage.lineage(identityLinkedList))
            .containsExactly(identityLinkedList, abstractSequentialList, abstractList,
                abstractCollection, object);
    }

    @Test
    public void test_linkedList() {
        ListLineage arrayListLineage = new LinkedListLineage();
        assertThat(arrayListLineage.lineage(identityLinkedList))
            .containsExactly(identityLinkedList, abstractSequentialList, abstractList,
                abstractCollection, object);
    }

    @Test
    public void test_equalsHashcode() {
        EqualsVerifier.forClass(Klass.class)
            .withPrefabValues(Klass.class,
                new Klass("foo"), new Klass("bar", new Klass("baz")))
            .verify();
    }

    @Test
    public void test_setup() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setKlassListSize(21);
        benchmark.setup();
        List<Klass> klist = benchmark.klasses();
        Set<Klass> kset = new HashSet<>(klist);
        assertThat(klist)
            .hasSize(21);
        assertThat(kset)
            .hasSize(21);
    }

    @Test
    public void test_growArrayList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(15);
        benchmark.growArrayList();
        List<Integer> list = benchmark.arrayList();
        assertThat(list)
            .hasSize(15);
        assertThat(list)
            .startsWith(0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void test_growLinkedList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(16);
        benchmark.growLinkedList();
        List<Integer> list = benchmark.linkedList();
        assertThat(list)
            .hasSize(16);
        assertThat(list)
            .startsWith(0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void test_growConsList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(13);
        benchmark.growConsList();
        ConsList<Integer> list = benchmark.consList();
        assertThat(list)
            .hasSize(13);
        assertThat(list)
            .startsWith(0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void test_growConsListReverseInputOrder() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(13);
        benchmark.growConsListReverseInputOrder();
        ConsList<Integer> list = benchmark.consList();
        assertThat(list)
            .hasSize(13);
        assertThat(list)
            .startsWith(0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void test_growIntConsList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(13);
        benchmark.growIntConsList();
        ConsList<Integer> list = benchmark.intConsList();
        assertThat(list)
            .hasSize(13);
        assertThat(list)
            .startsWith(0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void test_growIntConsListReverseInputOrder() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(13);
        benchmark.growIntConsListReverseInputOrder();
        ConsList<Integer> list = benchmark.intConsList();
        assertThat(list)
            .hasSize(13);
        assertThat(list)
            .startsWith(0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void test_iterateConsList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(15);
        benchmark.setup();
        int sum = benchmark.iterateConsList();
        assertThat(sum)
            .isEqualTo(105);
    }

    @Test
    public void test_iterateArrayList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(10);
        benchmark.setup();
        int sum = benchmark.iterateArrayList();
        assertThat(sum)
            .isEqualTo(45);
    }

    @Test
    public void test_iterateLinkedList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(11);
        benchmark.setup();
        int sum = benchmark.iterateLinkedList();
        assertThat(sum)
            .isEqualTo(55);
    }

    @Test
    public void test_iterateIntConsList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(15);
        benchmark.setup();
        int sum = benchmark.iterateIntConsList();
        assertThat(sum)
            .isEqualTo(105);
    }
}
