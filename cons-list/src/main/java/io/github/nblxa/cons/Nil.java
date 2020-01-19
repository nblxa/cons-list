package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.Serializable;
import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Singleton class for the empty <tt>ConsList</tt>.
 */
@Immutable
@ThreadSafe
public final class Nil<E> extends AbstractCollection<E>
                   implements ConsList<E>, IntConsList<E>, LongConsList<E>, DoubleConsList<E>, Serializable {
    private static final long serialVersionUID = -4298182790270344441L;
    static final Nil<?> INSTANCE = new Nil<>();

    private Nil() {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException();
        }
    }

    @NonNull
    @Override
    public E head() {
        throw new NoSuchElementException();
    }

    @NonNull
    @Override
    public ConsList<E> tail() {
        throw new NoSuchElementException();
    }

    @NonNull
    @Override
    public Nil<E> reverse() {
        return this;
    }

    @Override
    public int intHead() {
        throw new NoSuchElementException();
    }

    @NonNull
    @Override
    public IntConsList<E> intTail() {
        throw new NoSuchElementException();
    }

    @NonNull
    @Override
    public IntConsList<E> intReverse() {
        return this;
    }

    @NonNull
    @Override
    public PrimitiveIterator.OfInt intIterator() {
        return new PrimitiveIterator.OfInt() {
            @Override
            public int nextInt() {
                throw new NoSuchElementException();
            }

            @Override
            public boolean hasNext() {
                return false;
            }
        };
    }

    @NonNull
    @Override
    public Spliterator.OfInt intSpliterator() {
        return Spliterators.emptyIntSpliterator();
    }

    @NonNull
    @Override
    public IntStream intStream() {
        return IntStream.empty();
    }

    @Override
    public long longHead() {
        throw new NoSuchElementException();
    }

    @NonNull
    @Override
    public LongConsList<E> longTail() {
        throw new NoSuchElementException();
    }

    @NonNull
    @Override
    public LongConsList<E> longReverse() {
        return this;
    }

    @NonNull
    @Override
    public PrimitiveIterator.OfLong longIterator() {
        return new PrimitiveIterator.OfLong() {
            @Override
            public long nextLong() {
                throw new NoSuchElementException();
            }

            @Override
            public boolean hasNext() {
                return false;
            }
        };
    }

    @NonNull
    @Override
    public Spliterator.OfLong longSpliterator() {
        return Spliterators.emptyLongSpliterator();
    }

    @NonNull
    @Override
    public LongStream longStream() {
        return LongStream.empty();
    }

    @Override
    public double doubleHead() {
        throw new NoSuchElementException();
    }

    @NonNull
    @Override
    public DoubleConsList<E> doubleTail() {
        throw new NoSuchElementException();
    }

    @NonNull
    @Override
    public DoubleConsList<E> doubleReverse() {
        return this;
    }

    @NonNull
    @Override
    public PrimitiveIterator.OfDouble doubleIterator() {
        return new PrimitiveIterator.OfDouble() {
            @Override
            public double nextDouble() {
                throw new NoSuchElementException();
            }

            @Override
            public boolean hasNext() {
                return false;
            }
        };
    }

    @NonNull
    @Override
    public Spliterator.OfDouble doubleSpliterator() {
        return Spliterators.emptyDoubleSpliterator();
    }

    @NonNull
    @Override
    public DoubleStream doubleStream() {
        return DoubleStream.empty();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int size() {
        return 0;
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Nil;
    }

    /**
     * Singleton instance control for deserialization.
     */
    private Object readResolve() {
        return INSTANCE;
    }
}
