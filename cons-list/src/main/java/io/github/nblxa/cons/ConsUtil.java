package io.github.nblxa.cons;

import java.util.Objects;
import java.util.Spliterator;

final class ConsUtil {
    private ConsUtil() {
        throw new UnsupportedOperationException();
    }

    final static int SPLITERATOR_CHARACTERISTICS = Spliterator.ORDERED | Spliterator.IMMUTABLE;
    static int EMPTY_SIZE = 0;

    static <V, U> boolean haveEqualElements(ConsList<V> first, ConsList<U> second) {
        while (first != Nil.INSTANCE && second != Nil.INSTANCE) {
            if (!Objects.equals(first.head(), second.head())) {
                return false;
            }
            first = first.tail();
            second = second.tail();
        }
        return first == Nil.INSTANCE && second == Nil.INSTANCE;
    }

    static <V, U> boolean haveEqualElements(IntConsList<V> first, IntConsList<U> second) {
        while (first != Nil.INSTANCE && second != Nil.INSTANCE) {
            if (!Objects.equals(first.head(), second.head())) {
                return false;
            }
            first = first.intTail();
            second = second.intTail();
        }
        return first == Nil.INSTANCE && second == Nil.INSTANCE;
    }

    static <V, U> boolean haveEqualElements(LongConsList<V> first, LongConsList<U> second) {
        while (first != Nil.INSTANCE && second != Nil.INSTANCE) {
            if (!Objects.equals(first.head(), second.head())) {
                return false;
            }
            first = first.longTail();
            second = second.longTail();
        }
        return first == Nil.INSTANCE && second == Nil.INSTANCE;
    }

    static <V, U> boolean haveEqualElements(DoubleConsList<V> first, DoubleConsList<U> second) {
        while (first != Nil.INSTANCE && second != Nil.INSTANCE) {
            if (!Objects.equals(first.head(), second.head())) {
                return false;
            }
            first = first.doubleTail();
            second = second.doubleTail();
        }
        return first == Nil.INSTANCE && second == Nil.INSTANCE;
    }
}
