package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.Serializable;
import java.util.*;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

@Immutable
@ThreadSafe
final class LongConsListImpl extends AbstractCollection<Long>
                             implements LongConsList<Long>, Serializable {
    private static final long serialVersionUID = 5261195696436478859L;
    private final long head;
    @NonNull
    private final LongConsList<Long> tail;

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
}
