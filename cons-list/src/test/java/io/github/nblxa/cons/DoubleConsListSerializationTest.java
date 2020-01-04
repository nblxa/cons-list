package io.github.nblxa.cons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static io.github.nblxa.cons.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class DoubleConsListSerializationTest {

    @Test
    public void test_nil_java() throws IOException, ClassNotFoundException {
        DoubleConsList<Double> empty = nil();

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
        DoubleConsList<Double> list = doubleCons(42.1d, nil());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list);

        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));

        Object object = is.readObject();
        assertThat(object).isInstanceOf(DoubleConsList.class);
        assertThat((DoubleConsList<Double>) object)
            .hasSize(1)
            .isNotEmpty()
            .containsExactly(42.1d);
    }

    @Test
    public void test_many_java() throws IOException, ClassNotFoundException {
        DoubleConsList<Double> list = doubleList(13.1d, 42.1d, Double.MAX_VALUE, Double.MIN_VALUE);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list);

        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));

        Object object = is.readObject();
        assertThat(object).isInstanceOf(DoubleConsList.class);
        assertThat((DoubleConsList<Double>) object)
            .hasSize(4)
            .isNotEmpty()
            .containsExactly(13.1d, 42.1d, Double.MAX_VALUE, Double.MIN_VALUE);
    }

    @Test
    public void test_huge_list_java() throws IOException, ClassNotFoundException {
        DoubleConsList<Double> list = nil();
        for (int i = 20_000; i > 0; i--) {
            list = doubleCons(i + 0.1d, list);
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
            .startsWith(1.1d, 2.1d, 3.1d, 4.1d);
    }

    @Test
    public void test_serializeError_java() throws IOException {
        DoubleConsList<Double> list = doubleList(1.1d, 2.1d, 3.1d, 4.1d, 5.1d, 6.1d);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new CorruptedObjectOutputStream(new ObjectOutputStream(bos), 2.1d);
        Throwable t = catchThrowable(() -> os.writeObject(list));
        assertThat(t)
            .isExactlyInstanceOf(ConsSerializationException.class)
            .hasMessage("Could not serialize element at 0-based position: 1")
            .hasCauseExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void test_corruptStream_java() throws IOException {
        DoubleConsList<Double> list = doubleList(1.1d, 2.1d, 3.1d, 4.1d, 5.1d, 6.1d);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(list);

        // corrupt the stream
        byte[] bytes = bos.toByteArray();
        ObjectInputStream is = new CorruptedObjectInputStream(new ByteArrayInputStream(bytes), 4.1d);

        Throwable t = catchThrowable(is::readObject);

        assertThat(t)
            .isExactlyInstanceOf(ConsSerializationException.class)
            .hasMessage("Could not de-serialize element at 0-based position: 3")
            .hasCauseExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void test_nil_jacksonAsList() throws IOException {
        DoubleConsList<Double> empty = nil();

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
        DoubleConsList<Double> list = doubleCons(42.1d, nil());

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(list);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List<Object>) object)
            .hasSize(1)
            .isNotEmpty()
            .containsExactly(42.1d);
    }

    @Test
    public void test_many_jacksonAsList() throws IOException {
        DoubleConsList<Double> list = doubleList(13.1d, 42.1d, Double.MAX_VALUE, Double.MIN_VALUE);

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(list);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List<Object>) object)
            .hasSize(4)
            .isNotEmpty()
            .containsExactly(13.1d, 42.1d, Double.MAX_VALUE, Double.MIN_VALUE);
    }

    @Test
    public void test_huge_jacksonAsList() throws IOException {
        DoubleConsList<Double> list = nil();
        for (int i = 20_000; i > 0; i--) {
            list = doubleCons(i + 0.1d, list);
        }

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(list);

        Object object = om.readValue(string, List.class);
        assertThat(object).isInstanceOf(List.class);
        assertThat((List<Object>) object)
            .hasSize(20_000)
            .startsWith(1.1d, 2.1d, 3.1d, 4.1d);
    }
}
