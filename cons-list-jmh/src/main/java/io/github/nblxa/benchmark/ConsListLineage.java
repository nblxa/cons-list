package io.github.nblxa.benchmark;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.github.nblxa.ConsList;
import io.github.nblxa.Klass;

import static io.github.nblxa.ConsList.cons;
import static io.github.nblxa.ConsList.nil;

public class ConsListLineage {
    @NonNull
    public ConsList<Klass> lineage(Klass klass) {
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
