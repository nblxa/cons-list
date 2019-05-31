package just.the;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static just.the.ConsList.*;

public class ConsListEqualsTest {

    @Test
    public void equalsHashCode_contract() {
        EqualsVerifier.forClass(ConsList.class)
            .withPrefabValues(ConsList.class, nil(), list("violet", "blue"))
            .verify();
    }
}
