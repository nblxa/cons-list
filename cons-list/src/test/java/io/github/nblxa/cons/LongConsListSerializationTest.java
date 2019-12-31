package io.github.nblxa.cons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static io.github.nblxa.cons.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        LongConsList<Long> empty = longCons(42L, nil());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(empty);

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
        LongConsList<Long> empty = longList(13L, 42L, Long.MAX_VALUE, Long.MIN_VALUE);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(empty);

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
        LongConsList<Long> empty = longCons(42L, nil());

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
        LongConsList<Long> empty = longList(13L, 42L, Long.MAX_VALUE, Long.MIN_VALUE);

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(empty);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List<Object>) object)
            .hasSize(4)
            .isNotEmpty()
            .containsExactly(13, 42, Long.MAX_VALUE, Long.MIN_VALUE);
    }
}
