package org.gielinor.game.node.entity.npc.agg;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.RegionManager;

import plugin.skill.runecrafting.abyss.AbyssalNPC;

/**
 * Handles an NPC's aggressive behaviour.
 *
 * @author Emperor
 */
public class AggressiveBehavior {

    /**
     * The default aggressive behavior.
     */
    public static final AggressiveBehavior DEFAULT = new AggressiveBehavior();

    /**
     * The wilderness aggressive behavior.
     */
    public static final AggressiveBehavior WILDERNESS = new AggressiveBehavior() {

        @Override
        public boolean canSelectTarget(Entity entity, Entity target) {
            if (!target.isActive() || DeathTask.isDead(target)) {
                return false;
            }
            if (target instanceof Player) {
                if (((Player) target).getSavedData().getGlobalData().getVisibility() != 0) {
                    return false;
                }
            }
            if (!entity.canSelectTarget(target)) {
                return false;
            }
            if (!target.getProperties().isMultiZone() && target.inCombat()) {
                return false;
            }
            if (target instanceof Player) {
                return ((Player) target).getSkullManager().isWilderness();
            }
            return true;
        }
    };

    /**
     * Constructs a new {@code AggressiveBehaviour} {@code Object}.
     */
    public AggressiveBehavior() {
        /*
         * empty.
         */
    }

    /**
     * Checks if the NPC is aggressive towards the entity.
     *
     * @param entity The npc wanting to target another entity
     * @param target The entity being targetted
     * @return <code>True</code> if the NPC can select the entity as a target.
     */
    public boolean canSelectTarget(Entity entity, Entity target) {
        if (!target.isActive() || DeathTask.isDead(target)) {
            return false;
        }
        if (!target.getProperties().isMultiZone() && target.inCombat()) {
            return false;
        }
        if (target instanceof Player) {
            if (((Player) target).getSavedData().getGlobalData().getVisibility() != 0) {
                return false;
            }
        }
        if (entity instanceof NPC && target instanceof Player) {
            NPC npc = (NPC) entity;
            if (npc.getAggressiveHandler() != null && npc.getAggressiveHandler().isAllowTolerance()) {
                int ticks = World.getTicks() - npc.getAggressiveHandler().getPlayerTolerance()[target.getIndex()];
                if (ticks > 3000) {
                    npc.getAggressiveHandler().getPlayerTolerance()[target.getIndex()] = World.getTicks();
                } else if (ticks > 1500) {
                    return false;
                }
            }
            if (npc instanceof AbyssalNPC && Perk.KING_OF_THE_ABYSS.enabled((Player) target)) {
                return false;
            }
            if (npc.getName().toLowerCase().contains("kalphite") && !npc.getName().toLowerCase().contains("queen") && Perk.KALPHITE_KILLER.enabled((Player) target)) {
                return false;
            }
        }
        int level = target.getProperties().getCurrentCombatLevel();
        return level <= entity.getProperties().getCurrentCombatLevel() << 1 ||
            target.getLocation().getRegionId() == 14231 ||
            target.getLocation().getRegionId() == 12107;
    }

    /**
     * Gets the priority flag.
     *
     * @param target The target.
     * @return The priority flag.
     */
    public int getPriorityFlag(Entity target) {
        int flag = 0;
        if (target.inCombat()) {
            flag++;
        }
        if (target.getLocks().isInteractionLocked()) {
            flag++;
        }
        Entity e = target.getAttribute("aggressor");
        if (e != null && e.getProperties().getCombatPulse().getVictim() == target) {
            flag++;
        }
        return flag;
    }

    /**
     * Gets the list of possible targets.
     *
     * @param entity The entity.
     * @param radius The aggressive radius.
     * @return The list of possible targets.
     */
    public List<Entity> getPossibleTargets(Entity entity, int radius) {
        List<Entity> targets = new ArrayList<>();
        for (Player player : RegionManager.getLocalPlayers(entity, radius)) {
            if (canSelectTarget(entity, player)) {
                targets.add(player);
            }
        }
        return targets;
    }

    /**
     * Gets the most logical target from the targets list.
     *
     * @param entity          The entity.
     * @param possibleTargets The possible targets list.
     * @return The target.
     */
    public Entity getLogicalTarget(Entity entity, List<Entity> possibleTargets) {
        Entity target = null;
        int comparingFlag = Integer.MAX_VALUE;
        for (Entity e : possibleTargets) {
            int flag = getPriorityFlag(e);
            if (flag <= comparingFlag) {
                comparingFlag = flag;
                target = e;
            }
        }
        return target;
    }
}
