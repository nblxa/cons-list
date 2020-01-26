package io.github.nblxa.cons;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class CorruptedObjectInputStream extends ObjectInputStream {
    private final Object valueToCorrupt;

    public CorruptedObjectInputStream(InputStream in, Object valueToCorrupt) throws IOException {
        super(in);
        this.valueToCorrupt = valueToCorrupt;
    }

    @Override
    public double readDouble() throws IOException {
        double value = super.readDouble();
        considerCorrupting(value);
        return value;
    }

    @Override
    public int readInt() throws IOException {
        int value = super.readInt();
        considerCorrupting(value);
        return value;
    }

    @Override
    public long readLong() throws IOException {
        long value = super.readLong();
        considerCorrupting(value);
        return value;
    }

    private void considerCorrupting(Object value) {
        if (valueToCorrupt.equals(value)) {
            throw new NullPointerException();
        }
    }
}
