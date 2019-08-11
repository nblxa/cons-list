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

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.nblxa.ConsList.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ConsListBenchmark {
    private static int MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 5;
    private static final int[] FACT = new int[] {
    //  0, 1, 2, 3,  4,   5,   6,     7,      8,       9,        10,         11,          12
        1, 1, 2, 6, 24, 120, 720, 5_040, 40_320, 362_880, 3_628_800, 39_916_800, 479_001_600,
        MAX_ARRAY_LENGTH
    };

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(ConsListBenchmark.class.getSimpleName())
            .addProfiler("gc")
            .addProfiler(SizeOfProfiler.class)
            .warmupIterations(5)
            .forks(3)
            .build();
        new Runner(opt).run();
    }

    private ConsList<String> consList = nil();
    private ArrayList<String> arrayList = new ArrayList<>();
    private LinkedList<String> linkedList = new LinkedList<>();

    @Setup
    public void setup() {
        consList = list("Io", "Europa", "Ganymede", "Callisto", "Io", "Thebe", "Amalthea");
        arrayList = new ArrayList<>(consList);
        linkedList = new LinkedList<>(consList);
    }

    @Benchmark
    public void findAllPermutations_ConsList() {
        ConsList<ConsList<String>> perms = permutations(consList);
        SizeOfProfiler.setRootObject(perms);
    }

    @Benchmark
    public void findAllPermutations_ArrayList() {
        List<ArrayList<String>> perms = permutations(arrayList);
        SizeOfProfiler.setRootObject(perms);
    }

    @Benchmark
    public void findAllPermutations_LinkedList() {
        List<LinkedList<String>> perms = permutations(linkedList);
        SizeOfProfiler.setRootObject(perms);
    }

    static <T> ConsList<ConsList<T>> permutations(ConsList<T> list) {
        if (list.isEmpty()) {
            return cons(list, nil());
        } else {
            ConsList<ConsList<T>> permutations = nil();
            ConsList<T> priorElements = nil();
            while (!list.isEmpty()) {
                T currentElement = list.head();
                if (!priorElements.contains(currentElement)) {
                    ConsList<T> listWithoutCurrentElement = concat(priorElements, list.tail());
                    for (ConsList<T> perm : permutations(listWithoutCurrentElement)) {
                        ConsList<T> permutation = cons(currentElement, perm);
                        permutations = cons(permutation, permutations);
                    }
                }
                priorElements = cons(currentElement, priorElements);
                list = list.tail();
            }
            return permutations;
        }
    }

    static <T> List<ArrayList<T>> permutations(ArrayList<T> list) {
        if (list.isEmpty()) {
            return Collections.singletonList(list);
        } else {
            List<ArrayList<T>> permutations = new ArrayList<>(FACT[list.size()]);
            List<T> priorElements = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                T currentElement = list.get(i);
                if (!priorElements.contains(currentElement)) {
                    ArrayList<T> listWithoutCurrentElement = new ArrayList<>(list);
                    listWithoutCurrentElement.remove(i);
                    for (List<T> perm : permutations(listWithoutCurrentElement)) {
                        ArrayList<T> permutation = new ArrayList<>(perm);
                        permutation.add(currentElement);
                        permutations = new ArrayList<>(permutations);
                        permutations.add(permutation);
                    }
                }
                priorElements.add(currentElement);
            }
            return permutations;
        }
    }

    static <T> List<LinkedList<T>> permutations(LinkedList<T> list) {
        if (list.isEmpty()) {
            return Collections.singletonList(list);
        } else {
            List<LinkedList<T>> permutations = new LinkedList<>();
            List<T> priorElements = new LinkedList<>();
            int i = 0;
            for (T currentElement: list) {
                if (!priorElements.contains(currentElement)) {
                    LinkedList<T> listWithoutCurrentElement = new LinkedList<>(list);
                    listWithoutCurrentElement.remove(i);
                    for (List<T> perm : permutations(listWithoutCurrentElement)) {
                        LinkedList<T> permutation = new LinkedList<>(perm);
                        permutation.add(currentElement);
                        permutations = new LinkedList<>(permutations);
                        permutations.add(permutation);
                    }
                }
                priorElements.add(currentElement);
                i++;
            }
            return permutations;
        }
    }
}
