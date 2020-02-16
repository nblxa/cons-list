package io.github.nblxa.cons;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.nblxa.cons.ConsList.nil;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class NilTest {

    @Test
    public void testIsEmpty() {
        assertThat(nil().isEmpty()).isTrue();
    }

    @Test
    public void testHashCode() {
        assertThat(nil().hashCode()).isEqualTo(0);
    }

    @Test
    public void testSingleton() throws NoSuchMethodException {
        @SuppressWarnings("rawtypes")
        Constructor<Nil> constructor = Nil.class.getDeclaredConstructor();
        boolean isAccessible = constructor.isAccessible();
        try {
            constructor.setAccessible(true);
            Throwable t = catchThrowable(constructor::newInstance);
            assertThat(t).isInstanceOf(InvocationTargetException.class)
                .hasCauseExactlyInstanceOf(UnsupportedOperationException.class)
                .hasRootCauseMessage(null);
        } finally {
            if (!isAccessible) {
                constructor.setAccessible(false);
            }
        }
    }

    @Test
    public void testNilIterator() {
        Iterator<?> iter = nil().iterator();
        assertThat(iter.hasNext()).isFalse();
        Throwable t = catchThrowable(iter::next);
        assertThat(t).isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause()
            .hasMessage(null);
    }

    @Test
    public void testNilIntIterator() {
        PrimitiveIterator.OfInt iter = nil().intIterator();
        assertThat(iter.hasNext()).isFalse();
        Throwable t = catchThrowable(iter::nextInt);
        assertThat(t).isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause()
            .hasMessage(null);
        t = catchThrowable(iter::next);
        assertThat(t).isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause()
            .hasMessage(null);
    }

    @Test
    public void testNilLongIterator() {
        PrimitiveIterator.OfLong iter = nil().longIterator();
        assertThat(iter.hasNext()).isFalse();
        Throwable t = catchThrowable(iter::nextLong);
        assertThat(t).isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause()
            .hasMessage(null);
        t = catchThrowable(iter::next);
        assertThat(t).isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause()
            .hasMessage(null);
    }

    @Test
    public void testNilDoubleIterator() {
        PrimitiveIterator.OfDouble iter = nil().doubleIterator();
        assertThat(iter.hasNext()).isFalse();
        Throwable t = catchThrowable(iter::nextDouble);
        assertThat(t).isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause()
            .hasMessage(null);
        t = catchThrowable(iter::next);
        assertThat(t).isExactlyInstanceOf(NoSuchElementException.class)
            .hasNoCause()
            .hasMessage(null);
    }

    @Test
    public void testNilSpliterator() {
        Spliterator<?> spliter = nil().spliterator();
        assertThat(spliter.getExactSizeIfKnown())
            .isEqualTo(0L);
    }

    @Test
    public void testNilIntSpliterator() {
        Spliterator.OfInt spliter = nil().intSpliterator();
        assertThat(spliter.getExactSizeIfKnown()).isEqualTo(0L);
    }

    @Test
    public void testNilLongSpliterator() {
        Spliterator.OfLong spliter = nil().longSpliterator();
        assertThat(spliter.getExactSizeIfKnown())
            .isEqualTo(0L);
    }

    @Test
    public void testNilDoubleSpliterator() {
        Spliterator.OfDouble spliter = nil().doubleSpliterator();
        assertThat(spliter.getExactSizeIfKnown())
            .isEqualTo(0L);
    }

    @Test
    public void testNilStream() {
        List<?> list = nil()
            .stream()
            .collect(Collectors.toList());
        assertThat(list)
            .isEmpty();
    }

    @Test
    public void testNilIntStream() {
        List<?> list = nil()
            .intStream()
            .boxed()
            .collect(Collectors.toList());
        assertThat(list)
            .isEmpty();
    }

    @Test
    public void testNilLongStream() {
        List<?> list = nil()
            .longStream()
            .boxed()
            .collect(Collectors.toList());
        assertThat(list)
            .isEmpty();
    }

    @Test
    public void testNilDoubleStream() {
        List<?> list = nil()
            .doubleStream()
            .boxed()
            .collect(Collectors.toList());
        assertThat(list)
            .isEmpty();
    }

    @Test
    public void testReverse() {
        ConsList<?> nil = nil();
        assertThat(nil.reverse())
            .isSameAs(nil);
    }

    @Test
    public void testIntReverse() {
        IntConsList<?> nil = nil();
        assertThat(nil.intReverse())
            .isSameAs(nil);
    }

    @Test
    public void testLongReverse() {
        LongConsList<?> nil = nil();
        assertThat(nil.longReverse())
            .isSameAs(nil);
    }

    @Test
    public void testDoubleReverse() {
        DoubleConsList<?> nil = nil();
        assertThat(nil.doubleReverse())
            .isSameAs(nil);
    }

    @Test
    public void testMap() {
        ConsList<Integer> nil = nil();
        assertThat(nil.map(Object::toString))
            .isSameAs(nil);
    }

    @Test
    public void testIntMap() {
        IntConsList<Integer> nil = nil();
        assertThat(nil.intMap(i -> i + 1))
            .isSameAs(nil);
    }

    @Test
    public void testLongMap() {
        LongConsList<Long> nil = nil();
        assertThat(nil.longMap(i -> i + 1L))
            .isSameAs(nil);
    }

    @Test
    public void testDoubleMap() {
        DoubleConsList<Double> nil = nil();
        assertThat(nil.doubleMap(i -> i + 1d))
            .isSameAs(nil);
    }
}
