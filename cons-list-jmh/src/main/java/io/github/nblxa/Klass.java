package io.github.nblxa;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Objects;

public class Klass {
    @NonNull
    private String name;
    @Nullable
    private Klass superKlass;

    public Klass(@NonNull String name) {
        this.name = Objects.requireNonNull(name);
    }

    public Klass(@NonNull String name, @NonNull Klass superKlass) {
        this(name);
        this.superKlass = Objects.requireNonNull(superKlass);
    }

    @NonNull
    public String name() {
        return name;
    }

    @Nullable
    public Klass superKlass() {
        return superKlass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Klass klass = (Klass) o;
        return name.equals(klass.name) &&
            Objects.equals(superKlass, klass.superKlass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, superKlass);
    }

    @Override
    @NonNull
    public String toString() {
        return name;
    }
}
