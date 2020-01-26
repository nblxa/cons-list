package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


/**
 * The ultimate immutable and thread-safe Cons List that implements {@link java.util.Collection}
 * and avoids recursive calls.
 *
 * <p>Usage:
 *
 * <pre>
 * import static io.github.nblxa.cons.ConsList.*;
 * ...
 * ConsList&lt;String&gt; list = list(&quot;Apples&quot;, &quot;Oranges&quot;, &quot;Bananas&quot;);
 * list.forEach(fruit -&gt; System.out.println(&quot;I like &quot; + fruit);</pre>
 *
 * @param <E> element type
 */
@Immutable
@ThreadSafe
public interface ConsList<E> extends Collection<E> {
    /**
     * Returns the empty <tt>ConsList</tt>.
     *
     * <p>The result is a singleton instance shared by all empty cons lists.
     *
     * <p><tt>tail</tt> collection, if not of type <tt>ConsList</tt>, will be converted to it.
     *
     * @param <T> element type
     * @return singleton empty list
     */
    @NonNull
    @SuppressWarnings("unchecked")
    static <T> Nil<T> nil() {
        return (Nil<T>) Nil.INSTANCE;
    }

    /**
     * Constructs a new <tt>ConsList</tt> with elements <tt>head</tt> and <tt>tail</tt>.
     *
     * <p>The head, the tail elements and the resulting list have the same compile-time type.
     *
     * <p>The original list is not modified.
     *
     * @param head first element of the new list
     * @param tail <tt>ConsList</tt> with the second and consecutive elements of the new list
     * @param <V>  element type
     * @return a cons list with the given head and tail elements
     */
    @NonNull
    static <V> ConsList<V> cons(@Nullable V head, @NonNull ConsList<V> tail) {
        return new ConsListImpl<>(head, Objects.requireNonNull(tail, ConsUtil.MSG_TAIL_IS_NULL));
    }

    /**
     * Constructs a new <tt>ConsList</tt> with elements <tt>head</tt> and <tt>tail</tt>.
     *
     * <p>The head, the tail elements and the resulting list have the same compile-time type.
     *
     * <p><tt>tail</tt> collection, if not of type <tt>ConsList</tt>, will be converted to it.
     *
     * <p>The original list is not modified.
     *
     * @param head first element of the new list
     * @param tail collection of second and consecutive elements of the new list;
     * @param <V>  element type
     * @return a cons list with the given head and tail elements
     */
    @NonNull
    static <V> ConsList<V> cons(@Nullable V head, @NonNull Collection<V> tail) {
        return new ConsListImpl<>(head, consList(tail));
    }

    /**
     * Constructs a new <tt>ConsList</tt> of element type <tt>klass</tt> with elements <tt>head</tt>
     * and <tt>tail</tt>.
     *
     * <p>The head, the tail elements and the resulting list have the same compile-time type
     * defined by the parameter <tt>klass</tt>. This type must be the common supertype
     * of both the new head element and the elements of the tail. The parameter is required
     * to overcome the deficiencies of the Java generics and type inference in JDK 8.
     *
     * <p>This works without the explicit type parameter:
     * <pre>ConsList&lt;Number&gt; l = cons(3.14d, cons(10, nil()));</pre>
     *
     * <p>But this requires it:
     * <pre>ConsList&lt;Integer&gt; l = cons(10, nil());
     *     ConsList&lt;Number&gt; l = cons(3.14d, i, Number.class);</pre>
     *
     * <p>The original list is not modified.
     *
     * @param head  first element of the new list
     * @param tail  <tt>ConsList</tt> with the second and consecutive elements of the new list
     * @param klass type-evidence parameter, unused at runtime, and only required
     *              to provide static type binding at compile-time
     * @param <V>   element type of the resulting list: base class of both <tt>head</tt>
     *              and elements of <tt>tail</tt>
     * @param <U>   element type of the new list's head
     * @return a cons list with the given head and tail elements and of given element type
     */
    @NonNull
    @SuppressWarnings( {"rawtypes", "unchecked", "unused"})
    static <V, U extends V> ConsList<V> cons(@Nullable U head,
                                             @NonNull ConsList<? extends V> tail,
                                             @NonNull Class<V> klass) {
        return (ConsList<V>) new ConsListImpl(head, Objects.requireNonNull(tail, ConsUtil.MSG_TAIL_IS_NULL));
    }

