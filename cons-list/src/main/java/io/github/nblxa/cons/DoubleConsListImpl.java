package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

@Immutable
@ThreadSafe
public final class DoubleConsListImpl extends AbstractCollection<Double>
                             implements DoubleConsList<Double>, Serializable {
    private static final double serialVersionUID = 112639795479064119L;
    private double head;
    @NonNull
    private DoubleConsList<Double> tail;

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

    /**
     * Serialization to an ObjectOutputStream is implemented non-recursively in order
     * to avoid the {@link StackOverflowError} on long lists.
     *
     * ConsList implementations are reversed before serializing. This way, one particular
     * use case of serialization is optimized: write-once, read-many. For instance, this could be
     * serialization on disk in order to save application state that can be restored multiple times.
     *
     * List length is calculated and stored in long value before serializing the list. Its size should
     * be enough for all practical concerns. Lists whose size exceed the amount of values of the long
     * type will take practically forever to iterate, not to mention the memory requirements.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        DoubleConsList<Double> reversed = ConsList.nil();
        DoubleConsList<Double> cons = this;
        long length = 0L;
        while (cons != Nil.INSTANCE) {
            reversed = new DoubleConsListImpl(cons.doubleHead(), reversed);
            cons = cons.doubleTail();
            length++; // Can overflow but it is very unpractical to check.
        }
        out.writeLong(length);
        long pos = 0L;
        PrimitiveIterator.OfDouble iter = reversed.doubleIterator();
        while (iter.hasNext()) {
            double elem = iter.nextDouble();
            try {
                out.writeDouble(elem);
                pos++;
            } catch (Exception e) {
                throw new ConsSerializationException(
                    "Could not serialize element at 0-based position: " +
                        (length - pos - 1L), e);
            }
        }
    }

    /**
     * De-serialize the ConsList from its reversed serialized representation.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        DoubleConsList<Double> cons = ConsList.nil();
        long length = in.readLong();
        for (long l = length; l != 0; l--) {
            try {
                double elem = in.readDouble();
                cons = ConsList.doubleCons(elem, cons);
            } catch (Exception e) {
                throw new ConsSerializationException(
                    "Could not de-serialize element at 0-based position: " + (l - 1), e);
            }
        }
        this.head = cons.doubleHead();
        this.tail = cons.doubleTail();
    }
}
