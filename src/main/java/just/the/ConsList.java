package just.the;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The ultimate immutable and thread-safe Cons List that implements {@link java.util.Collection}
 * and does not consume the stack.
 *
 * @param <E> element type
 */
@Immutable
@ThreadSafe
public final class ConsList<E> implements Collection<E> {
    private static ConsList NIL = new ConsList<Void>(null, null, 0);

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> ConsList<T> nil() {
        return (ConsList<T>) NIL;
    }

    @NonNull
    public static <V> ConsList<V> cons(V head) {
        return new ConsList<>(head, nil(), 1);
    }

    @NonNull
    public static <V> ConsList<V> cons(V head, ConsList<V> tail) {
        return new ConsList<>(head, tail, tail.size + 1);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public static <V, U extends V> ConsList<V> cons(U head, ConsList<? extends V> tail, Class<V> klass) {
        return (ConsList<V>) new ConsList(head, tail, tail.size + 1);
    }

    @NonNull
    @SafeVarargs
    public static <V> ConsList<V> list(V... elements) {
        ConsList<V> target = nil();
        for (int i = elements.length - 1; i >= 0; i--) {
            target = cons(elements[i], target);
        }
        return target;
    }

    private final E head;
    private final ConsList<E> tail;
    private final int size;

    private ConsList(E head, ConsList<E> tail, int size) {
        this.tail = tail;
        this.head = head;
        this.size = size;
    }

    @NonNull
    public E head() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return head;
    }

    @NonNull
    public ConsList<E> tail() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail;
    }

    @NonNull
    public ConsList<E> reverse() {
        ConsList<E> result = nil();
        ConsList<E> target = this;
        while (target.tail != null) {
            result = cons(target.head, result);
            target = target.tail;
        }
        return result;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object o) {
        ConsList<E> target = this;
        while (target.tail != null) {
            if (Objects.equals(o, target.head)) {
                return true;
            }
            target = target.tail;
        }
        return false;
    }

    @NonNull
    public Iterator<E> iterator() {
        return new ConsIterator<>(this);
    }

    @NonNull
    public Object[] toArray() {
        Object[] array = new Object[size];
        ConsList<E> target = this;
        int i = 0;
        while (target.tail != null) {
            array[i] = target.head;
            i++;
            target = target.tail;
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T1> T1[] toArray(T1[] a) {
        if (a.length >= size) {
            ConsList<E> target = this;
            int i = 0;
            while (target.tail != null) {
                try {
                    a[i] = (T1) target.head;
                } catch (ClassCastException cce) {
                    throw new ArrayStoreException();
                }
                i++;
                target = target.tail;
            }

            return a;
        }
        return (T1[]) toArray();
    }

    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o: c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    private static final class ConsIterator<E> implements Iterator<E> {
        private ConsList<E> target;

        private ConsIterator(ConsList<E> target) {
            this.target = target;
        }

        @Override
        public boolean hasNext() {
            return target.tail != null;
        }

        @Override
        public E next() {
            if (target.tail == null) {
                throw new NoSuchElementException();
            }
            E next = target.head;
            target = target.tail;
            return next;
        }
    }
}
