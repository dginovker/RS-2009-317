package org.gielinor.game.node.entity.player.link;

import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.content.skill.member.slayer.Tasks;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SlayerKillLog {

    /**
     * The {@link java.util.Map} of Slayer NPC kills.
     */
    private final Map<String, Integer> killLog = new HashMap<>();

    /**
     * Constructs a new <code>SlayerKillLog</code>.
     */
    public SlayerKillLog() {
        /**
         * empty.
         */
    }

    /**
     * Gets the {@link #killLog}.
     *
     * @return The kill log.
     */
    public Map<String, Integer> getKillLog() {
        return killLog;
    }

    /**
     * Gets the name of a {@link org.gielinor.game.content.skill.member.slayer.Task}.
     *
     * @param npcId The id of the Slayer NPC.
     * @return The name.
     */
    public String getName(int npcId) {
        return Tasks.forId(npcId).getName();
    }

    /**
     * Gets a kill count for a {@link org.gielinor.game.content.skill.member.slayer.Task}.
     *
     * @param tasks The Slayer {@link org.gielinor.game.content.skill.member.slayer.Tasks}.
     * @return The kill count.
     */
    public int getKillCount(Tasks tasks) {
        if (killLog.get(tasks.name()) == null) {
            return killLog.put(tasks.name(), 0);
        }
        return killLog.get(tasks.name());
    }

    /**
     * Sets a kill count for a {@link org.gielinor.game.content.skill.member.slayer.Task}.
     *
     * @param tasks     The Slayer {@link org.gielinor.game.content.skill.member.slayer.Tasks}.
     * @param killCount The kill count to set.
     */
    public void setKillCount(Tasks tasks, int killCount) {
        killLog.put(tasks.name(), killCount);
    }

    /**
     * Increases a kill count for a {@link org.gielinor.game.content.skill.member.slayer.Task}.
     *
     * @param tasks The Slayer {@link org.gielinor.game.content.skill.member.slayer.Tasks}.
     */
    public void increaseKillCount(Tasks tasks) {
        if (tasks == null || tasks.name() == null) {
            return;
        }
        if (killLog.get(tasks.name()) == null) {
            killLog.put(tasks.name(), 1);
            return;
        }
        killLog.replace(tasks.name(), killLog.get(tasks.name()), killLog.get(tasks.name()) + 1);
    }
}
