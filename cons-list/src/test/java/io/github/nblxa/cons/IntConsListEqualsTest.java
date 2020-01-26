package io.github.nblxa.cons;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static io.github.nblxa.cons.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;

public class IntConsListEqualsTest {

    @Test
    public void equalsHashCode_intConsList() {
        EqualsVerifier.forClass(IntConsListImpl.class)
            .withPrefabValues(IntConsList.class, intList(1, 2, 3), intList(100, 200))
            .verify();
    }

    @Test
    public void equalsHashCode_intConsListWithNil() {
        EqualsVerifier.forClass(IntConsListImpl.class)
            .withPrefabValues(IntConsList.class, intList(1, 2, 3), nil())
            .verify();
    }

    @Test
    public void equalsHashCode_intConsListWithBoxedCons() {
        EqualsVerifier.forClass(ConsList.class)
            .withPrefabValues(ConsList.class, intList(1, 2, 3), list(100, 200))
            .verify();
    }

    @Test
    public void equalsHashCode_intConsListWithEqualBoxedCons() {
        IntConsList<Integer> il = intList(1, 2, 3);
        ConsList<Integer> l = list(1, 2, 3);
        assertThat(il.equals(l))
            .isTrue();
        assertThat(l.equals(il))
            .isTrue();
        assertThat(il.hashCode() == l.hashCode())
            .isTrue();
    }
}
