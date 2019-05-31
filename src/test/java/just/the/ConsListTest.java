package just.the;

import org.junit.Test;

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
}