    /**
     * Constructs a new cons list of element type <tt>klass</tt> with elements <tt>head</tt>
     * and <tt>tail</tt>.
     *
     * <p>The head, the tail elements and the resulting list have the same compile-time type
     * defined by the parameter <tt>klass</tt>. This type must be the common supertype
     * of both the new head element and the elements of the tail. The parameter is required
     * to overcome the deficiencies of the Java generics and type inference in JDK 8.
     *
     * <p>This works without the explicit type parameter:
     * <pre>ConsList&lt;Number&gt; l = cons(3.14d, cons(10, nil()));</pre>
     *
     * <p>But this requires it:
     * <pre>ConsList&lt;Integer&gt; l = cons(10, nil());
     *     ConsList&lt;Number&gt; l = cons(3.14d, i, Number.class);</pre>
     *
     * <p>The original list is not modified.
     *
     * @param head  first element of the new list
     * @param tail  collection of second and consecutive elements of the new list;
     *              if it not of type <tt>ConsList</tt>, it will be converted
     * @param klass type-evidence parameter, unused at runtime, and only required
     *              to provide static type binding at compile-time
     * @param <V>   element type of the resulting list: base class of both <tt>head</tt>
     *              and elements of <tt>tail</tt>
     * @param <U>   element type of the new list's head
     * @return a cons list with the given head and tail elements and of given element type
     */
    @NonNull
    @SuppressWarnings( {"rawtypes", "unchecked", "unused"})
    static <V, U extends V> ConsList<V> cons(@Nullable U head,
                                                    @NonNull Collection<? extends V> tail,
                                                    @NonNull Class<V> klass) {
        return (ConsList<V>) new ConsListImpl(head, consList(tail));
    }

    /**
     * Constructs a new cons list containing elements in the given order.
     *
     * <p>An invocation with an empty parameter list will produce the empty
     * list instance <tt>nil()</tt>.
     *
     * <p>With a non-empty parameter array, its first element is the head of the new cons list,
     * the rest are the parameters for constructing its tail.
     *
     * @param elements any number of elements of the list to be constructed
     * @param <V>      element type
     * @return the cons list with the elements consList the argument array in the same order
     */
    @NonNull
    @SafeVarargs
    static <V> ConsList<V> list(@NonNull V... elements) {
        ConsList<V> cons = nil();
        for (int i = elements.length - 1; i >= 0; i--) {
            cons = new ConsListImpl<>(elements[i], cons);
        }
        return cons;
    }

    /**
     * Constructs a new <tt>ConsList</tt> from the given {@link Iterable}.
     *
     * <p>If the {@link Iterable} is itself a ConsList, returns the typecast iterable.
     *
     * <p>For instances of {@link List}, only one iteration backwards through the list's
     * {@link ListIterator} will be done.
     *
     * <p>For all other {@link Iterable} types, this traverses both the Iterable
     * and the intermediate ConsList once.
     *
     * @param iterable input iterable
     * @param <V>      element type
     * @return the cons list with the elements consList the Iterable in the same order
     */
    @NonNull
    static <V> ConsList<V> consList(@NonNull Iterable<V> iterable) {
        Objects.requireNonNull(iterable, ConsUtil.MSG_ITERABLE_IS_NULL);
        if (iterable instanceof ConsList) {
            return (ConsList<V>) iterable;
        }
        ConsList<V> cons = nil();
        if (iterable instanceof List) {
            List<V> list = (List<V>) iterable;
            ListIterator<V> iter = list.listIterator(list.size());
            while (iter.hasPrevious()) {
                cons = new ConsListImpl<>(iter.previous(), cons);
            }
            return cons;
        } else {
            for (V v : iterable) {
                cons = new ConsListImpl<>(v, cons);
            }
            return cons.reverse();
        }
    }

