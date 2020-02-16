package io.github.nblxa.cons;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static io.github.nblxa.cons.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class LongConsListTest {

    @Test
    public void nil_isEmpty() {
        LongConsList<Long> empty = nil();
        assertThat(empty)
            .hasSize(0)
            .isEmpty();
        Throwable t = catchThrowable(empty::longHead);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
        t = catchThrowable(empty::head);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
        t = catchThrowable(empty::longTail);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
        t = catchThrowable(empty::tail);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
    }

    @Test
    public void nil_longConsElement_yieldsSize1() {
        LongConsList<Long> list = nil();
        list = longCons(42L, list);
        assertThat(list)
            .hasSize(1)
            .isNotEmpty();
        assertThat(list.longHead()).isEqualTo(42L);
        assertThat(list.head()).isEqualTo(42L);
        assertThat(list.longTail()).isEmpty();
        assertThat(list.tail()).isEmpty();
    }

    @Test
    public void size1_longConsElement_yieldsSize2() {
        LongConsList<Long> list = longList(100500L);
        list = longCons(42L, list);
        assertThat(list)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(42L, 100500L);
        assertThat(list.longHead()).isEqualTo(42L);
        assertThat(list.head()).isEqualTo(42L);
        assertThat(list.longTail()).containsExactly(100500L);
        assertThat(list.tail()).containsExactly(100500L);
    }

    @Test
    public void size1Collection_longConsElement_yieldsSize2() {
        Collection<Long> p = longList(100500L);
        LongConsList<Long> list = longCons(42L, p);
        assertThat(list)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(42L, 100500L);
        assertThat(list.longHead()).isEqualTo(42L);
        assertThat(list.head()).isEqualTo(42L);
        assertThat(list.longTail()).containsExactly(100500L);
        assertThat(list.tail()).containsExactly(100500L);
    }

    @Test
    public void longIterator() {
        PrimitiveIterator.OfLong iter = longList(42L, 13L, 1L).longIterator();
        assertThat(iter.hasNext()).isTrue();
        assertThat(iter.nextLong()).isEqualTo(42L);
        assertThat(iter.hasNext()).isTrue();
        assertThat(iter.nextLong()).isEqualTo(13L);
        assertThat(iter.hasNext()).isTrue();
        assertThat(iter.nextLong()).isEqualTo(1L);
        assertThat(iter.hasNext()).isFalse();
        Throwable t = catchThrowable(iter::nextLong);
        assertThat(t).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void spliterator_characteristicsAndData() {
        LongConsList<Long> numbers = longList(1L, 1L, 2L, 3L, 5L, 8L);
        Spliterator<Long> spliter = numbers.spliterator();

        assertThat(spliter.hasCharacteristics(Spliterator.DISTINCT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SORTED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.ORDERED)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.SIZED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.NONNULL)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.IMMUTABLE)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.CONCURRENT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SUBSIZED)).isFalse();

        List<Long> list = StreamSupport.stream(spliter, false)
            .collect(Collectors.toList());
        assertThat(list)
            .containsExactly(1L, 1L, 2L, 3L, 5L, 8L);
    }

    @Test
    public void longSpliterator_characteristicsAndData() {
        LongConsList<Long> numbers = longList(1L, 1L, 2L, 3L, 5L, 8L);
        Spliterator.OfLong spliter = numbers.longSpliterator();

        assertThat(spliter.hasCharacteristics(Spliterator.DISTINCT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SORTED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.ORDERED)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.SIZED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.NONNULL)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.IMMUTABLE)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.CONCURRENT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SUBSIZED)).isFalse();

        List<Long> list = StreamSupport.stream(spliter, false)
            .collect(Collectors.toList());
        assertThat(list)
            .containsExactly(1L, 1L, 2L, 3L, 5L, 8L);
    }

    @Test
    public void longStream() {
        LongConsList<Long> numbers = longList(1L, 1L, 2L, 3L, 5L, 8L);
        LongStream is = numbers.longStream();
        assertThat(is.isParallel()).isFalse();
        long sum = is.sum();
        assertThat(sum).isEqualTo(20L);
    }

    @Test
    public void cons_withExplicitType() {
        LongConsList<Long> i = longCons(10L, nil());
        ConsList<Number> n = cons(3.14d, i, Number.class);
        assertThat(n)
            .containsExactly(3.14d, 10L)
            .hasSize(2)
            .isNotEmpty();
    }

    @Test
    public void list_withoutParameters_isNil() {
        assertThat(longList()).isEqualTo(nil());
    }

    @Test
    public void longElements_toString() {
        Collection<Long> strings = longList(1L, 1L, 2L, 3L, 5L, 8L, 13L);
        assertThat(strings.toString()).isEqualTo("[1, 1, 2, 3, 5, 8, 13]");
    }

    @Test
    public void reverse() {
        LongConsList<Long> numbers = longList(1L, 1L, 2L, 3L, 5L, 8L, 13L);
        assertThat(numbers.longReverse()).containsExactly(13L, 8L, 5L, 3L, 2L, 1L, 1L);
        assertThat(numbers.reverse()).containsExactly(13L, 8L, 5L, 3L, 2L, 1L, 1L);
    }

    @Test
    public void longCons_withNullTail_throwsException() {
        LongConsList<Long> cnslst = null;
        Throwable t = catchThrowable(() -> longCons(1337L, cnslst));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("tail is null");
    }

    @Test
    public void longCons_withNullTailCollection_throwsException() {
        Collection<Long> coll = null;
        Throwable t = catchThrowable(() -> longCons(1337L, coll));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("iterable is null");
    }

    @Test
    public void exhaustedIterator_next_throwsNoSuchElement() {
        LongConsList<Long> list = longList(1L, 7L);
        Iterator<Long> iter = list.iterator();
        assertThat(iter.next()).isEqualTo(1L);
        assertThat(iter.next()).isEqualTo(7L);
        Throwable t = catchThrowable(iter::next);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
    }

    @Test
    public void hugeList_hasSizeIntMaxValue() throws NoSuchFieldException, IllegalAccessException {
        LongConsList<Long> bs = longCons(3L, longCons(2L, longCons(1L, nil())));

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
        LongConsList<Long> fortyTwo = longCons(11L, longCons(11L, nil()));
        assertThat(fortyTwo.hashCode()).isEqualTo(1313);
    }

    @Test
    public void longConsList_fromNull_throwsNpe() {
        Throwable t = catchThrowable(() -> longConsList(null));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("iterable is null");
    }

    @Test
    public void longConsList_fromCons_returnsSelf() {
        LongConsList<Long> input = longList(100500L, 42L);
        LongConsList<Long> fromCons = longConsList(input);

        assertThat(fromCons == input).isTrue();
    }

    @Test
    public void longConsList_fromArraysArrayList_yieldsSameOrderAsList() {
        Iterable<Long> input = Arrays.asList(100500L, 42L);
        LongConsList<Long> fromCons = longConsList(input);

        assertThat(fromCons)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(100500L, 42L);
    }

    @Test
    public void longConsList_fromArraysArrayListReversed_yieldsSameOrderAsList() {
        Iterable<Long> input = Arrays.asList(42L, 100500L);
        LongConsList<Long> fromCons = longConsList(input);

        assertThat(fromCons)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(42L, 100500L);
    }

    @Test
    public void longConsList_fromSet_returnsSameElements() {
        Set<Long> input = new HashSet<>(Arrays.asList(100500L, 42L));
        LongConsList<Long> fromCons = longConsList(input);

        assertThat(fromCons)
            .hasSize(2)
            .isNotEmpty()
            .containsExactlyInAnyOrder(100500L, 42L);
    }

    @Test
    public void concat_one_yieldsSelf() {
        LongConsList<Long> input = longList(3L, 2L);
        assertThat(concat(input) == input).isTrue();
    }

    @Test
    public void concat_many_yieldsSameOrder() {
        LongConsList<Long> binomial = concat(longList(1L), longList(1L, 1L), longList(1L, 2L, 1L), longList(1L, 3L, 3L, 1L));
        assertThat(binomial)
            .containsExactly(1L, 1L, 1L, 1L, 2L, 1L, 1L, 3L, 3L, 1L);
    }

    @Test
    public void concat_oneNullVararg_throwsException() {
        LongConsList<Long>[] nullList = null;
        Throwable t = catchThrowable(() -> concat(longList(1L), nullList));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Argument array rest is null");
    }

    @Test
    public void concat_lastNullListArg_throwsException() {
        Throwable t = catchThrowable(() -> concat(longList(1L, 2L), longList(3L), null));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Null concat argument at position 2");
    }

    @Test
    public void concat_nullInListArg_throwsException() {
        Throwable t = catchThrowable(() -> concat(longList(1L, 2L), null, longList(3L)));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Null concat argument at position 1");
    }

    @Test
    public void concat_firstNullListArg_throwsException() {
        Throwable t = catchThrowable(() -> concat(null, longList(1L, 2L)));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Null concat argument at position 0");
    }

    @Test
    public void longMap_isEager() {
        List<String> events = new ArrayList<>();

        longList(3L, 14L)
            .longMap(e -> {
                events.add("Map 1 Element " + e);
                return e;
            })
            .longMap(e -> {
                events.add("Map 2 Element " + e);
                return e;
            });

        assertThat(events)
            .containsExactly(
                "Map 1 Element 3", "Map 1 Element 14", "Map 2 Element 3", "Map 2 Element 14");
    }

    @Test
    public void longStreamMap_isLazy() {
        List<String> events = new ArrayList<>();

        longList(3L, 14L)
            .longStream()
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
