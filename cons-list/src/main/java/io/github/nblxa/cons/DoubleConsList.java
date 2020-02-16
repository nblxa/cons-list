package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.DoubleStream;

@Immutable
@ThreadSafe
public interface DoubleConsList<E> extends ConsList<E> {

    /**
     * Returns the first element of the <tt>DoubleConsList</tt> as <tt>double</tt> primitive type.
     *
     * This method ensures no primitive boxing is taking place.
     * <p>Throws {@link NoSuchElementException} if the list is empty.
     *
     * @return first element of the list
     */
    double doubleHead();

    /**
     * Returns another <tt>DoubleConsList</tt> containing the current list's elements
     * after the first one.
     *
     * <p>Throws {@link NoSuchElementException} if the list is empty.
     *
     * @return elements of the list after the first one
     */
    @NonNull
    DoubleConsList<E> doubleTail();

    /**
     * Constructs a new <tt>DoubleConsList</tt> with the elements of the current one
     * in the reverse order.
     *
     * @return new list with elements in reversed order
     */
    @NonNull
    DoubleConsList<E> doubleReverse();

    /**
     * Creates a new primitive-typed iterator for this list of type <tt>double</tt>.
     * @return new primitive-typed iterator
     */
    @NonNull
    PrimitiveIterator.OfDouble doubleIterator();

    /**
     * Creates a new primitive-typed spliterator for this list of type <tt>double</tt>.
     * @return new primitive-typed spliterator
     */
    @NonNull
    Spliterator.OfDouble doubleSpliterator();

    /**
     * Creates a new primitive-typed stream from this list of type <tt>double</tt>.
     * @return new primitive-typed stream
     */
    @NonNull
    DoubleStream doubleStream();

    /**
     * Eager implementation of the <tt>map</tt> method that applies <tt>mapper</tt>
     * to all elements of the <tt>DoubleConsList</tt>.
     *
     * The resulting new list has the same order of elements.
     *
     * @param mapper the mapping function to be applied to all elements
     * @return new resulting list
     */
    @NonNull
    DoubleConsList<E> doubleMap(@NonNull DoubleUnaryOperator mapper);
}
