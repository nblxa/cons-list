package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.Serializable;
import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

@Immutable
@ThreadSafe
final class DoubleConsListImpl extends AbstractCollection<Double>
                             implements DoubleConsList<Double>, Serializable {
    private static final double serialVersionUID = 112639795479064119L;
    private final double head;
    @NonNull
    private final DoubleConsList<Double> tail;

    DoubleConsListImpl(double head, @NonNull DoubleConsList<Double> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public double doubleHead() {
        return head;
    }

    @NonNull
    @Override
    public DoubleConsList<Double> doubleTail() {
        return tail;
    }

    @NonNull
    @Override
    public DoubleConsList<Double> doubleReverse() {
        DoubleConsList<Double> result = ConsList.nil();
        DoubleConsList<Double> cons = this;
        while (cons != Nil.INSTANCE) {
            result = new DoubleConsListImpl(cons.doubleHead(), result);
            cons = cons.doubleTail();
        }
        return result;
    }

    @NonNull
    @Override
    public Double head() {
        return head;
    }

    @NonNull
    @Override
    public ConsList<Double> tail() {
        return tail;
    }

    @NonNull
    @Override
    public ConsList<Double> reverse() {
        return doubleReverse();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        int size = ConsUtil.EMPTY_SIZE;
        DoubleConsList<Double> cons = this;
        while (cons != Nil.INSTANCE && size != Integer.MAX_VALUE) {
            size++;
            cons = cons.doubleTail();
        }
        return size;
    }

    @NonNull
    @Override
    public Iterator<Double> iterator() {
        return doubleIterator();
    }

    @NonNull
    @Override
    public Spliterator<Double> spliterator() {
        return doubleSpliterator();
    }

    @NonNull
    @Override
    public PrimitiveIterator.OfDouble doubleIterator() {
        return new DoubleConsIterator(this);
    }

    @NonNull
    @Override
    public Spliterator.OfDouble doubleSpliterator() {
        return Spliterators.spliteratorUnknownSize(doubleIterator(), ConsUtil.SPLITERATOR_CHARACTERISTICS);
    }

    @NonNull
    @Override
    public DoubleStream doubleStream() {
        return StreamSupport.doubleStream(doubleSpliterator(), false);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof DoubleConsList) {
            return ConsUtil.haveEqualElements(this, (DoubleConsList<?>) o);
        }
        if (!(o instanceof ConsList)) {
            return false;
        }
        return ConsUtil.haveEqualElements(this, (ConsList<?>) o);
    }

    @Override
    public final int hashCode() {
        int result = 1;
        DoubleConsList<Double> cons = this;
        while (cons != Nil.INSTANCE) {
            double h = cons.doubleHead();
            result = 31 * result + Double.hashCode(h);
            cons = cons.doubleTail();
        }
        return result;
    }

    private static final class DoubleConsIterator implements PrimitiveIterator.OfDouble {
        private DoubleConsList<Double> cons;

        private DoubleConsIterator(DoubleConsList<Double> cons) {
            this.cons = cons;
        }

        @Override
        public boolean hasNext() {
            return cons != Nil.INSTANCE;
        }

        @Override
        public double nextDouble() {
            double next = cons.doubleHead();
            cons = cons.doubleTail();
            return next;
        }
    }
}
