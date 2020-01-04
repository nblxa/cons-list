package io.github.nblxa.cons;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class CorruptedObjectOutputStream extends ObjectOutputStream {
    private final Object valueToCorrupt;

    public CorruptedObjectOutputStream(OutputStream out, Object valueToCorrupt) throws IOException {
        super(out);
        this.valueToCorrupt = valueToCorrupt;
    }

    @Override
    public void writeDouble(double val) throws IOException {
        considerCorrupting(val);
        super.writeDouble(val);
    }

    @Override
    public void writeInt(int val) throws IOException {
        considerCorrupting(val);
        super.writeInt(val);
    }

    @Override
    public void writeLong(long val) throws IOException {
        considerCorrupting(val);
        super.writeLong(val);
    }

    private void considerCorrupting(Object value) {
        if (valueToCorrupt.equals(value)) {
            throw new NullPointerException();
        }
    }
}
