package io.github.nblxa;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;

import static io.github.nblxa.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ConsListTest {

    @Test
    public void nil_isEmpty() {
        ConsList<Void> empty = nil();
        assertThat(empty)
            .hasSize(0)
            .isEmpty();
        Throwable t = catchThrowable(empty::head);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
        t = catchThrowable(empty::tail);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
    }

    @Test
    public void nil_consElement_yieldsSize1() {
        ConsList<Integer> list = nil();
        list = cons(42, list);
        assertThat(list)
            .hasSize(1)
            .isNotEmpty();
        assertThat(list.head()).isEqualTo(42);
        assertThat(list.tail()).isEmpty();
    }

    @Test
    public void size1_consElement_yieldsSize2() {
        ConsList<Integer> list = list(100500);
        list = cons(42, list);
        assertThat(list)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(42, 100500);
        assertThat(list.head()).isEqualTo(42);
        assertThat(list.tail()).containsExactly(100500);
    }

    @Test
    public void stream_join() {
        ConsList<String> strings = list("Hello", "functional", "programming", "!");
        String joined = strings.stream()
            .collect(Collectors.joining(" "));
        assertThat(joined).isEqualTo("Hello functional programming !");
    }

    @Test
    public void spliterator_hasCharacteristics() {
        ConsList<String> strings = list("Hello", "functional", "programming", "!");
        Spliterator<String> spliter = strings.spliterator();

        assertThat(spliter.hasCharacteristics(Spliterator.DISTINCT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SORTED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.ORDERED)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.SIZED)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.NONNULL)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.IMMUTABLE)).isTrue();
        assertThat(spliter.hasCharacteristics(Spliterator.CONCURRENT)).isFalse();
        assertThat(spliter.hasCharacteristics(Spliterator.SUBSIZED)).isFalse();
    }

    @Test
    public void cons_withTypeInference() {
        ConsList<Number> n = cons(3.14d, cons(10, nil()));
        assertThat(n)
            .containsExactly(3.14d, 10)
            .hasSize(2)
            .isNotEmpty();
    }

    @Test
    public void cons_withExplicitType() {
        ConsList<Integer> i = cons(10, nil());
        ConsList<Number> n = cons(3.14d, i, Number.class);
        assertThat(n)
            .containsExactly(3.14d, 10)
            .hasSize(2)
            .isNotEmpty();
    }

    @Test
    public void list_withoutParameters_isNil() {
        assertThat(list()).isEqualTo(nil());
    }

    @Test
    public void stringElements_toString() {
        Collection<String> strings = list("Apples", "Bananas", "Oranges");
        assertThat(strings.toString()).isEqualTo("[Apples, Bananas, Oranges]");
    }

    @Test
    public void integerElements_toString() {
        Collection<Integer> strings = list(1, 1, 2, 3, 5, 8, 13);
        assertThat(strings.toString()).isEqualTo("[1, 1, 2, 3, 5, 8, 13]");
    }

    @Test
    public void reverse() {
        ConsList<String> strings = list("Apples", "Bananas", "Oranges");
        assertThat(strings.reverse()).containsExactly("Oranges", "Bananas", "Apples");
    }

    @Test
    public void cons_withNullTail_throwsException() {
        Throwable t = catchThrowable(() -> cons("Peaches", null));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("tail is null");
    }

    @Test
    public void exhaustedIterator_next_throwsNoSuchElement() {
        ConsList<Boolean> list = list(true, false);
        Iterator<Boolean> iter = list.iterator();
        assertThat(iter.next()).isTrue();
        assertThat(iter.next()).isFalse();
        Throwable t = catchThrowable(iter::next);
        assertThat(t)
            .isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void hugeList_hasSizeIntMaxValue() {
        ConsList<Integer> bs = cons(3, cons(2, cons(1, nil())));

        ConsList<Integer> spyCons = Mockito.spy(bs);
        Mockito.doReturn(Integer.MAX_VALUE - 1)
            .when(spyCons)
            .nilSize();

        assertThat(spyCons)
            .isNotEmpty()
            .hasSize(Integer.MAX_VALUE);
    }

    @Test
    public void hashCode_value() {
        ConsList<Integer> fortyTwo = cons(11, cons(11, nil()));
        assertThat(fortyTwo.hashCode()).isEqualTo(1313);
    }

    @Test
    public void consList_fromNull_throwsNpe() {
        Throwable t = catchThrowable(() -> consList(null));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("iterable is null");
    }

    @Test
    public void consList_fromCons_returnsSelf() {
        Iterable<Integer> input = list(100500, 42);
        ConsList<Integer> fromCons = consList(input);

        assertThat(fromCons == input).isTrue();
    }

    @Test
    public void consList_fromArraysArrayList_yieldsSameOrderAsList() {
        Iterable<Integer> input = Arrays.asList(100500, 42);
        ConsList<Integer> fromCons = consList(input);

        assertThat(fromCons)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(100500, 42);
    }

    @Test
    public void consList_fromArraysArrayListReversed_yieldsSameOrderAsList() {
        Iterable<Integer> input = Arrays.asList(42, 100500);
        ConsList<Integer> fromCons = consList(input);

        assertThat(fromCons)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(42, 100500);
    }

    @Test
    public void consList_fromSet_returnsSameElements() {
        Set<Integer> input = new HashSet<>(Arrays.asList(100500, 42));
        ConsList<Integer> fromCons = consList(input);

        assertThat(fromCons)
            .hasSize(2)
            .isNotEmpty()
            .containsExactlyInAnyOrder(100500, 42);
    }

    @Test
    public void concat_none_yieldsNil() {
        assertThat(concat())
            .isEqualTo(nil())
            .hasSize(0)
            .isEmpty();
    }

    @Test
    public void concat_one_yieldsSelf() {
        ConsList<String> input = concat(list("There", "must", "be", "only", "one!"));
        assertThat(concat(input) == input).isTrue();
    }

    @Test
    public void concat_many_yieldsSameOrder() {
        ConsList<Integer> binomial = concat(list(1), list(1, 1), list(1, 2, 1), list(1, 3, 3, 1));
        assertThat(binomial)
            .containsExactly(1, 1, 1, 1, 2, 1, 1, 3, 3, 1);
    }

    @Test
    public void concat_oneNullVararg_throwsException() {
        Throwable t = catchThrowable(() -> concat(null));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Argument array lists is null");
    }

    @Test
    public void concat_lastNullListArg_throwsException() {
        Throwable t = catchThrowable(() -> concat(list(1, 2), null));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Null argument at position 1");
    }

    @Test
    public void concat_firstNullListArg_throwsException() {
        Throwable t = catchThrowable(() -> concat(null, list(1, 2)));
        assertThat(t)
            .isExactlyInstanceOf(NullPointerException.class)
            .hasNoCause()
            .hasMessage("Null argument at position 0");
    }

    @Test
    public void collector_emptyStream_yieldsNil() {
        ConsList<String> empty = Arrays.stream(new String[0])
            .collect(toConsCollector());

        assertThat(empty)
            .isEqualTo(nil())
            .hasSize(0)
            .isEmpty();
    }

    @Test
    public void collector_nonEmptyStream_yieldsCons() {
        ConsList<String> empty = Arrays.stream(new String[] {"Apples", "Pears", "Oranges"})
            .collect(toConsCollector());

        assertThat(empty)
            .hasSize(3)
            .isNotEmpty()
            .containsExactly("Apples", "Pears", "Oranges");
    }
}
