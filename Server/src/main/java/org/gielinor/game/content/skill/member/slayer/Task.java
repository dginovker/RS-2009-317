package org.gielinor.game.content.skill.member.slayer;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents a task to be set.
 *
 * @author 'Vexia
 */
public class Task {


    public enum location {
        ABERRANT_SPECTRES(),
        ABYSSAL_DEMONS(),
        ANKOU(),
        BASILISKS(),
        BATS(),
        BEARS(),
        BIRDS(),
        BLACK_DEMONS(),
        BLACK_DRAGONS(),
        BLOODVELDS(),
        BLUE_DRAGONS(),
        BRINE_RATS(),
        BRONZE_DRAGONS(),
        CATABLEPONS(),
        CAVE_BUG(),
        CAVE_CRAWLERS(),
        CAVE_HORRORS(),
        CAVE_SLIMES(),
        COCKATRICES(),
        COWS(),
        CRAWLING_HAND(),
        CROCODILES(),
        CYCLOPES(),
        DAGANNOTHS(),
        DARK_BEASTS(),
        DESERT_LIZARDS(),
        DOG(),
        DUST_DEVILS(),
        DWARF(),
        EARTH_WARRIORS(),
        ELVES(),
        FEVER_SPIDERS(),
        FIRE_GIANTS(),
        FLESH_CRAWLERS(),
        GARGOYLES(),
        GHOSTS(),
        GHOULS(),
        GOBLINS(),
        GORAKS(),
        GREATER_DEMONS(),
        GREEN_DRAGON(),
        HARPIE_BUG_SWARMS(),
        HELLHOUNDS(),
        HILL_GIANTS(),
        HOBGOBLINS(),
        ICE_FIENDS(),
        ICE_GIANTS(),
        ICE_WARRIOR(),
        INFERNAL_MAGES(),
        IRON_DRAGONS(),
        JELLIES(),
        JUNGLE_HORRORS(),
        KALPHITES(),
        KILLERWATTS(),
        KURASKS(),
        LESSER_DEMONS(),
        MITHRIL_DRAGON(),
        MINOTAURS(),
        MOGRES(),
        MOLANISKS(),
        MONKEYS(),
        MOSS_GIANTS(),
        NECHRYAELS(),
        OGRES(),
        OTHERWORDLY_BEING(),
        PYREFIENDS(),
        RATS(),
        RED_DRAGONS(),
        ROCK_SLUGS(),
        SCABARITES(),
        SCORPIONS(),
        SEA_SNAKES(),
        SHADE(),
        SHADOW_WARRIORS(),
        SKELETONS(),
        SPIDERS(),
        SPIRTUAL_WARRIORS(),
        STEEL_DRAGONS(),
        SUQAHS(),
        TERROR_DOG(),
        TROLLS(),
        TUROTHS(),
        TZHAAR(),
        VAMPIRES(),
        WALL_BEASTS(),
        WARPED_TERRORBIRDS(),
        WARPED_TORTOISES(),
        WATERFIENDS(),
        WEREWOLFS(),
        WOLVES(),
        ZOMBIES(),
        ZYGOMITES(),
        SKELETAL_WYVERN(),
    }

    /**
     * Represents the npc ids reated to the task.
     */
    private final int[] npcs;

    /**
     * Represents the tip to give.
     */
    private final Object[] tip;

    /**
     * Represents the masters able to assign this task.
     */
    private final Master[] masters;

    /**
     * The required slayer level.
     */
    private final int level;

    /**
     * If the task is killing undead NPCs.
     */
    private final boolean undead;

    /**
     * Any requirement equipment for this task.
     */
    private final Equipment[] equipment;

    /**
     * The task amount hash.
     */
    private int taskAmtHash;

    /**
     * Constructs a new {@code Task} {@code Object}.
     *
     * @param npcs      the npcs.
     * @param tip       the tips.
     * @param level     the level.
     * @param masters   the masters.
     * @param undead    if undead.
     * @param equipment the equipment.
     */
    public Task(int[] npcs, Object[] tip, int level, Master[] masters, boolean undead, Equipment... equipment) {
        this.npcs = npcs;
        this.tip = tip;
        this.level = level;
        this.masters = masters;
        this.undead = undead;
        this.equipment = equipment;
    }

    /**
     * Constructs a new {@code Task} {@code Object}.
     *
     * @param npcs        the npcs.
     * @param tip         the tips.
     * @param level       the level.
     * @param masters     the masters.
     * @param undead      if undead.
     * @param taskAmtHash the hash of amounts to recieve.
     * @param equipment   the equipment.
     */
    public Task(int[] npcs, Object[] tip, int level, Master[] masters, boolean undead, int taskAmtHash, Equipment... equipment) {
        this(npcs, tip, level, masters, undead, equipment);
        this.taskAmtHash = taskAmtHash;
    }

    /**
     * Checks if this task can be assigned to the player.
     *
     * @param player the player.
     * @param master the master.
     * @return {@code True} if so.
     */
    public boolean canAssign(Player player, Master master) {
        final int slayerLevel = player.getSkills().getStaticLevel(Skills.SLAYER);
        return !isDisabled() && getLevel() <= slayerLevel && hasEquipmentRequirement(player);
    }

    /**
     * Checks if they have the equipment requirement.
     *
     * @param player the player.
     * @return {@code True} if so.
     */
    public boolean hasEquipmentRequirement(final Player player) {
        for (Equipment e : equipment) {
            if (!e.hasRequirement(player)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the task contains the master given.
     *
     * @param master the master.
     * @return the {@code True} if so
     */
    public boolean hasMaster(Master master) {
        for (Master master1 : masters) {
            if (master1 == master) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the task is disabled.
     *
     * @return {@code True} if so.
     */
    public boolean isDisabled() {
        return false;
    }

    /**
     * Gets the ranges.
     *
     * @param master the master.
     * @return the ranges.
     */
    public int[] getRanges(Master master) {
        if (taskAmtHash != 0) {
            int min = taskAmtHash & 0xFFFF;
            int max = (taskAmtHash >> 16) & 0xFFFF;
            return new int[]{ min, max };
        }
        return master.getRanges();
    }

    /**
     * Gets the name.
     *
     * @return the name.
     */
    public String getName() {
        return NPCDefinition.forId(npcs[0]).getName();
    }

    /**
     * Gets the npcs.
     *
     * @return The npcs.
     */
    public int[] getNpcs() {
        return npcs;
    }

    /**
     * Gets the masters.
     *
     * @return The masters.
     */
    public Master[] getMasters() {
        return masters;
    }

    /**
     * Gets the level.
     *
     * @return The level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the tip.
     *
     * @return The tip.
     */
    public String[] getTip() {
        return (String[]) tip;
    }

    /**
     * Gets the undead.
     *
     * @return The undead.
     */
    public boolean isUndead() {
        return undead;
    }

    /**
     * Gets the equipment.
     *
     * @return The equipment.
     */
    public Equipment[] getEquipment() {
        return equipment;
    }

}
