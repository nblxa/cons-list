package io.github.nblxa.cons;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

import static io.github.nblxa.cons.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class DoubleConsListTest {

    @Test
    public void nil_isEmpty() {
        DoubleConsList<Double> empty = nil();
        assertThat(empty)
            .hasSize(0)
            .isEmpty();
        Throwable t = catchThrowable(empty::doubleHead);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
        t = catchThrowable(empty::head);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
        t = catchThrowable(empty::doubleTail);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
        t = catchThrowable(empty::tail);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
    }

    @Test
    public void nil_doubleConsElement_yieldsSize1() {
        DoubleConsList<Double> list = nil();
        list = doubleCons(42.1d, list);
        assertThat(list)
            .hasSize(1)
            .isNotEmpty();
        assertThat(list.doubleHead()).isEqualTo(42.1d);
        assertThat(list.head()).isEqualTo(42.1d);
        assertThat(list.doubleTail()).isEmpty();
        assertThat(list.tail()).isEmpty();
    }

    @Test
    public void size1_doubleConsElement_yieldsSize2() {
        DoubleConsList<Double> list = doubleList(100500.1d);
        list = doubleCons(42.1d, list);
        assertThat(list)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(42.1d, 100500.1d);
        assertThat(list.doubleHead()).isEqualTo(42.1d);
        assertThat(list.head()).isEqualTo(42.1d);
        assertThat(list.doubleTail()).containsExactly(100500.1d);
        assertThat(list.tail()).containsExactly(100500.1d);
    }

    @Test
    public void size1Collection_doubleConsElement_yieldsSize2() {
        Collection<Double> p = doubleList(100500.1d);
        DoubleConsList<Double> list = doubleCons(42.1d, p);
        assertThat(list)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(42.1d, 100500.1d);
        assertThat(list.doubleHead()).isEqualTo(42.1d);
        assertThat(list.head()).isEqualTo(42.1d);
        assertThat(list.doubleTail()).containsExactly(100500.1d);
        assertThat(list.tail()).containsExactly(100500.1d);
    }

    @Test
    public void doubleIterator() {
        PrimitiveIterator.OfDouble iter = doubleList(42.1d, 13.1d, 1.1d).doubleIterator();
        assertThat(iter.hasNext()).isTrue();
        assertThat(iter.nextDouble()).isEqualTo(42.1d);
        assertThat(iter.hasNext()).isTrue();
        assertThat(iter.nextDouble()).isEqualTo(13.1d);
        assertThat(iter.hasNext()).isTrue();
        assertThat(iter.nextDouble()).isEqualTo(1.1d);
        assertThat(iter.hasNext()).isFalse();
        Throwable t = catchThrowable(iter::nextDouble);
        assertThat(t).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void spliterator_characteristicsAndData() {
        DoubleConsList<Double> numbers = doubleList(1.1d, 1.1d, 2.1d, 3.1d, 5.1d, 8.1d);
        Spliterator<Double> spliter = numbers.spliterator();

        assertThat(spliter.hasCharacteristics(Spliterator.DISTINCT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SORTED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.ORDERED)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.SIZED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.NONNULL)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.IMMUTABLE)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.CONCURRENT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SUBSIZED)).isFalse();

        List<Double> list = StreamSupport.stream(spliter, false)
            .collect(Collectors.toList());
        assertThat(list)
            .containsExactly(1.1d, 1.1d, 2.1d, 3.1d, 5.1d, 8.1d);
    }

    @Test
    public void doubleSpliterator_characteristicsAndData() {
        DoubleConsList<Double> numbers = doubleList(1.1d, 1.1d, 2.1d, 3.1d, 5.1d, 8.1d);
        Spliterator.OfDouble spliter = numbers.doubleSpliterator();

        assertThat(spliter.hasCharacteristics(Spliterator.DISTINCT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SORTED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.ORDERED)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.SIZED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.NONNULL)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.IMMUTABLE)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.CONCURRENT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SUBSIZED)).isFalse();

        List<Double> list = StreamSupport.stream(spliter, false)
            .collect(Collectors.toList());
        assertThat(list)
            .containsExactly(1.1d, 1.1d, 2.1d, 3.1d, 5.1d, 8.1d);
    }

    @Test
    public void doubleStream() {
        DoubleConsList<Double> numbers = doubleList(1.1d, 1.1d, 2.1d, 3.1d, 5.1d, 8.1d);
        DoubleStream is = numbers.doubleStream();
        assertThat(is.isParallel()).isFalse();
        double sum = is.sum();
        assertThat(sum).isEqualTo(20.6d);
    }

    @Test
    public void cons_withExplicitType() {
        DoubleConsList<Double> i = doubleCons(10.1d, nil());
        ConsList<Number> n = cons(3.14d, i, Number.class);
        assertThat(n)
            .containsExactly(3.14d, 10.1d)
            .hasSize(2)
            .isNotEmpty();
    }

    @Test
    public void list_withoutParameters_isNil() {
        assertThat(doubleList()).isEqualTo(nil());
    }

    @Test
    public void doubleElements_toString() {
        Collection<Double> strings = doubleList(1.1d, 1.1d, 2.1d, 3.1d, 5.1d, 8.1d, 13.1d);
        assertThat(strings.toString()).isEqualTo("[1.1, 1.1, 2.1, 3.1, 5.1, 8.1, 13.1]");
    }

    @Test
    public void reverse() {
        DoubleConsList<Double> numbers = doubleList(1.1d, 1.1d, 2.1d, 3.1d, 5.1d, 8.1d, 13.1d);
        assertThat(numbers.doubleReverse()).containsExactly(13.1d, 8.1d, 5.1d, 3.1d, 2.1d, 1.1d, 1.1d);
        assertThat(numbers.reverse()).containsExactly(13.1d, 8.1d, 5.1d, 3.1d, 2.1d, 1.1d, 1.1d);
    }

    @Test
    public void doubleCons_withNullTail_throwsException() {
        DoubleConsList<Double> cnslst = null;
        Throwable t = catchThrowable(() -> doubleCons(1337.1d, cnslst));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("tail is null");
    }

    @Test
    public void doubleCons_withNullTailCollection_throwsException() {
        Collection<Double> coll = null;
        Throwable t = catchThrowable(() -> doubleCons(1337.1d, coll));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("iterable is null");
    }

    @Test
    public void exhaustedIterator_next_throwsNoSuchElement() {
        DoubleConsList<Double> list = doubleList(1.1d, 7.1d);
        Iterator<Double> iter = list.iterator();
        assertThat(iter.next()).isEqualTo(1.1d);
        assertThat(iter.next()).isEqualTo(7.1d);
        Throwable t = catchThrowable(iter::next);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
    }

    @Test
    public void hugeList_hasSizeIntMaxValue() throws NoSuchFieldException, IllegalAccessException {
        DoubleConsList<Double> bs = doubleCons(3.1d, doubleCons(2.1d, doubleCons(1.1d, nil())));

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
        DoubleConsList<Double> fortyTwo = doubleCons(11.1d, doubleCons(11.1d, nil()));
        assertThat(fortyTwo.hashCode()).isEqualTo(1654653889);
    }

    @Test
    public void doubleConsList_fromNull_throwsNpe() {
        Throwable t = catchThrowable(() -> doubleConsList(null));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("iterable is null");
    }

    @Test
    public void doubleConsList_fromCons_returnsSelf() {
        DoubleConsList<Double> input = doubleList(100500.1d, 42.1d);
        DoubleConsList<Double> fromCons = doubleConsList(input);

        assertThat(fromCons == input).isTrue();
    }

    @Test
    public void doubleConsList_fromArraysArrayList_yieldsSameOrderAsList() {
        Iterable<Double> input = Arrays.asList(100500.1d, 42.1d);
        DoubleConsList<Double> fromCons = doubleConsList(input);

        assertThat(fromCons)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(100500.1d, 42.1d);
    }

    @Test
    public void doubleConsList_fromArraysArrayListReversed_yieldsSameOrderAsList() {
        Iterable<Double> input = Arrays.asList(42.1d, 100500.1d);
        DoubleConsList<Double> fromCons = doubleConsList(input);

        assertThat(fromCons)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(42.1d, 100500.1d);
    }

    @Test
    public void doubleConsList_fromSet_returnsSameElements() {
        Set<Double> input = new HashSet<>(Arrays.asList(100500.1d, 42.1d));
        DoubleConsList<Double> fromCons = doubleConsList(input);

        assertThat(fromCons)
            .hasSize(2)
            .isNotEmpty()
            .containsExactlyInAnyOrder(100500.1d, 42.1d);
    }

    @Test
    public void concat_one_yieldsSelf() {
        DoubleConsList<Double> input = doubleList(3.1d, 2.1d);
        assertThat(concat(input) == input).isTrue();
    }

    @Test
    public void concat_many_yieldsSameOrder() {
        DoubleConsList<Double> binomial = concat(doubleList(1.1d), doubleList(1.1d, 1.1d), doubleList(1.1d, 2.1d, 1.1d), doubleList(1.1d, 3.1d, 3.1d, 1.1d));
        assertThat(binomial)
            .containsExactly(1.1d, 1.1d, 1.1d, 1.1d, 2.1d, 1.1d, 1.1d, 3.1d, 3.1d, 1.1d);
    }

    @Test
    public void concat_oneNullVararg_throwsException() {
        DoubleConsList<Double>[] nullList = null;
        Throwable t = catchThrowable(() -> concat(doubleList(1.1d), nullList));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Argument array rest is null");
    }

    @Test
    public void concat_lastNullListArg_throwsException() {
        Throwable t = catchThrowable(() -> concat(doubleList(1.1d, 2.1d), doubleList(3.1d), null));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Null concat argument at position 2");
    }

    @Test
    public void concat_nullInListArg_throwsException() {
        Throwable t = catchThrowable(() -> concat(doubleList(1.1d, 2.1d), null, doubleList(3.1d)));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Null concat argument at position 1");
    }

    @Test
    public void concat_firstNullListArg_throwsException() {
        Throwable t = catchThrowable(() -> concat(null, doubleList(1.1d, 2.1d)));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Null concat argument at position 0");
    }
}
