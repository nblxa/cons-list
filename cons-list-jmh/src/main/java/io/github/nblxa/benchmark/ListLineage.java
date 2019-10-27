package io.github.nblxa.benchmark;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.Collection;

/**
 * The common interface for different implementations of the same problem using different lists.
 */
public interface ListLineage {
    /**
     * Given a tree-like hierarchy of {@link Klass} objects, where every object contains either
     * a link to the higher-level object or <tt>null</tt> if it is at the top of the hierarchy,
     * construct for the given <tt>Klass</tt> its lineage in a form of a list starting with
     * the class itself and continuing with objects of ever increasing levels.
     *
     * @param klass the given <tt>Klass</tt>
     * @return the list of <tt>Klass</tt> from the given one and up to the top of the hierarchy
     */
    @NonNull
    Collection<Klass> lineage(@NonNull Klass klass);
}
