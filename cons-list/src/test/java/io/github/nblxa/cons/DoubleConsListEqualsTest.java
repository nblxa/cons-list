package io.github.nblxa.cons;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static io.github.nblxa.cons.ConsList.*;

public class DoubleConsListEqualsTest {

    @Test
    public void equalsHashCode_doubleConsList() {
        EqualsVerifier.forClass(DoubleConsListImpl.class)
            .withPrefabValues(DoubleConsList.class, doubleList(1.1d, 2.1d, 3.1d), doubleList(100.1d, 200.1d))
            .verify();
    }

    @Test
    public void equalsHashCode_doubleConsListWithBoxedCons() {
        EqualsVerifier.forClass(ConsList.class)
            .withPrefabValues(ConsList.class, doubleList(1.1d, 2.1d, 3.1d), list(100.1d, 200.1d))
            .verify();
    }
}
