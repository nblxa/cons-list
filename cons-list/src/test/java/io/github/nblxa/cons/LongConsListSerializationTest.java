package io.github.nblxa.cons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static io.github.nblxa.cons.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class LongConsListSerializationTest {

    @Test
    public void test_nil_java() throws IOException, ClassNotFoundException {
        LongConsList<Long> empty = nil();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(empty);

        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));

        Object object = is.readObject();
        assertThat(object).isInstanceOf(ConsList.class);
        assertThat((ConsList<?>) object)
            .hasSize(0)
            .isEmpty();
    }

    @Test
    public void test_one_java() throws IOException, ClassNotFoundException {
        LongConsList<Long> list = longCons(42L, nil());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list);

        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));

        Object object = is.readObject();
        assertThat(object).isInstanceOf(LongConsList.class);
        assertThat((LongConsList<Long>) object)
            .hasSize(1)
            .isNotEmpty()
            .containsExactly(42L);
    }

    @Test
    public void test_many_java() throws IOException, ClassNotFoundException {
        LongConsList<Long> list = longList(13L, 42L, Long.MAX_VALUE, Long.MIN_VALUE);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list);

        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));

        Object object = is.readObject();
        assertThat(object).isInstanceOf(LongConsList.class);
        assertThat((LongConsList<Long>) object)
            .hasSize(4)
            .isNotEmpty()
            .containsExactly(13L, 42L, Long.MAX_VALUE, Long.MIN_VALUE);
    }

    @Test
    public void test_huge_list_java() throws IOException, ClassNotFoundException {
        LongConsList<Long> list = nil();
        for (int i = 20_000; i > 0; i--) {
            list = longCons(i, list);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list); // this should not cause a StackOverflowError

        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));

        Object object = is.readObject();
        assertThat(object).isInstanceOf(ConsList.class);
        assertThat((ConsList<Object>) object)
            .hasSize(20_000)
            .isNotEmpty()
            .startsWith(1L, 2L, 3L, 4L);
    }

    @Test
    public void test_serializeError_java() throws IOException {
        LongConsList<Long> list = longList(10L, 20L, 30L, 40L, 50L, 60L);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new CorruptedObjectOutputStream(new ObjectOutputStream(bos), 50L);
        Throwable t = catchThrowable(() -> os.writeObject(list));
        assertThat(t)
            .isExactlyInstanceOf(ConsSerializationException.class)
            .hasMessage("Could not serialize element at 0-based position: 4")
            .hasCauseExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void test_corruptStream_java() throws IOException {
        LongConsList<Long> list = longList(10L, 20L, 30L, 40L, 50L, 60L);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list);

        // corrupt the stream
        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new CorruptedObjectInputStream(new ByteArrayInputStream(bytes), 60L);

        Throwable t = catchThrowable(is::readObject);

        assertThat(t)
            .isExactlyInstanceOf(ConsSerializationException.class)
            .hasMessage("Could not de-serialize element at 0-based position: 5")
            .hasCauseExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void test_nil_jacksonAsList() throws IOException {
        LongConsList<Long> empty = nil();

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(empty);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List) object)
            .hasSize(0)
            .isEmpty();
    }

    @Test
    public void test_one_jacksonAsList() throws IOException {
        LongConsList<Long> list = longCons(42L, nil());

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(list);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List<Object>) object)
            .hasSize(1)
            .isNotEmpty()
            .containsExactly(42);
    }

    @Test
    public void test_many_jacksonAsList() throws IOException {
        LongConsList<Long> list = longList(13L, 42L, Long.MAX_VALUE, Long.MIN_VALUE);

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(list);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List<Object>) object)
            .hasSize(4)
            .isNotEmpty()
            .containsExactly(13, 42, Long.MAX_VALUE, Long.MIN_VALUE);
    }

    @Test
    public void test_huge_jacksonAsList() throws IOException {
        LongConsList<Long> list = nil();
        for (int i = 20_000 - 1; i > 0; i--) {
            list = longCons(i, list);
        }
        list = longCons(Long.MAX_VALUE, list);

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(list);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List<Object>) object)
            .hasSize(20_000)
            .startsWith(Long.MAX_VALUE, 1, 2, 3, 4);
    }


    @Test
    public void test_bogusIntConsList_java() throws IOException {
        // LongConsListImpl serialized without the writeReplace method.
        int[] ints = new int[] {
            -84, -19, 0, 5, 115, 114, 0, 37, 105, 111, 46, 103, 105, 116, 104, 117, 98, 46, 110, 98,
            108, 120, 97, 46, 99, 111, 110, 115, 46, 76, 111, 110, 103, 67, 111, 110, 115, 76, 105,
            115, 116, 73, 109, 112, 108, 120, -41, 121, -128, -17, 71, 75, -118, 2, 0, 2, 74, 0, 4,
            104, 101, 97, 100, 76, 0, 4, 116, 97, 105, 108, 116, 0, 35, 76, 105, 111, 47, 103, 105,
            116, 104, 117, 98, 47, 110, 98, 108, 120, 97, 47, 99, 111, 110, 115, 47, 76, 111, 110,
            103, 67, 111, 110, 115, 76, 105, 115, 116, 59, 120, 112, 0, 0, 0, 0, 0, 0, 0, 42, 115,
            114, 0, 24, 105, 111, 46, 103, 105, 116, 104, 117, 98, 46, 110, 98, 108, 120, 97, 46,
            99, 111, 110, 115, 46, 78, 105, 108, -60, 89, -55, -122, 102, 61, 35, 7, 2, 0, 0, 120,
            112
        };

        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ints[i];
        }
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));

        Throwable t = catchThrowable(is::readObject);
        assertThat(t)
            .isExactlyInstanceOf(UnsupportedOperationException.class)
            .hasMessage("Use serialization proxy!")
            .hasStackTraceContaining(LongConsListImpl.class.getCanonicalName());
    }
}
