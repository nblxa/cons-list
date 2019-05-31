package just.the;

import org.junit.Test;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static just.the.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ConsListTest {

    @Test
    public void nil_isEmpty() {
        ConsList<Void> empty = nil();
        assertThat(empty).isEmpty();
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
        assertThat(list).hasSize(1);
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.head()).isEqualTo(42);
        assertThat(list.tail()).isEmpty();
    }

    @Test
    public void size1_consElement_yieldsSize2() {
        ConsList<Integer> list = list(100500);
        list = cons(42, list);
        assertThat(list)
            .hasSize(2)
            .containsExactly(42, 100500);
        assertThat(list.size()).isEqualTo(2);
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
    public void cons_withTypeInference() {
        ConsList<Number> n = cons(3.14d, cons(10, nil()));
        assertThat(n).containsExactly(3.14d, 10);
    }

    @Test
    public void cons_withExplicitType() {
        ConsList<Integer> i = cons(10, nil());
        ConsList<Number> n = cons(3.14d, i, Number.class);
        assertThat(n).containsExactly(3.14d, 10);
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
}