    /**
     * Returns a <tt>ConsList</tt> that contains the concatenation of elements of all argument cons lists.
     *
     * @param first the first argument cons list, not nullable
     * @param rest the rest of the argument cons lists, each of them not nullable
     * @param <V>   element type of all argument lists and the resulting list
     * @return the concatentation of all argument lists
     */
    @NonNull
    @SafeVarargs
    static <V> ConsList<V> concat(@NonNull ConsList<V> first, @NonNull ConsList<V>... rest) {
        Objects.requireNonNull(first, "Null concat argument at position 0");
        Objects.requireNonNull(rest, ConsUtil.MSG_ARG_ARRAY_REST_IS_NULL);
        if (rest.length == 0) {
            return first;
        }
        ConsList<V> result = rest[rest.length - 1];
        if (result == null) {
            throw new NullPointerException(ConsUtil.MSG_NULL_CONCAT_ARG_AT_POS + rest.length);
        }
        for (int i = rest.length - 2; i >= -1; i--) {
            ConsList<V> cons;
            if (i == -1) {
                cons = first;
            } else {
                if (rest[i] == null) {
                    throw new NullPointerException(ConsUtil.MSG_NULL_CONCAT_ARG_AT_POS + (i + 1));
                }
                cons = rest[i].reverse();
            }
            while (cons != Nil.INSTANCE) {
                result = new ConsListImpl<>(cons.head(), result);
                cons = cons.tail();
            }
        }
        return result;
    }

    /**
     * Returns a {@link Collector} collecting a Stream into a cons list.
     *
     * @param <T> element type
     * @return a collector collecting into a cons list
     */
    @NonNull
    static <T> Collector<T, ?, ConsList<T>> toConsCollector() {
        return Collectors.collectingAndThen(Collectors.toList(), ConsList::consList);
    }

    /**
     * Constructs a new <tt>IntConsList</tt> with elements <tt>head</tt> and <tt>tail</tt>.
     *
     * <p>The head, the tail elements and the resulting list have the same compile-time type.
     *
     * <p>The original list is not modified.
     *
     * @param head first element of the new list
     * @param tail <tt>ConsList</tt> with the second and consecutive elements of the new list
     * @return a cons list with the given head and tail elements
     */
    @NonNull
    static IntConsList<Integer> intCons(int head, @NonNull IntConsList<Integer> tail) {
        return new IntConsListImpl(head, Objects.requireNonNull(tail, ConsUtil.MSG_TAIL_IS_NULL));
    }

    /**
     * Constructs a new <tt>IntConsList</tt> with elements <tt>head</tt> and <tt>tail</tt>.
     *
     * <p>The head, the tail elements and the resulting list have the same compile-time type.
     *
     * <p><tt>tail</tt> collection, if not of type <tt>ConsList</tt>, will be converted to it.
     *
     * <p>The original list is not modified.
     *
     * @param head first element of the new list
     * @param tail collection of second and consecutive elements of the new list;
     * @return a cons list with the given head and tail elements
     */
    @NonNull
    static IntConsList<Integer> intCons(int head, @NonNull Collection<Integer> tail) {
        return new IntConsListImpl(head, intConsList(tail));
    }

    /**
     * Constructs a new <tt>IntConsList</tt> containing elements in the given order.
     *
     * <p>An invocation with an empty parameter list will produce the empty
     * list instance <tt>nil()</tt>.
     *
     * <p>With a non-empty parameter array, its first element is the head of the new cons list,
     * the rest are the parameters for constructing its tail.
     *
     * @param elements any number of elements of the list to be constructed
     * @return the cons list with the elements consList the argument array in the same order
     */
    @NonNull
    static IntConsList<Integer> intList(@NonNull int... elements) {
        IntConsList<Integer> cons = nil();
        for (int i = elements.length - 1; i >= 0; i--) {
            cons = new IntConsListImpl(elements[i], cons);
        }
        return cons;
    }

