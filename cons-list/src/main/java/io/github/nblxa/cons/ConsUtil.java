package io.github.nblxa.cons;

import java.util.Objects;
import java.util.Spliterator;

final class ConsUtil {
    private ConsUtil() {
        throw new UnsupportedOperationException();
    }

    final static int SPLITERATOR_CHARACTERISTICS = Spliterator.ORDERED | Spliterator.IMMUTABLE;
    final static String MSG_TAIL_IS_NULL = "tail is null";
    final static String MSG_ITERABLE_IS_NULL = "iterable is null";
    final static String MSG_NULL_CONCAT_ARG_AT_POS = "Null concat argument at position ";
    final static String MSG_NULL_CONCAT_ARG_AT_POS_0 = MSG_NULL_CONCAT_ARG_AT_POS + "0";
    final static String MSG_ARG_ARRAY_REST_IS_NULL = "Argument array rest is null";
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
