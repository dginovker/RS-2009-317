package org.gielinor.game.content.global;

import org.gielinor.game.content.global.distraction.treasuretrail.ClueLevel;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.fishing.Fish;

import java.security.SecureRandom;
import java.util.*;

/**
 * Represent clue bottle drops, can be obtained through the {@link Skills#FISHING} skill.
 * wiki: http://oldschoolrunescape.wikia.com/wiki/Clue_bottle
 *
 * todo: add elite clue scroll level
 *
 * Created by Stan van der Bend on 16/01/2018.
 *
 * project: Gielinor-Server
 * package: org.gielinor.game.content.global
 */
public enum ClueBottle {

    EASY(33648, ClueLevel.EASY),
    MEDIUM(33649, ClueLevel.MEDIUM),
    HARD(33650, ClueLevel.HARD),
    ELITE(33651, ClueLevel.HARD);

    private final int clueBottleID;
    private final ClueLevel clueLevel;

    ClueBottle(int clueBottleID, ClueLevel clueLevel) {
        this.clueBottleID = clueBottleID;
        this.clueLevel = clueLevel;
    }

    public int getClueBottleID() {
        return clueBottleID;
    }

    public ClueLevel getClueLevel() {
        return clueLevel;
    }

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static void main(String[] args){

        final Fish simulatedFish = Fish.ANGLER_FISH;
        final int simulations = 1000;
        final List<ClueBottle> receivedBottles = new ArrayList<>();
        System.out.println("Simulating "+simulations+"x "+simulatedFish.name()+" catches.");
        for(int catches = 0; catches < simulations; catches++){
            receiveClueBottle(simulatedFish, 99)
                .ifPresent(receivedBottles::add);
                    //clueBottle -> System.out.println("Received "+clueBottle.name()+" clue bottle. Catch "+ finalCatches +"."));
        }
        for(ClueBottle type : values())
            System.out.println("Received "+ Collections.frequency(receivedBottles, type) +" "+type.name()+" clue bottles.");

    }

    public static Optional<ClueBottle> receiveClueBottle(Fish fish, int fishingLevel) {

        final OptionalInt base_chance = getBaseChance(fish);

        if (base_chance.isPresent()) {

            final int bound = base_chance.getAsInt() / (100 + fishingLevel);

            if (SECURE_RANDOM.nextInt(bound) == 0) {

                final int chance = SECURE_RANDOM.nextInt(100);

                ClueBottle bottle = ClueBottle.EASY;

                if (chance > 30 && chance <= 60)
                    bottle = MEDIUM;
                else if (chance > 10 && chance <= 30)
                    bottle = HARD;
                else if (chance > 0 && chance <= 10)
                    bottle = ELITE;

                return Optional.of(bottle);
            }
        }

        return Optional.empty();
    }

    private static OptionalInt getBaseChance(Fish fish){
        switch (fish) {
            case LEAPING_TROUT:
            case LEAPING_SALMON:
            case LEAPING_STRUGEON:
                return OptionalInt.of(1_280_882);
            case MACKEREL:
            case COD:
            case BASS:
                return OptionalInt.of(1_147_827);
            case SARDINE:
            case HERRING:
                return OptionalInt.of(1_056_000);
            case TROUT:
            case SALMON:
                return OptionalInt.of(923_616);
            case SHRIMP:
            case ANCHOVIE:
                return OptionalInt.of(870_330);
            case KARAMBWANJI:
                return OptionalInt.of(443_697);
            case PIKE:
                return OptionalInt.of(305_792);
            case TUNA:
            case SWORDFISH:
                return OptionalInt.of(257_770);
            case KARAMBWAN:
                return OptionalInt.of(170_874);
            case DARK_CRAB:
                return OptionalInt.of(149_434);
            case MONKFISH:
                return OptionalInt.of(138_583);
            case RAINBOW_FISH:
                return OptionalInt.of(137_739);
            case LOBSTER:
                return OptionalInt.of(116_129);
            case SACRED_EEL:
                return OptionalInt.of(99_000);
            case SHARK:
                return OptionalInt.of(82_243);
            case ANGLER_FISH:
                return OptionalInt.of(78_549);
        }
        return OptionalInt.empty();
    }

    public static ClueBottle forItemWith(int id) {
        for(ClueBottle clueBottle : values())
            if (clueBottle.getClueBottleID() == id)
                return clueBottle;
        return null;
    }
}
