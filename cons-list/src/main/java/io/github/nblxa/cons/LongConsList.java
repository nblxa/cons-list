package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.stream.LongStream;

@Immutable
@ThreadSafe
public interface LongConsList<E> extends ConsList<E> {
    long longHead();

    @NonNull
    LongConsList<E> longTail();

    @NonNull
    LongConsList<E> longReverse();

    @NonNull
    PrimitiveIterator.OfLong longIterator();

    @NonNull
    Spliterator.OfLong longSpliterator();

    @NonNull
    LongStream longStream();
}
