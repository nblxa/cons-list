package io.github.nblxa.benchmark;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.github.nblxa.cons.ConsList;

import java.util.Collection;

import static io.github.nblxa.cons.ConsList.cons;
import static io.github.nblxa.cons.ConsList.nil;

public class ConsListLineage implements ListLineage {
    @Override
    @NonNull
    public Collection<Klass> lineage(@NonNull Klass klass) {
        final Klass superKl = klass.superKlass();
        final ConsList<Klass> res;
        if (superKl == null) {
            res = cons(klass, nil());
        } else {
            res = cons(klass, lineage(superKl));
        }

        return res;
    }
}
