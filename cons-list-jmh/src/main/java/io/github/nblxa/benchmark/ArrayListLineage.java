package io.github.nblxa.benchmark;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ArrayListLineage implements ListLineage {
    @Override
    @NonNull
    public Collection<Klass> lineage(@NonNull Klass klass) {
        final Klass superKl = klass.superKlass();
        final List<Klass> res;
        if (superKl == null) {
            res = Collections.singletonList(klass);
        } else {
            Collection<Klass> lin = lineage(superKl);
            res = new ArrayList<>(lin.size() + 1);
            res.add(klass);
            res.addAll(lin);
        }
        return res;
    }
}
