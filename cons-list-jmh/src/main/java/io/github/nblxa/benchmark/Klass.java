package io.github.nblxa.benchmark;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Objects;

public final class Klass {
    @NonNull
    private final String name;
    @Nullable
    private final Klass superKlass;

    public Klass(@NonNull String name) {
        this.name = Objects.requireNonNull(name);
        this.superKlass = null;
    }

    public Klass(@NonNull String name, @NonNull Klass superKlass) {
        this.name = Objects.requireNonNull(name);
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
