package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

@Immutable
@ThreadSafe
public final class ConsListImpl<E> extends AbstractCollection<E> implements Serializable, ConsList<E> {
    private static final long serialVersionUID = -2746754218342304128L;
    private E head;
    @NonNull
    private ConsList<E> tail;

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
        ConsList<E> reversed = ConsList.nil();
        ConsList<E> cons = this;
        long length = 0L;
        while (cons != Nil.INSTANCE) {
            reversed = new ConsListImpl<>(cons.head(), reversed);
            cons = cons.tail();
            length++; // Can overflow but it is very unpractical to check.
        }
        out.writeLong(length);
        long pos = 0L;
        for (E elem: reversed) {
            try {
                out.writeObject(elem);
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
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ConsList<E> cons = ConsList.nil();
        long length = in.readLong();
        for (long l = length; l != 0; l--) {
            try {
                E elem = (E) in.readObject();
                cons = ConsList.cons(elem, cons);
            } catch (Exception e) {
                throw new ConsSerializationException(
                    "Could not de-serialize element at 0-based position: " + (l - 1), e);
            }
        }
        this.head = cons.head();
        this.tail = cons.tail();
    }
}
