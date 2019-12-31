package io.github.nblxa.cons;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static io.github.nblxa.cons.ConsList.*;

public class LongConsListEqualsTest {

    @Test
    public void equalsHashCode_longConsList() {
        EqualsVerifier.forClass(LongConsListImpl.class)
            .withPrefabValues(LongConsList.class, longList(1L, 2L, 3L), longList(100L, 200L))
            .verify();
    }

    @Test
    public void equalsHashCode_longConsListWithBoxedCons() {
        EqualsVerifier.forClass(ConsList.class)
            .withPrefabValues(ConsList.class, longList(1L, 2L, 3L), list(100L, 200L))
            .verify();
    }
}
