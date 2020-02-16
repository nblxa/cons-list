package io.github.nblxa.cons;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static io.github.nblxa.cons.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class IntConsListTest {

    @Test
    public void nil_isEmpty() {
        IntConsList<Integer> empty = nil();
        assertThat(empty)
            .hasSize(0)
            .isEmpty();
        Throwable t = catchThrowable(empty::intHead);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
        t = catchThrowable(empty::head);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
        t = catchThrowable(empty::intTail);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
        t = catchThrowable(empty::tail);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
    }

    @Test
    public void nil_intConsElement_yieldsSize1() {
        IntConsList<Integer> list = nil();
        list = intCons(42, list);
        assertThat(list)
            .hasSize(1)
            .isNotEmpty();
        assertThat(list.intHead()).isEqualTo(42);
        assertThat(list.head()).isEqualTo(42);
        assertThat(list.intTail()).isEmpty();
        assertThat(list.tail()).isEmpty();
    }

    @Test
    public void size1_intConsElement_yieldsSize2() {
        IntConsList<Integer> list = intList(100500);
        list = intCons(42, list);
        assertThat(list)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(42, 100500);
        assertThat(list.intHead()).isEqualTo(42);
        assertThat(list.head()).isEqualTo(42);
        assertThat(list.intTail()).containsExactly(100500);
        assertThat(list.tail()).containsExactly(100500);
    }

    @Test
    public void size1Collection_intConsElement_yieldsSize2() {
        Collection<Integer> p = intList(100500);
        IntConsList<Integer> list = intCons(42, p);
        assertThat(list)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(42, 100500);
        assertThat(list.intHead()).isEqualTo(42);
        assertThat(list.head()).isEqualTo(42);
        assertThat(list.intTail()).containsExactly(100500);
        assertThat(list.tail()).containsExactly(100500);
    }

    @Test
    public void intIterator() {
        PrimitiveIterator.OfInt iter = intList(42, 13, 1).intIterator();
        assertThat(iter.hasNext()).isTrue();
        assertThat(iter.nextInt()).isEqualTo(42);
        assertThat(iter.hasNext()).isTrue();
        assertThat(iter.nextInt()).isEqualTo(13);
        assertThat(iter.hasNext()).isTrue();
        assertThat(iter.nextInt()).isEqualTo(1);
        assertThat(iter.hasNext()).isFalse();
        Throwable t = catchThrowable(iter::nextInt);
        assertThat(t).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void spliterator_characteristicsAndData() {
        IntConsList<Integer> numbers = intList(1, 1, 2, 3, 5, 8);
        Spliterator<Integer> spliter = numbers.spliterator();

        assertThat(spliter.hasCharacteristics(Spliterator.DISTINCT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SORTED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.ORDERED)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.SIZED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.NONNULL)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.IMMUTABLE)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.CONCURRENT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SUBSIZED)).isFalse();

        List<Integer> list = StreamSupport.stream(spliter, false)
            .collect(Collectors.toList());
        assertThat(list)
            .containsExactly(1, 1, 2, 3, 5, 8);
    }

    @Test
    public void intSpliterator_characteristicsAndData() {
        IntConsList<Integer> numbers = intList(1, 1, 2, 3, 5, 8);
        Spliterator.OfInt spliter = numbers.intSpliterator();

        assertThat(spliter.hasCharacteristics(Spliterator.DISTINCT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SORTED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.ORDERED)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.SIZED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.NONNULL)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.IMMUTABLE)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.CONCURRENT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SUBSIZED)).isFalse();

        List<Integer> list = StreamSupport.stream(spliter, false)
            .collect(Collectors.toList());
        assertThat(list)
            .containsExactly(1, 1, 2, 3, 5, 8);
    }

    @Test
    public void intStream() {
        IntConsList<Integer> numbers = intList(1, 1, 2, 3, 5, 8);
        IntStream is = numbers.intStream();
        assertThat(is.isParallel()).isFalse();
        int sum = is.sum();
        assertThat(sum).isEqualTo(20);
    }

    @Test
    public void cons_withExplicitType() {
        IntConsList<Integer> i = intCons(10, nil());
        ConsList<Number> n = cons(3.14d, i, Number.class);
        assertThat(n)
            .containsExactly(3.14d, 10)
            .hasSize(2)
            .isNotEmpty();
    }

    @Test
    public void list_withoutParameters_isNil() {
        assertThat(intList()).isEqualTo(nil());
    }

    @Test
    public void integerElements_toString() {
        Collection<Integer> strings = intList(1, 1, 2, 3, 5, 8, 13);
        assertThat(strings.toString()).isEqualTo("[1, 1, 2, 3, 5, 8, 13]");
    }

    @Test
    public void reverse() {
        IntConsList<Integer> numbers = intList(1, 1, 2, 3, 5, 8, 13);
        assertThat(numbers.intReverse()).containsExactly(13, 8, 5, 3, 2, 1, 1);
        assertThat(numbers.reverse()).containsExactly(13, 8, 5, 3, 2, 1, 1);
    }

    @Test
    public void intCons_withNullTail_throwsException() {
        IntConsList<Integer> cnslst = null;
        Throwable t = catchThrowable(() -> intCons(1337, cnslst));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("tail is null");
    }

    @Test
    public void intCons_withNullTailCollection_throwsException() {
        Collection<Integer> coll = null;
        Throwable t = catchThrowable(() -> intCons(1337, coll));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("iterable is null");
    }

    @Test
    public void exhaustedIterator_next_throwsNoSuchElement() {
        IntConsList<Integer> list = intList(1, 7);
        Iterator<Integer> iter = list.iterator();
        assertThat(iter.next()).isEqualTo(1);
        assertThat(iter.next()).isEqualTo(7);
        Throwable t = catchThrowable(iter::next);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
    }

    @Test
    public void hugeList_hasSizeIntMaxValue() throws NoSuchFieldException, IllegalAccessException {
        IntConsList<Integer> bs = intCons(3, intCons(2, intCons(1, nil())));

        int priorValue = ConsUtil.EMPTY_SIZE;
        ConsUtil.EMPTY_SIZE = Integer.MAX_VALUE - 1;
        try {
            assertThat(bs)
                .isNotEmpty()
                .hasSize(Integer.MAX_VALUE);
        } finally {
            ConsUtil.EMPTY_SIZE = priorValue;
        }
    }

    @Test
    public void hashCode_value() {
        IntConsList<Integer> fortyTwo = intCons(11, intCons(11, nil()));
        assertThat(fortyTwo.hashCode()).isEqualTo(1313);
    }

    @Test
    public void intConsList_fromNull_throwsNpe() {
        Throwable t = catchThrowable(() -> intConsList(null));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("iterable is null");
    }

    @Test
    public void intConsList_fromCons_returnsSelf() {
        Iterable<Integer> input = intList(100500, 42);
        IntConsList<Integer> fromCons = intConsList(input);

        assertThat(fromCons == input).isTrue();
    }

    @Test
    public void intConsList_fromArraysArrayList_yieldsSameOrderAsList() {
        Iterable<Integer> input = Arrays.asList(100500, 42);
        IntConsList<Integer> fromCons = intConsList(input);

        assertThat(fromCons)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(100500, 42);
    }

    @Test
    public void intConsList_fromArraysArrayListReversed_yieldsSameOrderAsList() {
        Iterable<Integer> input = Arrays.asList(42, 100500);
        IntConsList<Integer> fromCons = intConsList(input);

        assertThat(fromCons)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(42, 100500);
    }

    @Test
    public void intConsList_fromSet_returnsSameElements() {
        Set<Integer> input = new HashSet<>(Arrays.asList(100500, 42));
        IntConsList<Integer> fromCons = intConsList(input);

        assertThat(fromCons)
            .hasSize(2)
            .isNotEmpty()
            .containsExactlyInAnyOrder(100500, 42);
    }

    @Test
    public void concat_one_yieldsSelf() {
        IntConsList<Integer> input = intList(3, 2);
        assertThat(concat(input) == input).isTrue();
    }

    @Test
    public void concat_many_yieldsSameOrder() {
        IntConsList<Integer> binomial = concat(intList(1), intList(1, 1), intList(1, 2, 1), intList(1, 3, 3, 1));
        assertThat(binomial)
            .containsExactly(1, 1, 1, 1, 2, 1, 1, 3, 3, 1);
    }

    @Test
    public void concat_oneNullVararg_throwsException() {
        IntConsList<Integer>[] nullList = null;
        Throwable t = catchThrowable(() -> concat(intList(1), nullList));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Argument array rest is null");
    }

    @Test
    public void concat_lastNullListArg_throwsException() {
        Throwable t = catchThrowable(() -> concat(intList(1, 2), intList(3), null));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Null concat argument at position 2");
    }

    @Test
    public void concat_nullInListArg_throwsException() {
        Throwable t = catchThrowable(() -> concat(intList(1, 2), null, intList(3)));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Null concat argument at position 1");
    }

    @Test
    public void concat_firstNullListArg_throwsException() {
        Throwable t = catchThrowable(() -> concat(null, intList(1, 2)));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Null concat argument at position 0");
    }

    @Test
    public void intMap_isEager() {
        List<String> events = new ArrayList<>();

        intList(3, 14)
            .intMap(e -> {
                events.add("Map 1 Element " + e);
                return e;
            })
            .intMap(e -> {
                events.add("Map 2 Element " + e);
                return e;
            });

        assertThat(events)
            .containsExactly(
                "Map 1 Element 3", "Map 1 Element 14", "Map 2 Element 3", "Map 2 Element 14");
    }

    @Test
    public void intStreamMap_isLazy() {
        List<String> events = new ArrayList<>();

        intList(3, 14)
            .intStream()
            .map(e -> {
                events.add("Map 1 Element " + e);
                return e;
            })
            .map(e -> {
                events.add("Map 2 Element " + e);
                return e;
            })
            .boxed()
            .collect(ConsList.toConsCollector());

        assertThat(events)
            .containsExactly(
                "Map 1 Element 3", "Map 2 Element 3", "Map 1 Element 14", "Map 2 Element 14");
    }
}
