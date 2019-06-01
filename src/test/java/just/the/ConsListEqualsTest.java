package just.the;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static just.the.ConsList.*;

public class ConsListEqualsTest {

    @Test
    public void equalsHashCode_contract() {
        EqualsVerifier.forClass(ConsList.class)
            .withPrefabValues(ConsList.class, nil(), list("violet", "blue"))
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
            .verify();
    }
}