    /**
     * Constructs a new <tt>IntConsList</tt> from the given {@link Iterable}.
     *
     * <p>If the {@link Iterable} is itself a ConsList, returns the typecast iterable.
     *
     * <p>For instances of {@link List}, only one iteration backwards through the list's
     * {@link ListIterator} will be done.
     *
     * <p>For all other {@link Iterable} types, this traverses both the Iterable
     * and the intermediate ConsList once.
     *
     * @param iterable input iterable
     * @return the cons list with the elements consList the Iterable in the same order
     */
    @NonNull
    static IntConsList<Integer> intConsList(@NonNull Iterable<Integer> iterable) {
        Objects.requireNonNull(iterable, ConsUtil.MSG_ITERABLE_IS_NULL);
        if (iterable instanceof IntConsList) {
            return (IntConsList<Integer>) iterable;
        }
        IntConsList<Integer> cons = nil();
        if (iterable instanceof List) {
            List<Integer> list = (List<Integer>) iterable;
            ListIterator<Integer> iter = list.listIterator(list.size());
            while (iter.hasPrevious()) {
                cons = new IntConsListImpl(iter.previous(), cons);
            }
            return cons;
        } else {
            for (Integer v : iterable) {
                cons = new IntConsListImpl(v, cons);
            }
            return cons.intReverse();
        }
    }

    /**
     * Returns an <tt>IntConsList</tt> that contains the concatenation of elements of all argument IntConsLists.
     *
     * @param first the first argument cons list, not nullable
     * @param rest the rest of the argument cons lists, each of them not nullable
     * @return the concatentation of all argument lists
     */
    @NonNull
    @SafeVarargs
    static IntConsList<Integer> concat(@NonNull IntConsList<Integer> first, @NonNull IntConsList<Integer>... rest) {
        Objects.requireNonNull(first, ConsUtil.MSG_NULL_CONCAT_ARG_AT_POS_0);
        Objects.requireNonNull(rest, ConsUtil.MSG_ARG_ARRAY_REST_IS_NULL);
        if (rest.length == 0) {
            return first;
        }
        IntConsList<Integer> result = rest[rest.length - 1];
        if (result == null) {
            throw new NullPointerException(ConsUtil.MSG_NULL_CONCAT_ARG_AT_POS + rest.length);
        }
        for (int i = rest.length - 2; i >= -1; i--) {
            IntConsList<Integer> cons;
            if (i == -1) {
                cons = first;
            } else {
                if (rest[i] == null) {
                    throw new NullPointerException(ConsUtil.MSG_NULL_CONCAT_ARG_AT_POS + (i + 1));
                }
                cons = rest[i].intReverse();
            }
            while (cons != Nil.INSTANCE) {
                result = new IntConsListImpl(cons.intHead(), result);
                cons = cons.intTail();
            }
        }
        return result;
    }

    /**
     * Constructs a new <tt>LongConsList</tt> with elements <tt>head</tt> and <tt>tail</tt>.
     *
     * <p>The head, the tail elements and the resulting list have the same compile-time type.
     *
     * <p>The original list is not modified.
     *
     * @param head first element of the new list
     * @param tail <tt>ConsList</tt> with the second and consecutive elements of the new list
     * @return a cons list with the given head and tail elements
     */
    @NonNull
    static LongConsList<Long> longCons(long head, @NonNull LongConsList<Long> tail) {
        return new LongConsListImpl(head, Objects.requireNonNull(tail, ConsUtil.MSG_TAIL_IS_NULL));
    }

    /**
     * Constructs a new LongConsList with elements <tt>head</tt> and <tt>tail</tt>.
     *
     * <p>The head, the tail elements and the resulting list have the same compile-time type.
     *
     * <p><tt>tail</tt> collection, if not of type <tt>ConsList</tt>, will be converted to it.
     *
     * <p>The original list is not modified.
     *
     * @param head first element of the new list
     * @param tail collection of second and consecutive elements of the new list;
     * @return a cons list with the given head and tail elements
     */
    @NonNull
    static LongConsList<Long> longCons(long head, @NonNull Collection<Long> tail) {
        return new LongConsListImpl(head, longConsList(tail));
    }

    /**
     * Constructs a new <tt>LongConsList</tt> containing elements in the given order.
     *
     * <p>An invocation with an empty parameter list will produce the empty
     * list instance <tt>nil()</tt>.
     *
     * <p>With a non-empty parameter array, its first element is the head of the new cons list,
     * the rest are the parameters for constructing its tail.
     *
     * @param elements any number of elements of the list to be constructed
     * @return the cons list with the elements consList the argument array in the same order
     */
    @NonNull
    static LongConsList<Long> longList(@NonNull long... elements) {
        LongConsList<Long> cons = nil();
        for (int i = elements.length - 1; i >= 0; i--) {
            cons = new LongConsListImpl(elements[i], cons);
        }
        return cons;
    }

