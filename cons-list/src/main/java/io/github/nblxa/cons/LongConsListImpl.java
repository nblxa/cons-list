package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

@Immutable
@ThreadSafe
public final class LongConsListImpl extends AbstractCollection<Long>
                             implements LongConsList<Long>, Serializable {
    private static final long serialVersionUID = 5261195696436478859L;
    private long head;
    @NonNull
    private LongConsList<Long> tail;

    LongConsListImpl(long head, @NonNull LongConsList<Long> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public long longHead() {
        return head;
    }

    @NonNull
    @Override
    public LongConsList<Long> longTail() {
        return tail;
    }

    @NonNull
    @Override
    public LongConsList<Long> longReverse() {
        LongConsList<Long> result = ConsList.nil();
        LongConsList<Long> cons = this;
        while (cons != Nil.INSTANCE) {
            result = new LongConsListImpl(cons.longHead(), result);
            cons = cons.longTail();
        }
        return result;
    }

    @NonNull
    @Override
    public Long head() {
        return head;
    }

    @NonNull
    @Override
    public ConsList<Long> tail() {
        return tail;
    }

    @NonNull
    @Override
    public ConsList<Long> reverse() {
        return longReverse();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        int size = ConsUtil.EMPTY_SIZE;
        LongConsList<Long> cons = this;
        while (cons != Nil.INSTANCE && size != Integer.MAX_VALUE) {
            size++;
            cons = cons.longTail();
        }
        return size;
    }

    @NonNull
    @Override
    public Iterator<Long> iterator() {
        return longIterator();
    }

    @NonNull
    @Override
    public Spliterator<Long> spliterator() {
        return longSpliterator();
    }

    @NonNull
    @Override
    public PrimitiveIterator.OfLong longIterator() {
        return new LongConsIterator(this);
    }

    @NonNull
    @Override
    public Spliterator.OfLong longSpliterator() {
        return Spliterators.spliteratorUnknownSize(longIterator(), ConsUtil.SPLITERATOR_CHARACTERISTICS);
    }

    @NonNull
    @Override
    public LongStream longStream() {
        return StreamSupport.longStream(longSpliterator(), false);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof LongConsList) {
            return ConsUtil.haveEqualElements(this, (LongConsList<?>) o);
        }
        if (!(o instanceof ConsList)) {
            return false;
        }
        return ConsUtil.haveEqualElements(this, (ConsList<?>) o);
    }

    @Override
    public final int hashCode() {
        int result = 1;
        LongConsList<Long> cons = this;
        while (cons != Nil.INSTANCE) {
            long h = cons.longHead();
            result = 31 * result + Long.hashCode(h);
            cons = cons.longTail();
        }
        return result;
    }

    private static final class LongConsIterator implements PrimitiveIterator.OfLong {
        private LongConsList<Long> cons;

        private LongConsIterator(LongConsList<Long> cons) {
            this.cons = cons;
        }

        @Override
        public boolean hasNext() {
            return cons != Nil.INSTANCE;
        }

        @Override
        public long nextLong() {
            long next = cons.longHead();
            cons = cons.longTail();
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
        LongConsList<Long> reversed = ConsList.nil();
        LongConsList<Long> cons = this;
        long length = 0L;
        while (cons != Nil.INSTANCE) {
            reversed = new LongConsListImpl(cons.longHead(), reversed);
            cons = cons.longTail();
            length++; // Can overflow but it is very unpractical to check.
        }
        out.writeLong(length);
        long pos = 0L;
        PrimitiveIterator.OfLong iter = reversed.longIterator();
        while (iter.hasNext()) {
            long elem = iter.nextLong();
            try {
                out.writeLong(elem);
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
        LongConsList<Long> cons = ConsList.nil();
        long length = in.readLong();
        for (long l = length; l != 0; l--) {
            try {
                long elem = in.readLong();
                cons = ConsList.longCons(elem, cons);
            } catch (Exception e) {
                throw new ConsSerializationException(
                    "Could not de-serialize element at 0-based position: " + (l - 1), e);
            }
        }
        this.head = cons.longHead();
        this.tail = cons.longTail();
    }
}
