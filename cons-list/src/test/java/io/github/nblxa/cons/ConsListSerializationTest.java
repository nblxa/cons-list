package io.github.nblxa.cons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static io.github.nblxa.cons.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        ConsList<Integer> empty = cons(42, nil());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(empty);

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
        ConsList<Object> empty = list("The answer is", 42);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(empty);

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
        ConsList<Integer> empty = cons(42, nil());

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(empty);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List<Object>) object)
            .hasSize(1)
            .isNotEmpty()
            .containsExactly(42);
    }

    @Test
    public void test_many_jacksonAsList() throws IOException {
        ConsList<Object> empty = list("The answer is", 42);

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(empty);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List<Object>) object)
            .hasSize(2)
            .isNotEmpty()
            .containsExactly("The answer is", 42);
    }
}
