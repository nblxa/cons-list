package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Immutable
@ThreadSafe
final class IntConsListImpl extends AbstractCollection<Integer>
                      implements IntConsList<Integer>, Serializable {
    private static final long serialVersionUID = 4292942776660892193L;
    private final int head;
    @NonNull
    private final IntConsList<Integer> tail;

    IntConsListImpl(int head, @NonNull IntConsList<Integer> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public int intHead() {
        return head;
    }

    @NonNull
    @Override
    public IntConsList<Integer> intTail() {
        return tail;
    }

    @NonNull
    @Override
    public IntConsList<Integer> intReverse() {
        IntConsList<Integer> result = ConsList.nil();
        IntConsList<Integer> cons = this;
        while (cons != Nil.INSTANCE) {
            result = new IntConsListImpl(cons.intHead(), result);
            cons = cons.intTail();
        }
        return result;
    }

    @NonNull
    @Override
    public Integer head() {
        return head;
    }

    @NonNull
    @Override
    public ConsList<Integer> tail() {
        return tail;
    }

    @NonNull
    @Override
    public ConsList<Integer> reverse() {
        return intReverse();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        int size = ConsUtil.EMPTY_SIZE;
        IntConsList<Integer> cons = this;
        while (cons != Nil.INSTANCE && size != Integer.MAX_VALUE) {
            size++;
            cons = cons.intTail();
        }
        return size;
    }

    @NonNull
    @Override
    public Iterator<Integer> iterator() {
        return intIterator();
    }

    @NonNull
    @Override
    public Spliterator<Integer> spliterator() {
        return intSpliterator();
    }

    @NonNull
    @Override
    public PrimitiveIterator.OfInt intIterator() {
        return new IntConsIterator(this);
    }

    @NonNull
    @Override
    public Spliterator.OfInt intSpliterator() {
        return Spliterators.spliteratorUnknownSize(intIterator(), ConsUtil.SPLITERATOR_CHARACTERISTICS);
    }

    @NonNull
    @Override
    public IntStream intStream() {
        return StreamSupport.intStream(intSpliterator(), false);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof IntConsList) {
            return ConsUtil.haveEqualElements(this, (IntConsList<?>) o);
        }
        if (!(o instanceof ConsList)) {
            return false;
        }
        return ConsUtil.haveEqualElements(this, (ConsList<?>) o);
    }

    @Override
    public final int hashCode() {
        int result = 1;
        IntConsList<Integer> cons = this;
        while (cons != Nil.INSTANCE) {
            int h = cons.intHead();
            result = 31 * result + Integer.hashCode(h);
            cons = cons.intTail();
        }
        return result;
    }

    private static final class IntConsIterator implements PrimitiveIterator.OfInt {
        private IntConsList<Integer> cons;

        private IntConsIterator(IntConsList<Integer> cons) {
            this.cons = cons;
        }

        @Override
        public boolean hasNext() {
            return cons != Nil.INSTANCE;
        }

        @Override
        public int nextInt() {
            int next = cons.intHead();
            cons = cons.intTail();
            return next;
        }
    }
}
