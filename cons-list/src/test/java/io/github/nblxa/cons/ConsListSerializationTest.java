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
}
