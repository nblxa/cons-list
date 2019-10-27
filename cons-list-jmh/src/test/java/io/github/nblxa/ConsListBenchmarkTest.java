package io.github.nblxa;

import io.github.nblxa.benchmark.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsListBenchmarkTest {
    private static Set<Klass> klasses;
    private static Klass object;
    private static Klass abstractCollection;
    private static Klass abstractList;
    private static Klass abstractSequentialList;
    private static Klass identityLinkedList;
    private static Klass abstractSet;

    @BeforeClass
    public static void setUpClass() {
        object = new Klass("object");
        Klass string = new Klass("string", object);
        abstractCollection = new Klass("abstractCollection", object);
        abstractList = new Klass("abstractList", abstractCollection);
        Klass arrayList = new Klass("arrayList", abstractList);
        abstractSequentialList = new Klass("abstractSequentialList", abstractList);
        Klass linkedList = new Klass("linkedList", abstractSequentialList);
        Klass keepAliveStreamCleaner = new Klass("keepAliveStreamCleaner", linkedList);
        identityLinkedList = new Klass("identityLinkedList", abstractSequentialList);
        abstractSet = new Klass("abstractSet", abstractCollection);
        klasses = new HashSet<>(Arrays.asList(object, string, abstractCollection, abstractList,
            arrayList, abstractSequentialList, linkedList, keepAliveStreamCleaner,
            identityLinkedList, abstractSet));
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
    public void test_growConsList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(13);
        benchmark.grow_ConsList();
        ConsList<Integer> list = benchmark.consList();
        assertThat(list)
            .hasSize(13);
        assertThat(list)
            .startsWith(0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void test_growConsListReversed() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(14);
        benchmark.grow_ConsList_reversed();
        ConsList<Integer> list = benchmark.consList();
        assertThat(list)
            .hasSize(14);
        assertThat(list)
            .startsWith(0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void test_growArrayList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(15);
        benchmark.grow_ArrayList();
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
        benchmark.grow_LinkedList();
        List<Integer> list = benchmark.linkedList();
        assertThat(list)
            .hasSize(16);
        assertThat(list)
            .startsWith(0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void test_iterateConsList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(15);
        benchmark.setup();
        int sum = benchmark.iterate_ConsList();
        assertThat(sum)
            .isEqualTo(105);
    }

    @Test
    public void test_iterateArrayList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(10);
        benchmark.setup();
        int sum = benchmark.iterate_ArrayList();
        assertThat(sum)
            .isEqualTo(45);
    }

    @Test
    public void test_iterateLinkedList() {
        ConsListBenchmark benchmark = new ConsListBenchmark();
        benchmark.setGrowListSize(11);
        benchmark.setup();
        int sum = benchmark.iterate_LinkedList();
        assertThat(sum)
            .isEqualTo(55);
    }
}
