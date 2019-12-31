package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.stream.IntStream;

@Immutable
@ThreadSafe
public interface IntConsList<E> extends ConsList<E> {
    int intHead();

    @NonNull
    IntConsList<E> intTail();

    @NonNull
    IntConsList<E> intReverse();

    @NonNull
    PrimitiveIterator.OfInt intIterator();

    @NonNull
    Spliterator.OfInt intSpliterator();

    @NonNull
    IntStream intStream();
}
