package io.github.nblxa.cons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.*;
import java.util.AbstractMap;
import java.util.List;
import java.util.stream.IntStream;

import static io.github.nblxa.cons.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ConsListSerializationTest {

    @Test
    public void test_nil_java() throws IOException, ClassNotFoundException {
        ConsList<?> empty = nil();

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
        ConsList<Integer> list = cons(42, nil());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list);

        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));

        Object object = is.readObject();
        assertThat(object).isInstanceOf(ConsList.class);
        assertThat((ConsList<Object>) object)
            .hasSize(1)
            .isNotEmpty()
            .containsExactly(42);
    }

    @Test
    public void test_many_java() throws IOException, ClassNotFoundException {
        ConsList<Object> list = list("The answer is", 42);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list);

        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));

        Object object = is.readObject();
        assertThat(object).isInstanceOf(ConsList.class);
        assertThat((ConsList<Object>) object)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly("The answer is", 42);
    }

    @Test
    public void test_huge_list_java() throws IOException, ClassNotFoundException {
        ConsList<Object> list = nil();
        for (int i = 20_000; i > 0; i--) {
            list = cons(i, list);
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
            .startsWith(1, 2, 3, 4);
    }

    public static class NonSerializableClass {
    }

    @Test
    public void test_serializeNonserializable_java() throws IOException {
        ConsList<Object> list = list(1, 2, new NonSerializableClass(), 3, 4, 5, 6, 7, 8, 9, 10);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);

        Throwable t = catchThrowable(() -> os.writeObject(list));
        assertThat(t)
            .isExactlyInstanceOf(ConsSerializationException.class)
            .hasMessage("Could not serialize element at 0-based position: 2")
            .hasCauseExactlyInstanceOf(NotSerializableException.class);
    }

    @Test
    public void test_corruptStream_java() throws IOException {
        ConsList<Object> list = list("abc", "def", "ghi", "jkl", "mno", "pqr");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list);

        byte[] bytes = bos.toByteArray();

        // corrupt the bytes in the stream
        int indexOfJkl = IntStream.range(0, bytes.length - 2)
            .mapToObj(i -> new AbstractMap.SimpleImmutableEntry<>(i, new String(new byte[]{bytes[i], bytes[i + 1], bytes[i + 2]})))
            .filter(e -> e.getValue().equals("jkl"))
            .findFirst()
            .orElseThrow(IllegalStateException::new)
            .getKey();
        for (int i = indexOfJkl + 1; i < bytes.length; i++) {
            bytes[i] = 0;
        }

        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Throwable t = catchThrowable(is::readObject);

        assertThat(t)
            .isExactlyInstanceOf(ConsSerializationException.class)
            .hasMessage("Could not de-serialize element at 0-based position: 2")
            .hasCauseExactlyInstanceOf(StreamCorruptedException.class);
    }

    @Test
    public void test_nil_jacksonAsList() throws IOException {
        ConsList<?> empty = nil();

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
        ConsList<Integer> list = cons(42, nil());

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
        ConsList<Object> list = list("The answer is", 42);

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(list);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List<Object>) object)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly("The answer is", 42);
    }

    @Test
    public void test_huge_jacksonAsList() throws IOException {
        ConsList<Object> list = nil();
        for (int i = 20_000; i > 0; i--) {
            list = cons(i, list);
        }

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(list);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List<Object>) object)
            .hasSize(20_000)
            .startsWith(1, 2, 3, 4);
    }

    @Test
    public void test_bogusConsList_java() throws IOException {
        // ConsListImpl serialized without the writeReplace method.
        int[] ints = new int[] {
            -84, -19, 0, 5, 115, 114, 0, 33, 105, 111, 46, 103, 105, 116, 104, 117, 98, 46, 110, 98,
            108, 120, 97, 46, 99, 111, 110, 115, 46, 67, 111, 110, 115, 76, 105, 115, 116, 73, 109,
            112, 108, -77, -38, -35, -35, 94, 22, 28, -26, 2, 0, 2, 76, 0, 4, 104, 101, 97, 100,
            116, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116,
            59, 76, 0, 4, 116, 97, 105, 108, 116, 0, 31, 76, 105, 111, 47, 103, 105, 116, 104, 117,
            98, 47, 110, 98, 108, 120, 97, 47, 99, 111, 110, 115, 47, 67, 111, 110, 115, 76, 105,
            115, 116, 59, 120, 112, 115, 114, 0, 17, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46,
            73, 110, 116, 101, 103, 101, 114, 18, -30, -96, -92, -9, -127, -121, 56, 2, 0, 1, 73, 0,
            5, 118, 97, 108, 117, 101, 120, 114, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46,
            78, 117, 109, 98, 101, 114, -122, -84, -107, 29, 11, -108, -32, -117, 2, 0, 0, 120, 112,
            0, 0, 0, 42, 115, 114, 0, 24, 105, 111, 46, 103, 105, 116, 104, 117, 98, 46, 110, 98,
            108, 120, 97, 46, 99, 111, 110, 115, 46, 78, 105, 108, -60, 89, -55, -122, 102, 61, 35,
            7, 2, 0, 0, 120, 112
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
            .hasStackTraceContaining(ConsListImpl.class.getCanonicalName());
    }
}
