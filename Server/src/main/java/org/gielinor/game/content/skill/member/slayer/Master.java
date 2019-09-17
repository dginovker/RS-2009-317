package org.gielinor.game.content.skill.member.slayer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents a slayer master.
 *
 * @author 'Vexia
 */
public enum Master {

    TURAEL(20401, 15, 30),
    MAZCHNA(20402, 30, 60) {
        @Override
        public boolean hasRequirment(Player player) {
            return player.getProperties().getCurrentCombatLevel() >= 20;
        }
    },
    VANNAKA(20403, 15, 60) {
        @Override
        public boolean hasRequirment(Player player) {
            return player.getProperties().getCurrentCombatLevel() >= 40;
        }
    },
    CHAELDAR(20404, 15, 75) {
        @Override
        public boolean hasRequirment(Player player) {
            return player.getProperties().getCurrentCombatLevel() >= 70 && player.getSkills().getLevel(Skills.SLAYER) >= 25;
        }
    },
    DURADEL(20405, 50, 120) {
        @Override
        public boolean hasRequirment(Player player) {
            return player.getProperties().getCurrentCombatLevel() >= 100 && player.getSkills().getLevel(Skills.SLAYER) >= 50;
        }
    };

    public static final List<Master> ALL; // because older kotlin versions are cucks
    
    static {
        Master[] masters = Master.values();
        ALL = Collections.unmodifiableList(Arrays.stream(masters).collect(Collectors.toList()));
    }

    /**
     * Represents the npc id.
     */
    private final int npc;

    /**
     * The ranges of task amts.
     */
    private final int[] ranges;

    /**
     * Constructs a new {@code Master} {@Code Object}.
     */
    Master(int npc, int... ranges) {
        this.npc = npc;
        this.ranges = ranges;
    }

    /**
     * Checks if the player has the requiremnts.
     *
     * @param player the player.
     * @return {@code True} if so.
     */
    public boolean hasRequirment(Player player) {
        return true;
    }

    /**
     * Gets the npc.
     *
     * @return The npc.
     */
    public int getNpc() {
        return npc;
    }

    /**
     * returns the value from the integer specification.
     *
     * @param id the id.
     * @return @app value.
     */
    public static Master forId(int id) {
        for (Master master : Master.values()) {
            if (master == null) {
                continue;
            }
            if (master.getNpc() == id) {
                return master;
            }
        }
        return null;
    }

    /**
     * Gets the ranges.
     *
     * @return The ranges.
     */
    public int[] getRanges() {
        return ranges;
    }

    /**
     * Checks if two masters share the same task.
     *
     * @param master   the master.
     * @param myMaster the players master.
     * @param player   the player.
     * @return {@code True} if so.
     */
    public static boolean hasSameTask(Master master, Master myMaster, Player player) {
        Task task = player.getSlayer().getTask();
        return master == myMaster || task.hasMaster(master);
    }
}
