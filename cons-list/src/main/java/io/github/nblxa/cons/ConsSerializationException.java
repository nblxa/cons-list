package io.github.nblxa.cons;

import java.io.IOException;

public class ConsSerializationException extends IOException {
    ConsSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
