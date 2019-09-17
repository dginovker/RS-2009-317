package org.gielinor.rs2.pulse;

import org.gielinor.game.content.builder.Builder;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.world.World;
import org.gielinor.utilities.misc.RandomUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by Stan van der Bend on 14/11/2017.
 *
 * A generic {@link PulseBuilder} following the builder design pattern.
 *
 * project: runeworld-game-server
 * package: runeworld.engine.pulse
 */
public class PulseBuilder<T> extends ArrayDeque<PulseSequence> implements Builder<Pulse> {

    private static boolean debug = false;

    private final Set<PulseSequence<T>> consecutiveSequences = new HashSet<>();
    private final Map<Integer, PulseSequence<T>> consecutiveTriggerSequences = new HashMap<>();
    private final Map<Integer, PulseSequence<T>> triggerSequences = new HashMap<>();

    private final T architect;
    private final Node[] nodes;
    private final int period;
    private final boolean immediate;

    private boolean firstPulse = true;
    private int finalTrigger;
    private int currentCycle, stopCycle = -1;

    private Predicate<T>
            pausePredicate = (t) -> false,
            startPredicate = Objects::nonNull,
            stopPredicate = Objects::isNull;
    private Consumer<T>
            pauseAction = t -> {System.out.println("Pausing Pulse.");},
            startAction = t -> {},
            stopAction = t -> stopCycle = currentCycle;

    /**
     * Creates a new {@link PulseBuilder}.
     *
     * On build the created {@link Pulse} will be bind to specified {@code architect}.
     * The pulse will execute consecutively after each specified delay of cycles has passed.
     *
     * @param architect the central type and key of the future build {@link Pulse}.
     * @param period the amount of cycles between each call to {@link Pulse#pulse()}
     * @param immediate {@code true} when the first call should skip the {@code period} delay before executing.
     */
    public PulseBuilder(T architect, int period, boolean immediate, Node... nodes) {
        this.architect = architect;
        this.period = period;
        this.immediate = immediate;
        this.nodes = nodes;
    }

    /**
     * Creates a new {@link PulseBuilder}.
     *
     * On build the created {@link Pulse} will be bind to specified {@code architect}.
     * The pulse will execute consecutively before each passing cycle and will have no initial delay.
     *
     * @param architect the central type and key of the future build {@link Pulse}.
     */
    public PulseBuilder(T architect) {
        this(architect, 1, false);
    }

    public static boolean isDebugging() {
        return debug;
    }

    /**
     * Specify a {@link Predicate<T>} at which the build {@link Pulse} is supposed to pause.
     *
     * @param pausePredicate the pause condition of the pulse.
     * @param pauseTillPredicate the end pause condition of this condition.
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> pauseWhen(Predicate<T> pausePredicate, Predicate<T> pauseTillPredicate){
        this.pausePredicate = this.pausePredicate.or(pausePredicate).and(pauseTillPredicate.negate());
        return this;
    }
    public PulseBuilder<T> pause(int trigger){
        this.pausePredicate = t -> trigger == currentCycle;
        return this;
    }
    public PulseBuilder<T> pause(){
        this.pausePredicate = t -> true;
        return this;
    }
    public PulseBuilder<T> resume(Predicate<T> resumePredicate){
        this.pausePredicate = pausePredicate.and(resumePredicate);
        return this;
    }
    /**
     * Specify a {@link Consumer<T>} that executes on pausing the build {@link Pulse}.
     *
     * @param pauseAction the first action after pausing the pulse.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> onPause(Consumer<T> pauseAction){
        this.pauseAction =  this.pauseAction.andThen(pauseAction);
        return this;
    }
    /**
     * Specify a {@link Predicate<T>} at which the build {@link Pulse} is supposed to initialize.
     *
     * @param startPredicate the initialize condition of the pulse.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> startWhen(Predicate<T> startPredicate){
        this.startPredicate = this.startPredicate.and(startPredicate);
        return this;
    }
    /**
     * Specify a {@link Consumer<T>} that executes on starting the build {@link Pulse}.
     *
     * @param startAction the first action of the pulse.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> onStart(Consumer<T> startAction){
        return  add(0, startAction);
    }

    /**
     * Specify a {@link Predicate<T>} at which the build {@link Pulse} is supposed to stop.
     *
     * @param stopPredicate the stop condition of the pulse.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> stopWhen(Predicate<T> stopPredicate){
        this.stopPredicate = this.stopPredicate.or(stopPredicate);
        return this;
    }
    /**
     * Specify a {@link Consumer<T>} that executes on stopping the build {@link Pulse}.
     *
     * @param stopAction the final action of the pulse.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> onStop(Consumer<T> stopAction){
        this.stopAction =  this.stopAction.andThen(stopAction);
        return this;
    }

    /**
     * Specify the cycle at which the build {@link Pulse} is supposed to stop.
     *
     * @param stopCycle the final cycle of the pulse.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> stopAfter(int stopCycle){
        this.stopCycle = stopCycle;
        return this;
    }

    /**
     * Adds a repeating {@link PulseSequence}, executes if {@link Predicate#test(Object)} returns {@code true}.
     *
     * @param predicate mapped to the specified {@code action}.
     * @param action the action that execute after pass of associated {@code predicate}.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> add(Predicate<T> predicate, Consumer<T> action){
        consecutiveSequences.add(new PulseSequence<T>().addNext(predicate, action));
        return this;
    }
    /**
     * Adds a repeating {@link PulseSequence} with a specified {@link RandomUtil.Odds} of execution.
     *
     * @param odds the odds for the associated {@link Consumer} to execute.
     * @param action the action that execute after pass of associated {@code predicate}.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> add(RandomUtil.Odds odds, Consumer<T> action){
        return add(t -> RandomUtil.getRandom(odds.getRange()) == 1, action);
    }

    /**
     * Adds a repeating {@link PulseSequence}.
     *
     * @param action the action that execute after pass of associated {@code predicate}.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> add(Consumer<T> action){
        return add(t -> true, action);
    }

    /**
     * Maps a {@link PulseSequence} to the associated passed trigger cycle.
     * The sequence will execute after every {@code passedCycles} if the associated {@link Predicate#test(Object)} returns {@code true}.
     *
     * @param passedCycles the amount of cycles after which the {@code action} will execute.
     * @param predicate mapped to the specified {@code action}.
     * @param action the action that execute after pass of associated {@code predicate}.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> addEvery(int passedCycles, Predicate<T> predicate, Consumer<T> action){

        if(!consecutiveTriggerSequences.containsKey(passedCycles))
            consecutiveTriggerSequences.put(passedCycles, new PulseSequence<>());

        consecutiveTriggerSequences.compute(passedCycles,
                (key, pulse) ->
                        pulse.addNext(predicate, action));

        return this;
    }

    /**
     * Maps a {@link PulseSequence} to the associated passed trigger cycle.
     * The sequence will execute after every {@code passedCycles}.
     *
     * @param passedCycles the amount of cycles after which the {@code action} will execute.
     * @param action the action that execute after pass of associated {@code predicate}.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> addEvery(int passedCycles, Consumer<T> action){ return addEvery(passedCycles, t -> true, action); }

    /**
     * Maps a {@link PulseSequence} to the associated trigger cycle.
     * The sequence will execute once and only if the given {@link Predicate#test(Object)} returns {@code true}.
     *
     * @param predicate mapped to the specified {@code action}.
     * @param action the action that execute after pass of associated {@code predicate}.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> add(int trigger, Predicate<T> predicate, Consumer<T> action){

        if(!triggerSequences.containsKey(trigger))
            triggerSequences.put(trigger, new PulseSequence<>());

        if(trigger > finalTrigger)
            this.finalTrigger = trigger;

        triggerSequences.compute(trigger,
                (key, pulse) ->
                        pulse.addNext(predicate, action));

        return this;
    }
    /**
     * Maps a {@link PulseSequence} to the associated trigger cycle.
     * The sequence will execute only once.
     *
     * @param action the action that execute after pass of associated {@code predicate}.
     *
     * @return {@code this} {@link PulseBuilder}.
     */
    public PulseBuilder<T> add(int trigger, Consumer<T> action){
        return add(trigger, t -> true, action);
    }


