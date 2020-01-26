package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.stream.IntStream;

@Immutable
@ThreadSafe
public interface IntConsList<E> extends ConsList<E> {

    /**
     * Returns the first element of the <tt>IntConsList</tt> as <tt>int</tt> primitive type.
     *
     * This method ensures no primitive boxing is taking place.
     * <p>Throws {@link NoSuchElementException} if the list is empty.
     *
     * @return first element of the list
     */
    int intHead();

    /**
     * Returns another <tt>IntConsList</tt> containing the current list's elements
     * after the first one.
     *
     * <p>Throws {@link NoSuchElementException} if the list is empty.
     *
     * @return elements of the list after the first one
     */
    @NonNull
    IntConsList<E> intTail();

    /**
     * Constructs a new <tt>IntConsList</tt> with the elements of the current one
     * in the reverse order.
     *
     * @return new list with elements in reversed order
     */
    @NonNull
    IntConsList<E> intReverse();

    /**
     * Creates a new primitive-typed iterator for this list of type <tt>int</tt>.
     * @return new primitive-typed iterator
     */
    @NonNull
    PrimitiveIterator.OfInt intIterator();

    /**
     * Creates a new primitive-typed spliterator for this list of type <tt>int</tt>.
     * @return new primitive-typed spliterator
     */
    @NonNull
    Spliterator.OfInt intSpliterator();

    /**
     * Creates a new primitive-typed stream from this list of type <tt>int</tt>.
     * @return new primitive-typed stream
     */
    @NonNull
    IntStream intStream();
}
