package io.github.nblxa.benchmark;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LinkedListLineage implements ListLineage {
    @Override
    @NonNull
    public Collection<Klass> lineage(@NonNull Klass klass) {
        final Klass superKl = klass.superKlass();
        final List<Klass> res;
        if (superKl == null) {
            res = Collections.singletonList(klass);
        } else {
            Collection<Klass> lin = lineage(superKl);
            res = new LinkedList<>(lin);
            res.add(0, klass);
        }
        return res;
    }
}