    /**
     * Constructs a new <tt>LongConsList</tt> from the given {@link Iterable}.
     *
     * <p>If the {@link Iterable} is itself a ConsList, returns the typecast iterable.
     *
     * <p>For instances of {@link List}, only one iteration backwards through the list's
     * {@link ListIterator} will be done.
     *
     * <p>For all other {@link Iterable} types, this traverses both the Iterable
     * and the intermediate ConsList once.
     *
     * @param iterable input iterable
     * @return the cons list with the elements consList the Iterable in the same order
     */
    @NonNull
    static LongConsList<Long> longConsList(@NonNull Iterable<Long> iterable) {
        Objects.requireNonNull(iterable, ConsUtil.MSG_ITERABLE_IS_NULL);
        if (iterable instanceof LongConsList) {
            return (LongConsList<Long>) iterable;
        }
        LongConsList<Long> cons = nil();
        if (iterable instanceof List) {
            List<Long> list = (List<Long>) iterable;
            ListIterator<Long> iter = list.listIterator(list.size());
            while (iter.hasPrevious()) {
                cons = new LongConsListImpl(iter.previous(), cons);
            }
            return cons;
        } else {
            for (Long v : iterable) {
                cons = new LongConsListImpl(v, cons);
            }
            return cons.longReverse();
        }
    }

    /**
     * Returns a <tt>LongConsList</tt> that contains the concatenation of elements of all argument LongConsLists.
     *
     * @param first the first argument cons list, not nullable
     * @param rest the rest of the argument cons lists, each of them not nullable
     * @return the concatentation of all argument lists
     */
    @NonNull
    @SafeVarargs
    static LongConsList<Long> concat(@NonNull LongConsList<Long> first, @NonNull LongConsList<Long>... rest) {
        Objects.requireNonNull(first, ConsUtil.MSG_NULL_CONCAT_ARG_AT_POS_0);
        Objects.requireNonNull(rest, ConsUtil.MSG_ARG_ARRAY_REST_IS_NULL);
        if (rest.length == 0) {
            return first;
        }
        LongConsList<Long> result = rest[rest.length - 1];
        if (result == null) {
            throw new NullPointerException(ConsUtil.MSG_NULL_CONCAT_ARG_AT_POS + rest.length);
        }
        for (int i = rest.length - 2; i >= -1; i--) {
            LongConsList<Long> cons;
            if (i == -1) {
                cons = first;
            } else {
                if (rest[i] == null) {
                    throw new NullPointerException(ConsUtil.MSG_NULL_CONCAT_ARG_AT_POS + (i + 1));
                }
                cons = rest[i].longReverse();
            }
            while (cons != Nil.INSTANCE) {
                result = new LongConsListImpl(cons.longHead(), result);
                cons = cons.longTail();
            }
        }
        return result;
    }

    /**
     * Constructs a new <tt>DoubleConsList</tt> with elements <tt>head</tt> and <tt>tail</tt>.
     *
     * <p>The head, the tail elements and the resulting list have the same compile-time type.
     *
     * <p>The original list is not modified.
     *
     * @param head first element of the new list
     * @param tail <tt>ConsList</tt> with the second and consecutive elements of the new list
     * @return a cons list with the given head and tail elements
     */
    @NonNull
    static DoubleConsList<Double> doubleCons(double head, @NonNull DoubleConsList<Double> tail) {
        return new DoubleConsListImpl(head, Objects.requireNonNull(tail, ConsUtil.MSG_TAIL_IS_NULL));
    }

    /**
     * Constructs a new <tt>DoubleConsList</tt> with elements <tt>head</tt> and <tt>tail</tt>.
     *
     * <p>The head, the tail elements and the resulting list have the same compile-time type.
     *
     * <p><tt>tail</tt> collection, if not of type <tt>ConsList</tt>, will be converted to it.
     *
     * <p>The original list is not modified.
     *
     * @param head first element of the new list
     * @param tail collection of second and consecutive elements of the new list;
     * @return a cons list with the given head and tail elements
     */
    @NonNull
    static DoubleConsList<Double> doubleCons(double head, @NonNull Collection<Double> tail) {
        return new DoubleConsListImpl(head, doubleConsList(tail));
    }