    /**
     * Builds a new {@link Pulse} with a cycle delay of {@link #period},
     * with {@link #immediate} execution and bound to the {@link #getArchitect()}
     *
     * @return a new pulse.
     */
    @Override
    public Pulse build() {

        if(stopCycle == 0)
            stopCycle = finalTrigger;

        return new Pulse(period, nodes) {

            @Override
            public boolean pulse() {
                if(firstPulse){
                    firstPulse = false;
                    if(!immediate)
                        return false;
                }
                final long start = System.nanoTime();
                final boolean stop = currentCycle == stopCycle || stopPredicate.test(architect);
                if(stop)
                    stop();
                else if(pausePredicate.test(architect))
                    pauseAction.accept(architect);
                else if(currentCycle != 0 || startPredicate.test(architect)){

                    consecutiveSequences.forEach(sequences -> sequences.stream()
                        .filter(sequence ->
                            sequences.getOptionalPredicate(sequence)
                                .orElse(t -> true)
                                .test(architect))
                        .forEach(sequence -> sequence.accept(architect)));


                    consecutiveTriggerSequences.entrySet().stream()
                        .filter(entry -> currentCycle > entry.getKey() && currentCycle % entry.getKey() == 0)
                        .map(Map.Entry::getValue)
                        .findAny().ifPresent(sequences -> sequences.stream()
                        .filter(sequence -> sequences.getOptionalPredicate(sequence)
                            .orElse(t -> true)
                            .test(architect))
                        .forEach(sequence -> sequence.accept(architect)));

                    Optional.ofNullable(triggerSequences.get(currentCycle++))
                        .ifPresent(sequences -> sequences.stream()
                            .filter(sequence -> sequences.getOptionalPredicate(sequence)
                                .orElse(t -> true)
                                .test(architect))
                            .forEachOrdered(action -> action.accept(architect)));

                }
                final long duration = System.nanoTime()-start;
                final long ms = TimeUnit.NANOSECONDS.toMillis(duration);

                if(debug)
                    System.out.println("[PulseBuilder]: duration["+ms+"] "+toString());

                if(ms > 20){
                    World.sendAdminMessage("@RED@ Send screenshot to Developers. Pulse["+ duration + "][" + (currentCycle - 1) + " < " + stopCycle + "]["+ms+"].");
                    World.sendAdminMessage("@red@[PulseBuilder]: details -> "+toString());
                }
                return stop;
            }

            @Override
            public void stop() {
                super.stop();
                stopAction.accept(architect);
            }

            @Override
            public String toString() {
                return PulseBuilder.this.toString();
            }
        };
    }

    /**
     * The central object subjected to this {@link Pulse}.
     *
     * @return the architect.
     */
    public T getArchitect() {
        return architect;
    }

    @Override
    public String toString(){
        return "Pulse["+ (architect instanceof Entity ? "ENTITY_"+((Entity) architect).getName() : "NON_ENTITY_"+architect.getClass().getSimpleName())+"]: stop["+stopCycle+"], current["+currentCycle+"]";
    }

    public void start() {
        World.submit(build());
    }

    public void cancel() {
        this.stopWhen(t -> true);
    }

    public static void toggleDebug() {
        debug = !debug;
    }
}
