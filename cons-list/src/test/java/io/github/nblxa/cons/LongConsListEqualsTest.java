package io.github.nblxa.cons;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static io.github.nblxa.cons.ConsList.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LongConsListEqualsTest {

    @Test
    public void equalsHashCode_longConsList() {
        EqualsVerifier.forClass(LongConsListImpl.class)
            .withPrefabValues(LongConsList.class, longList(1L, 2L, 3L), longList(100L, 200L))
            .verify();
    }

    @Test
    public void equalsHashCode_longConsListWithNil() {
        EqualsVerifier.forClass(LongConsListImpl.class)
            .withPrefabValues(LongConsList.class, longList(1L, 2L, 3L), nil())
            .verify();
    }

    @Test
    public void equalsHashCode_longConsListWithBoxedCons() {
        EqualsVerifier.forClass(ConsList.class)
            .withPrefabValues(ConsList.class, longList(1L, 2L, 3L), list(100L, 200L))
            .verify();
    }

    @Test
    public void equalsHashCode_longConsListWithEqualBoxedCons() {
        LongConsList<Long> ll = longList(1L, 2L, 3L);
        ConsList<Long> l = list(1L, 2L, 3L);
        assertThat(ll.equals(l))
            .isTrue();
        assertThat(l.equals(ll))
            .isTrue();
        assertThat(ll.hashCode() == l.hashCode())
            .isTrue();
    }
}
