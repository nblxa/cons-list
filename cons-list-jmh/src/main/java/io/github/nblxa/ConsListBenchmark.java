/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.github.nblxa;

import io.github.nblxa.benchmark.*;
import io.github.nblxa.cons.ConsList;
import io.github.nblxa.cons.IntConsList;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.github.nblxa.cons.ConsList.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ConsListBenchmark {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsListBenchmark.class);
    private List<Klass> klasses;
    private ListLineage consListLineage;
    private ListLineage arrayListLineage;
    private ListLineage linkedListLineage;
    private ConsList<Integer> consList;
    private IntConsList<Integer> intConsList;
    private List<Integer> arrayList;
    private List<Integer> linkedList;

    /**
     * Size of the list of {@link Klass} objects that form the hierarchy to be flattened
     * in the benchmarks:
     *
     * <ul>
     * <li>{@link ConsListBenchmark#flattenHierarchyConsList}</li>
     * <li>{@link ConsListBenchmark#flattenHierarchyArrayList}</li>
     * <li>{@link ConsListBenchmark#flattenHierarchyLinkedList}</li>
     * </ul>
     */
    private int klassListSize = 100_000;

    /**
     * Size of the list of integers to be constructed and iterated in benchmarks:
     * <ul>
     * <li>{@link ConsListBenchmark#growConsList}</li>
     * <li>{@link ConsListBenchmark#growConsListReverseInputOrder}</li>
     * <li>{@link ConsListBenchmark#growIntConsList}</li>
     * <li>{@link ConsListBenchmark#growIntConsListReverseInputOrder}</li>
     * <li>{@link ConsListBenchmark#growArrayList}</li>
     * <li>{@link ConsListBenchmark#growLinkedList}</li>
     * <li>{@link ConsListBenchmark#iterateConsList}</li>
     * <li>{@link ConsListBenchmark#iterateIntConsList}</li>
     * <li>{@link ConsListBenchmark#iterateArrayList}</li>
     * <li>{@link ConsListBenchmark#iterateLinkedList}</li>
     * </ul>
     */
    private int growListSize = 1_000_000;

    public static void main(String[] args) throws RunnerException {
        final List<String> exclude;
        if (args.length > 0 && args[0].equals("cons")) {
            exclude = Arrays.asList("^.*ArrayList.*$", "^.*LinkedList.*$");
        } else {
            exclude = Collections.emptyList();
        }

        ChainedOptionsBuilder optBuilder = new OptionsBuilder()
            .include(ConsListBenchmark.class.getSimpleName())
            .addProfiler("gc")
            .addProfiler(SizeOfProfiler.class)
            .warmupIterations(3)
            .forks(2)
            .warmupTime(TimeValue.seconds(5L))
            .measurementTime(TimeValue.seconds(5L));
        for (String ex: exclude) {
            optBuilder = optBuilder.exclude(ex);
        }
        Options opt = optBuilder.build();

        new Runner(opt).run();
    }

    @Setup
    public void setup() {
        Klass object = new Klass("object");
        Klass string = new Klass("string", object);
        Klass abstractCollection = new Klass("abstractCollection", object);
        Klass abstractList = new Klass("abstractList", abstractCollection);
        Klass arrayList = new Klass("arrayList", abstractList);
        Klass abstractSequentialList = new Klass("abstractSequentialList", abstractList);
        Klass linkedList = new Klass("linkedList", abstractSequentialList);
        Klass keepAliveStreamCleaner = new Klass("keepAliveStreamCleaner", linkedList);
        Klass identityLinkedList = new Klass("identityLinkedList", abstractSequentialList);
        Klass abstractSet = new Klass("abstractSet", abstractCollection);
        Klass hashSet = new Klass("hashSet", abstractSet);
        Klass treeSet = new Klass("treeSet", abstractSet);
        Klass consList = new Klass("consList", abstractCollection);
        klasses = new ArrayList<>(Arrays.asList(object, string, abstractCollection, abstractList,
            arrayList, abstractSequentialList, linkedList, keepAliveStreamCleaner,
            identityLinkedList, abstractSet, hashSet, treeSet, consList));

        Random rand = new Random(42L);
        int klassesSize = klasses.size();
        for (int i = 0; i < klassListSize - klassesSize; i++) {
            Klass randKlass = klasses.get(rand.nextInt(klasses.size()));
            Klass newKlass = new Klass(randKlass.name() + "." + i, randKlass);
            klasses.add(newKlass);
        }

        consListLineage = new ConsListLineage();
        arrayListLineage = new ArrayListLineage();
        linkedListLineage = new LinkedListLineage();

        // set up the lists for the iteration test
        growConsListReverseInputOrder();
        growArrayList();
        growLinkedList();
        growIntConsListReverseInputOrder();
    }

    List<Klass> klasses() {
        return klasses;
    }

    @Benchmark
    public void flattenHierarchyConsList() {
        for (Klass klass : klasses) {
            consListLineage.lineage(klass);
        }
        SizeOfProfiler.setRootObject(null);
    }

    @Benchmark
    public void flattenHierarchyArrayList() {
        for (Klass klass : klasses) {
            arrayListLineage.lineage(klass);
        }
        SizeOfProfiler.setRootObject(null);
    }

    @Benchmark
    public void flattenHierarchyLinkedList() {
        for (Klass klass : klasses) {
            linkedListLineage.lineage(klass);
        }
        SizeOfProfiler.setRootObject(null);
    }

    @Benchmark
    public void growArrayList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < growListSize; i++) {
            list.add(i);
        }
        this.arrayList = list;
        SizeOfProfiler.setRootObject(this.arrayList);
    }

    @Benchmark
    public void growLinkedList() {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < growListSize; i++) {
            list.add(i);
        }
        this.linkedList = list;
        SizeOfProfiler.setRootObject(this.linkedList);
    }

    @Benchmark
    public void growConsList() {
        ConsList<Integer> list = nil();
        for (int i = 0; i < growListSize; i++) {
            list = cons(i, list);
        }
        this.consList = list.reverse();
        SizeOfProfiler.setRootObject(this.consList);
    }

    @Benchmark
    public void growConsListReverseInputOrder() {
        ConsList<Integer> list = nil();
        for (int i = growListSize - 1; i >= 0; i--) {
            list = cons(i, list);
        }
        this.consList = list;
        SizeOfProfiler.setRootObject(this.consList);
    }

    @Benchmark
    public void growIntConsList() {
        IntConsList<Integer> list = nil();
        for (int i = 0; i < growListSize; i++) {
            list = intCons(i, list);
        }
        this.intConsList = list.intReverse();
        SizeOfProfiler.setRootObject(this.intConsList);
    }

    @Benchmark
    public void growIntConsListReverseInputOrder() {
        IntConsList<Integer> list = nil();
        for (int i = growListSize - 1; i >= 0; i--) {
            list = intCons(i, list);
        }
        this.intConsList = list;
        SizeOfProfiler.setRootObject(this.intConsList);
    }

    @Benchmark
    public int iterateConsList() {
        int sum = 0;
        for (Integer i: consList) {
            sum += i;
        }
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.valueOf(sum));
        }
        SizeOfProfiler.setRootObject(null);
        return sum;
    }

    @Benchmark
    public int iterateArrayList() {
        int sum = 0;
        for (Integer i: arrayList) {
            sum += i;
        }
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.valueOf(sum));
        }
        SizeOfProfiler.setRootObject(null);
        return sum;
    }

    @Benchmark
    public int iterateLinkedList() {
        int sum = 0;
        for (Integer i: linkedList) {
            sum += i;
        }
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.valueOf(sum));
        }
        SizeOfProfiler.setRootObject(null);
        return sum;
    }

    @Benchmark
    public int iterateIntConsList() {
        int sum = 0;
        PrimitiveIterator.OfInt iter = intConsList.intIterator();
        while (iter.hasNext()) {
            sum += iter.nextInt();
        }
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.valueOf(sum));
        }
        SizeOfProfiler.setRootObject(null);
        return sum;
    }

    public ConsList<Integer> consList() {
        return consList;
    }

    public List<Integer> arrayList() {
        return arrayList;
    }

    public List<Integer> linkedList() {
        return linkedList;
    }

    public ConsList<Integer> intConsList() {
        return intConsList;
    }

    void setKlassListSize(int klassListSize) {
        this.klassListSize = klassListSize;
    }

    void setGrowListSize(int growListSize) {
        this.growListSize = growListSize;
    }
}
