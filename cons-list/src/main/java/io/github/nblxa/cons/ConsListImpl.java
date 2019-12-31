package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.Serializable;
import java.util.*;

@Immutable
@ThreadSafe
final class ConsListImpl<E> extends AbstractCollection<E> implements Serializable, ConsList<E> {
    private static final long serialVersionUID = -2746754218342304128L;
    private final E head;
    @NonNull
    private final ConsList<E> tail;

    ConsListImpl(E head, @NonNull ConsList<E> tail) {
        this.tail = tail;
        this.head = head;
    }

    @Override
    public E head() {
        return head;
    }

    @Override
    @NonNull
    public ConsList<E> tail() {
        return tail;
    }

    @Override
    @NonNull
    public ConsList<E> reverse() {
        ConsList<E> result = ConsList.nil();
        ConsList<E> cons = this;
        while (cons != Nil.INSTANCE) {
            result = new ConsListImpl<>(cons.head(), result);
            cons = cons.tail();
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        int size = ConsUtil.EMPTY_SIZE;
        ConsList<E> cons = this;
        while (cons != Nil.INSTANCE && size != Integer.MAX_VALUE) {
            size++;
            cons = cons.tail();
        }
        return size;
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return new ConsIterator<>(this);
    }

    @NonNull
    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), ConsUtil.SPLITERATOR_CHARACTERISTICS);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsList)) {
            return false;
        }
        return ConsUtil.haveEqualElements(this, (ConsList<?>) o);
    }

    @Override
    public final int hashCode() {
        int result = 1;
        ConsList<E> cons = this;
        while (cons != Nil.INSTANCE) {
            E h = cons.head();
            result = 31 * result + (h == null ? 0 : h.hashCode());
            cons = cons.tail();
        }
        return result;
    }

    private static final class ConsIterator<E> implements Iterator<E> {
        private ConsList<E> cons;

        private ConsIterator(ConsList<E> cons) {
            this.cons = cons;
        }

        @Override
        public boolean hasNext() {
            return cons != Nil.INSTANCE;
        }

        @Override
        public E next() {
            E next = cons.head();
            cons = cons.tail();
            return next;
        }
    }
}
