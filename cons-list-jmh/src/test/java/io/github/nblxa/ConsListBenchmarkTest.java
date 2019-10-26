package io.github.nblxa;

import io.github.nblxa.benchmark.ArrayListLineage;
import io.github.nblxa.benchmark.ConsListLineage;
import io.github.nblxa.benchmark.LinkedListLineage;
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
        ConsListLineage consListLineage = new ConsListLineage();
        for (Klass klass : klasses) {
            consListLineage.lineage(klass);
        }
        assertThat(consListLineage.lineage(identityLinkedList))
            .containsExactly(identityLinkedList, abstractSequentialList, abstractList,
                abstractCollection, object);
    }

    @Test
    public void test_arrayList() {
        ArrayListLineage arrayListLineage = new ArrayListLineage();
        for (Klass klass : klasses) {
            arrayListLineage.lineage(klass);
        }
        assertThat(arrayListLineage.lineage(identityLinkedList))
            .containsExactly(identityLinkedList, abstractSequentialList, abstractList,
                abstractCollection, object);
    }

    @Test
    public void test_linkedList() {
        LinkedListLineage arrayListLineage = new LinkedListLineage();
        for (Klass klass : klasses) {
            arrayListLineage.lineage(klass);
        }
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
        benchmark.setup();
        List<Klass> klist = benchmark.klasses();
        Set<Klass> kset = new HashSet<>(klist);
        assertThat(klist)
            .hasSize(100_013);
        assertThat(kset)
            .hasSize(100_013);
    }
}
