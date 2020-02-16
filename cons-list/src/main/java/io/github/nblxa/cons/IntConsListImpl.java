package io.github.nblxa.cons;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Immutable
@ThreadSafe
public final class IntConsListImpl extends AbstractCollection<Integer>
                      implements IntConsList<Integer>, Serializable {
    private static final long serialVersionUID = 4408088640009974148L;
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
    public IntConsList<Integer> intMap(@NonNull IntUnaryOperator mapper) {
        IntConsList<Integer> result = ConsList.nil();
        IntConsList<Integer> cons = this;
        while (cons != Nil.INSTANCE) {
            result = new IntConsListImpl(mapper.applyAsInt(cons.intHead()), result);
            cons = cons.intTail();
        }
        return result.intReverse();
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

    @NonNull
    @Override
    public <T> ConsList<T> map(@NonNull Function<? super Integer, ? extends T> mapper) {
        return ConsUtil.map(this, mapper);
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

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(ObjectInputStream in) {
        throw new UnsupportedOperationException(ConsUtil.MSG_USE_SERIALIZATION_PROXY);
    }

    /**
     * Serialization proxy pattern.
     *
     * This makes sure instances of the IntConsListImpl class only get created by constructors,
     * and its fields can be made final.
     *
     * @serial
     */
    private static final class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 7879963871389736253L;
        private transient IntConsList<Integer> list;

        private SerializationProxy(IntConsList<Integer> list) {
            this.list = list;
        }

        /**
         * Serialization to an ObjectOutputStream is implemented non-recursively in order
         * to avoid the {@link StackOverflowError} on long lists.
         * <p>
         * ConsList implementations are reversed before serializing. This way, one particular
         * use case of serialization is optimized: write-once, read-many. For instance, this could be
         * serialization on disk in order to save application state that can be restored multiple times.
         * <p>
         * List length is calculated and stored in long value before serializing the list. Its size should
         * be enough for all practical concerns. Lists whose size exceed the amount of values of the long
         * type will take practically forever to iterate, not to mention the memory requirements.
         */
        private void writeObject(ObjectOutputStream out) throws IOException {
            IntConsList<Integer> reversed = ConsList.nil();
            IntConsList<Integer> cons = list;
            long length = 0L;
            while (cons != Nil.INSTANCE) {
                reversed = new IntConsListImpl(cons.intHead(), reversed);
                cons = cons.intTail();
                length++; // Can overflow but it is very unpractical to check.
            }
            out.writeLong(length);
            long pos = 0L;
            PrimitiveIterator.OfInt iter = reversed.intIterator();
            while (iter.hasNext()) {
                int elem = iter.nextInt();
                try {
                    out.writeInt(elem);
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
            IntConsList<Integer> cons = ConsList.nil();
            long length = in.readLong();
            for (long l = length; l != 0; l--) {
                try {
                    int elem = in.readInt();
                    cons = ConsList.intCons(elem, cons);
                } catch (Exception e) {
                    throw new ConsSerializationException(
                        "Could not de-serialize element at 0-based position: " + (l - 1), e);
                }
            }
            list = cons;
        }

        /**
         * Serialization proxy pattern: resolve the proxy into the ConsList when de-serializing.
         * @return the de-serialized ConsList.
         */
        private Object readResolve() {
            return list;
        }
    }
}
