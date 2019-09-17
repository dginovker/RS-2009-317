package org.gielinor.rs2.pulse;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by Stan van der Bend on 14/11/2017.
 *
 * project: runeworld-game-server
 * package: runeworld.engine.task
 */
class PulseSequence<T> extends ArrayDeque<Consumer<T>> {

    private Map<Consumer<T>, Predicate<T>> mappedPredicates = new HashMap<>();

    private PulseSequence<T> addNext(Consumer<T> tConsumer) {
        super.addLast(tConsumer);
        return this;
    }

    PulseSequence<T> addNext(Predicate<T> tPredicate, Consumer<T> tConsumer) {
        mappedPredicates.put(tConsumer, tPredicate);
        return addNext(tConsumer);
    }

    Optional<Predicate<T>> getOptionalPredicate(Consumer<T> action){
        return Optional.ofNullable(mappedPredicates.get(action));
    }

}
