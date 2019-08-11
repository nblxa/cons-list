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
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.nblxa.ConsList.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ConsListBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(ConsListBenchmark.class.getSimpleName())
            .addProfiler("hs_gc")
            .warmupIterations(5)
            .forks(3)
            .build();
        new Runner(opt).run();
    }

    private ConsList<String> consList = nil();
    private ArrayList<String> arrayList = new ArrayList<>();

    @Setup
    public void setup() {
        consList = list("Io", "Europa", "Ganymede", "Callisto", "Io");
        arrayList = new ArrayList<>(consList);
    }

    @Benchmark
    public void findAllPermutations_ConsList() {
        permutations(consList);
    }

    @Benchmark
    public void findAllPermutations_ArrayList() {
        permutations(arrayList);
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

    static <T> List<List<T>> permutations(List<T> list) {
        if (list.isEmpty()) {
            return Collections.singletonList(list);
        } else {
            List<List<T>> permutations = new ArrayList<>();
            List<T> priorElements = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                T currentElement = list.get(i);
                if (!priorElements.contains(currentElement)) {
                    List<T> listWithoutCurrentElement = new ArrayList<>(list);
                    listWithoutCurrentElement.remove(i);
                    for (List<T> perm : permutations(listWithoutCurrentElement)) {
                        List<T> permutation = new ArrayList<>(perm);
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
}
