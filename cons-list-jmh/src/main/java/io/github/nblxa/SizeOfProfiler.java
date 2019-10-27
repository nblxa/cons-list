package io.github.nblxa;

import org.ehcache.sizeof.SizeOf;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.IterationParams;
import org.openjdk.jmh.profile.InternalProfiler;
import org.openjdk.jmh.results.*;

import java.util.Arrays;
import java.util.Collection;

public class SizeOfProfiler implements InternalProfiler {
    private static Object rootObject = null;
    private static final SizeOf sizeOf = SizeOf.newInstance();

    static void setRootObject(Object rootObject) {
        SizeOfProfiler.rootObject = rootObject;
    }

    @Override
    public void beforeIteration(BenchmarkParams benchmarkParams, IterationParams iterationParams) {
        // no setup is required
    }

    @Override
    public Collection<? extends Result> afterIteration(BenchmarkParams benchmarkParams,
                                                       IterationParams iterationParams,
                                                       IterationResult result) {
        long size = sizeOf.deepSizeOf(rootObject);
        return Arrays.asList(
            new ScalarResult(Defaults.PREFIX + "result.size", size, "b", AggregationPolicy.AVG)
        );
    }

    @Override
    public String getDescription() {
        return "Object size profiling using SizeOf.";
    }
}