    /**
     * Constructs a new <tt>DoubleConsList</tt> containing elements in the given order.
     *
     * <p>An invocation with an empty parameter list will produce the empty
     * list instance <tt>nil()</tt>.
     *
     * <p>With a non-empty parameter array, its first element is the head of the new cons list,
     * the rest are the parameters for constructing its tail.
     *
     * @param elements any number of elements of the list to be constructed
     * @return the cons list with the elements consList the argument array in the same order
     */
    @NonNull
    static DoubleConsList<Double> doubleList(@NonNull double... elements) {
        DoubleConsList<Double> cons = nil();
        for (int i = elements.length - 1; i >= 0; i--) {
            cons = new DoubleConsListImpl(elements[i], cons);
        }
        return cons;
    }

    /**
     * Constructs a new <tt>DoubleConsList</tt> from the given {@link Iterable}.
     *
     * <p>If the {@link Iterable} is itself a ConsList, returns the typecast iterable.
     *
     * <p>For instances of {@link List}, only one iteration backwards through the list's
     * {@link ListIterator} will be done.
     *
     * <p>For all other {@link Iterable} types, this traverses both the Iterable
     * and the intermediate ConsList once.
     *
     * @param iterable input iterable
     * @return the cons list with the elements consList the Iterable in the same order
     */
    @NonNull
    static DoubleConsList<Double> doubleConsList(@NonNull Iterable<Double> iterable) {
        Objects.requireNonNull(iterable, ConsUtil.MSG_ITERABLE_IS_NULL);
        if (iterable instanceof DoubleConsList) {
            return (DoubleConsList<Double>) iterable;
        }
        DoubleConsList<Double> cons = nil();
        if (iterable instanceof List) {
            List<Double> list = (List<Double>) iterable;
            ListIterator<Double> iter = list.listIterator(list.size());
            while (iter.hasPrevious()) {
                cons = new DoubleConsListImpl(iter.previous(), cons);
            }
            return cons;
        } else {
            for (Double v : iterable) {
                cons = new DoubleConsListImpl(v, cons);
            }
            return cons.doubleReverse();
        }
    }

    /**
     * Returns a <tt>DoubleConsList</tt> that contains the concatenation of elements of all argument DoubleConsLists.
     *
     * @param first the first argument cons list, not nullable
     * @param rest the rest of the argument cons lists, each of them not nullable
     * @return the concatentation of all argument lists
     */
    @NonNull
    @SafeVarargs
    static DoubleConsList<Double> concat(@NonNull DoubleConsList<Double> first, @NonNull DoubleConsList<Double>... rest) {
        Objects.requireNonNull(first, "Null concat argument at position 0");
        Objects.requireNonNull(rest, ConsUtil.MSG_ARG_ARRAY_REST_IS_NULL);
        if (rest.length == 0) {
            return first;
        }
        DoubleConsList<Double> result = rest[rest.length - 1];
        if (result == null) {
            throw new NullPointerException(ConsUtil.MSG_NULL_CONCAT_ARG_AT_POS + rest.length);
        }
        for (int i = rest.length - 2; i >= -1; i--) {
            DoubleConsList<Double> cons;
            if (i == -1) {
                cons = first;
            } else {
                if (rest[i] == null) {
                    throw new NullPointerException(ConsUtil.MSG_NULL_CONCAT_ARG_AT_POS + (i + 1));
                }
                cons = rest[i].doubleReverse();
            }
            while (cons != Nil.INSTANCE) {
                result = new DoubleConsListImpl(cons.doubleHead(), result);
                cons = cons.doubleTail();
            }
        }
        return result;
    }

    /**
     * Returns the first element of the <tt>ConsList</tt>
     *
     * <p>Throws {@link NoSuchElementException} if the list is empty.
     *
     * @return first element of the list
     */
    E head();

    /**
     * Returns another <tt>ConsList</tt> containing the current list's elements after the first one.
     *
     * <p>Throws {@link NoSuchElementException} if the list is empty.
     *
     * @return elements of the list after the first one
     */
    @NonNull
    ConsList<E> tail();

    /**
     * Constructs a new <tt>ConsList</tt> with the elements of the current one in the reverse order.
     *
     * @return a new list with elements in reversed order
     */
    @NonNull
    ConsList<E> reverse();
}
