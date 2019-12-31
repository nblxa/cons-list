package io.github.nblxa.cons;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static io.github.nblxa.cons.ConsList.*;

public class IntConsListEqualsTest {

    @Test
    public void equalsHashCode_intConsList() {
        EqualsVerifier.forClass(IntConsListImpl.class)
            .withPrefabValues(IntConsList.class, intList(1, 2, 3), intList(100, 200))
            .verify();
    }

    @Test
    public void equalsHashCode_intConsListWithBoxedCons() {
        EqualsVerifier.forClass(ConsList.class)
            .withPrefabValues(ConsList.class, intList(1, 2, 3), list(100, 200))
            .verify();
    }
}
