package io.github.nblxa.cons;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static io.github.nblxa.cons.ConsList.*;

public class ConsListEqualsTest {

    @Test
    public void equalsHashCode_consList() {
        EqualsVerifier.forClass(ConsListImpl.class)
            .withPrefabValues(ConsList.class, list("a", "b", "c"), list(3.14, -.1))
            .verify();
    }

    @Test
    public void equalsHashCode_consListWithNil() {
        EqualsVerifier.forClass(ConsListImpl.class)
            .withPrefabValues(ConsList.class, list("a", "b", "c"), nil())
            .verify();
    }
}
