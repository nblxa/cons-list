package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.stream.LongStream;

@Immutable
@ThreadSafe
public interface LongConsList<E> extends ConsList<E> {

    /**
     * Returns the first element of the <tt>LongConsList</tt> as <tt>long</tt> primitive type.
     *
     * This method ensures no primitive boxing is taking place.
     * <p>Throws {@link NoSuchElementException} if the list is empty.
     *
     * @return first element of the list
     */
    long longHead();

    /**
     * Returns another <tt>LongConsList</tt> containing the current list's elements
     * after the first one.
     *
     * <p>Throws {@link NoSuchElementException} if the list is empty.
     *
     * @return elements of the list after the first one
     */
    @NonNull
    LongConsList<E> longTail();

    /**
     * Constructs a new <tt>LongConsList</tt> with the elements of the current one
     * in the reverse order.
     *
     * @return new list with elements in reversed order
     */
    @NonNull
    LongConsList<E> longReverse();

    /**
     * Creates a new primitive-typed iterator for this list of type <tt>long</tt>.
     * @return new primitive-typed iterator
     */
    @NonNull
    PrimitiveIterator.OfLong longIterator();

    /**
     * Creates a new primitive-typed spliterator for this list of type <tt>long</tt>.
     * @return new primitive-typed spliterator
     */
    @NonNull
    Spliterator.OfLong longSpliterator();

    /**
     * Creates a new primitive-typed stream from this list of type <tt>long</tt>.
     * @return new primitive-typed stream
     */
    @NonNull
    LongStream longStream();
}
