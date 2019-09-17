package org.gielinor.game.content.eco;

import org.gielinor.parser.npc.NPCDropsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a managing class for the economy.
 *
 * @author Emperor
 */
public final class EconomyManagement {

    private static final Logger log = LoggerFactory.getLogger(EconomyManagement.class);

    /**
     * The current economy state.
     */
    private static EcoStatus ecoState = EcoStatus.MAINTAINING;

    /**
     * The modification rate.
     */
    private static double modificationRate = 0.1;

    /**
     * Sets the ecoState.
     *
     * @param ecoState The ecoState to set.
     */
    public static void update(EcoStatus ecoState, double modificationRate) {
        boolean update = EconomyManagement.ecoState != ecoState;
        EconomyManagement.ecoState = ecoState;
        if (EconomyManagement.modificationRate != modificationRate) {
            EconomyManagement.modificationRate = modificationRate;
            update = true;
        }
        if (update) {
            log.info("-------------------------------------------------------------------");
            log.info("Switched economy management status to {} with a rate of {}.", ecoState, modificationRate);
            log.info("-------------------------------------------------------------------");
            try {
                new NPCDropsParser().parse();
            } catch (Throwable ex) {
                log.error("Failed to parse NPC drops while changing economy state to: {}.", ecoState, ex);
            }
        }
    }

    /**
     * Sets the ecoState.
     *
     * @param ecoState The ecoState to set.
     */
    public static void updateEcoState(EcoStatus ecoState) {
        update(ecoState, modificationRate);
    }

    /**
     * Sets the modificationRate.
     *
     * @param modificationRate The modificationRate to set.
     */
    public static void updateModificationRate(double modificationRate) {
        update(ecoState, modificationRate);
    }

    /**
     * Gets the ecoState.
     *
     * @return The ecoState.
     */
    public static EcoStatus getEcoState() {
        return ecoState;
    }

    /**
     * Gets the modificationRate.
     *
     * @return The modificationRate.
     */
    public static double getModificationRate() {
        return modificationRate;
    }

}
