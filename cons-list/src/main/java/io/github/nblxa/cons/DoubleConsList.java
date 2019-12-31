package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.stream.DoubleStream;

@Immutable
@ThreadSafe
public interface DoubleConsList<E> extends ConsList<E> {
    double doubleHead();

    @NonNull
    DoubleConsList<E> doubleTail();

    @NonNull
    DoubleConsList<E> doubleReverse();

    @NonNull
    PrimitiveIterator.OfDouble doubleIterator();

    @NonNull
    Spliterator.OfDouble doubleSpliterator();

    @NonNull
    DoubleStream doubleStream();
}
