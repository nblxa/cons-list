package io.github.nblxa.cons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static io.github.nblxa.cons.ConsList.*;
import static io.github.nblxa.cons.ConsList.intList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class IntConsListSerializationTest {

    @Test
    public void test_nil_java() throws IOException, ClassNotFoundException {
        IntConsList<Integer> empty = nil();

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
        IntConsList<Integer> list = intCons(42, nil());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list);

        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));

        Object object = is.readObject();
        assertThat(object).isInstanceOf(IntConsList.class);
        assertThat((IntConsList<Integer>) object)
            .hasSize(1)
            .isNotEmpty()
            .containsExactly(42);
    }

    @Test
    public void test_many_java() throws IOException, ClassNotFoundException {
        IntConsList<Integer> list = intList(13, 42);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list);

        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));

        Object object = is.readObject();
        assertThat(object).isInstanceOf(IntConsList.class);
        assertThat((IntConsList<Integer>) object)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(13, 42);
    }

    @Test
    public void test_huge_list_java() throws IOException, ClassNotFoundException {
        IntConsList<Integer> list = nil();
        for (int i = 20_000; i > 0; i--) {
            list = intCons(i, list);
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

    @Test
    public void test_serializeError_java() throws IOException {
        IntConsList<Integer> list = intList(1, 2, 3, 4, 5, 6);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new CorruptedObjectOutputStream(new ObjectOutputStream(bos), 1);
        Throwable t = catchThrowable(() -> os.writeObject(list));
        assertThat(t)
            .isExactlyInstanceOf(ConsSerializationException.class)
            .hasMessage("Could not serialize element at 0-based position: 0")
            .hasCauseExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void test_corruptStream_java() throws IOException {
        IntConsList<Integer> list = intList(1, 2, 3, 4, 5, 6);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list);

        // corrupt the stream
        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new CorruptedObjectInputStream(new ByteArrayInputStream(bytes), 2);

        Throwable t = catchThrowable(is::readObject);

        assertThat(t)
            .isExactlyInstanceOf(ConsSerializationException.class)
            .hasMessage("Could not de-serialize element at 0-based position: 1")
            .hasCauseExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void test_nil_jacksonAsList() throws IOException {
        IntConsList<Integer> empty = nil();

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
        IntConsList<Integer> list = intCons(42, nil());

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
        IntConsList<Integer> list = intList(13, 42);

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(list);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List<Integer>) object)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly(13, 42);
    }

    @Test
    public void test_huge_jacksonAsList() throws IOException {
        IntConsList<Integer> list = nil();
        for (int i = 20_000; i > 0; i--) {
            list = intCons(i, list);
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
