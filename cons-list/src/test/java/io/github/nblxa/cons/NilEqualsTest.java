package io.github.nblxa.cons;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static io.github.nblxa.cons.ConsList.*;

public class NilEqualsTest {

    @Test
    public void equalsHashCode_consListImpl() {
        EqualsVerifier.forClass(Nil.class)
            .withPrefabValues(ConsList.class, nil(), list("a", "b", "c"))
            .verify();
    }

    @Test
    public void equalsHashCode_intConsListImpl() {
        EqualsVerifier.forClass(Nil.class)
            .withPrefabValues(ConsList.class, nil(), intList(1, 2, 3))
            .verify();
    }

    @Test
    public void equalsHashCode_longConsListImpl() {
        EqualsVerifier.forClass(Nil.class)
            .withPrefabValues(ConsList.class, nil(), longList(1L, 2L, 3L))
            .verify();
    }

    @Test
    public void equalsHashCode_doubleConsListImpl() {
        EqualsVerifier.forClass(Nil.class)
            .withPrefabValues(ConsList.class, nil(), doubleList(1.1d, 2.1d, 3.1d))
            .verify();
    }
}
