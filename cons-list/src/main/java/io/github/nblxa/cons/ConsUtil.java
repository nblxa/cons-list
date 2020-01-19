package io.github.nblxa.cons;

import java.util.Objects;
import java.util.Spliterator;

final class ConsUtil {
    private ConsUtil() {
        throw new UnsupportedOperationException();
    }

    static final int SPLITERATOR_CHARACTERISTICS = Spliterator.ORDERED | Spliterator.IMMUTABLE;
    static final String MSG_TAIL_IS_NULL = "tail is null";
    static final String MSG_ITERABLE_IS_NULL = "iterable is null";
    static final String MSG_NULL_CONCAT_ARG_AT_POS = "Null concat argument at position ";
    static final String MSG_NULL_CONCAT_ARG_AT_POS_0 = MSG_NULL_CONCAT_ARG_AT_POS + "0";
    static final String MSG_ARG_ARRAY_REST_IS_NULL = "Argument array rest is null";
    static final String MSG_USE_SERIALIZATION_PROXY = "Use serialization proxy!";
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
            if (first.intHead() != second.intHead()) {
                return false;
            }
            first = first.intTail();
            second = second.intTail();
        }
        return first == Nil.INSTANCE && second == Nil.INSTANCE;
    }

    static <V, U> boolean haveEqualElements(LongConsList<V> first, LongConsList<U> second) {
        while (first != Nil.INSTANCE && second != Nil.INSTANCE) {
            if (first.longHead() != second.longHead()) {
                return false;
            }
            first = first.longTail();
            second = second.longTail();
        }
        return first == Nil.INSTANCE && second == Nil.INSTANCE;
    }

    static <V, U> boolean haveEqualElements(DoubleConsList<V> first, DoubleConsList<U> second) {
        while (first != Nil.INSTANCE && second != Nil.INSTANCE) {
            if (Double.compare(first.doubleHead(), second.doubleHead()) != 0) {
                return false;
            }
            first = first.doubleTail();
            second = second.doubleTail();
        }
        return first == Nil.INSTANCE && second == Nil.INSTANCE;
    }
}
