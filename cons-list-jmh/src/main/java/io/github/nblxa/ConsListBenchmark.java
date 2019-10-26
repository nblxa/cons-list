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

import io.github.nblxa.benchmark.ArrayListLineage;
import io.github.nblxa.benchmark.ConsListLineage;
import io.github.nblxa.benchmark.LinkedListLineage;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.github.nblxa.ConsList.cons;
import static io.github.nblxa.ConsList.nil;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ConsListBenchmark {
    private List<Klass> klasses;
    private ConsListLineage consListLineage;
    private ArrayListLineage arrayListLineage;
    private LinkedListLineage linkedListLineage;
    private ConsList<Integer> consList;
    private List<Integer> arrayList;
    private List<Integer> linkedList;
    private int klassListSize = 100_000;
    private int growListSize = 1_000_000;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(ConsListBenchmark.class.getSimpleName())
            .addProfiler("gc")
            .addProfiler(SizeOfProfiler.class)
            .warmupIterations(3)
            .forks(2)
            .build();
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
        for (int i = 0; i < klassListSize; i++) {
            int size = klasses.size();
            Klass randKlass = klasses.get(rand.nextInt(size));
            Klass newKlass = new Klass(randKlass.name() + "." + i, randKlass);
            klasses.add(newKlass);
        }

        consListLineage = new ConsListLineage();
        arrayListLineage = new ArrayListLineage();
        linkedListLineage = new LinkedListLineage();
    }

    List<Klass> klasses() {
        return klasses;
    }

    @Benchmark
    public void flattenHierarchy_ConsList() {
        for (Klass klass : klasses) {
            consListLineage.lineage(klass);
        }
    }

    @Benchmark
    public void flattenHierarchy_ArrayList() {
        for (Klass klass : klasses) {
            arrayListLineage.lineage(klass);
        }
    }

    @Benchmark
    public void flattenHierarchy_LinkedList() {
        for (Klass klass : klasses) {
            linkedListLineage.lineage(klass);
        }
    }

    @Benchmark
    public void grow_ConsList() {
        ConsList<Integer> list = nil();
        for (int i = 0; i < growListSize; i++) {
            list = cons(i, list);
        }
        this.consList = list.reverse();
        SizeOfProfiler.setRootObject(this.consList);
    }

    @Benchmark
    public void grow_ConsList_reversed() {
        ConsList<Integer> list = nil();
        for (int i = growListSize - 1; i >= 0; i--) {
            list = cons(i, list);
        }
        this.consList = list;
        SizeOfProfiler.setRootObject(this.consList);
    }

    @Benchmark
    public void grow_ArrayList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < growListSize; i++) {
            list.add(i);
        }
        this.arrayList = list;
        SizeOfProfiler.setRootObject(this.arrayList);
    }

    @Benchmark
    public void grow_LinkedList() {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < growListSize; i++) {
            list.add(i);
        }
        this.linkedList = list;
        SizeOfProfiler.setRootObject(this.linkedList);
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

    void setKlassListSize(int klassListSize) {
        this.klassListSize = klassListSize;
    }

    void setGrowListSize(int growListSize) {
        this.growListSize = growListSize;
    }
}
