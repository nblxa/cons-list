package io.github.nblxa.benchmark;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.github.nblxa.Klass;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LinkedListLineage {
    @NonNull
    public List<Klass> lineage(Klass klass) {
        final Klass superKl = klass.superKlass();
        final List<Klass> res;
        if (superKl == null) {
            res = Collections.singletonList(klass);
        } else {
            List<Klass> lin = lineage(superKl);
            res = new LinkedList<>(lin);
            res.add(0, klass);
        }
        return res;
    }
}
