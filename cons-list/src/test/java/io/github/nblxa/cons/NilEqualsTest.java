package io.github.nblxa.cons;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static io.github.nblxa.cons.ConsList.*;

public class NilEqualsTest {

    @Test
    public void equalsHashCode_consListImpl() {
        EqualsVerifier.forClass(ConsList.class)
            .withPrefabValues(ConsList.class, list("a", "b", "c"), nil())
            .verify();
    }

    @Test
    public void equalsHashCode_intConsListImpl() {
        EqualsVerifier.forClass(ConsList.class)
            .withPrefabValues(ConsList.class, intList(1, 2, 3), nil())
            .verify();
    }

    @Test
    public void equalsHashCode_longConsListImpl() {
        EqualsVerifier.forClass(ConsList.class)
            .withPrefabValues(ConsList.class, longList(1L, 2L, 3L), nil())
            .verify();
    }

    @Test
    public void equalsHashCode_doubleConsListImpl() {
        EqualsVerifier.forClass(ConsList.class)
            .withPrefabValues(ConsList.class, doubleList(1.1d, 2.1d, 3.1d), nil())
            .verify();
    }
}
