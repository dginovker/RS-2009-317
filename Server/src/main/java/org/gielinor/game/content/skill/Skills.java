package org.gielinor.game.content.skill;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.world.update.flag.player.AppearanceFlag;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.SkillContext;
import org.gielinor.net.packet.context.XPDropContext;
import org.gielinor.net.packet.out.SkillLevel;
import org.gielinor.net.packet.out.XPDrop;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.string.TextUtils;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Represents an entity's skills.
 *
 * @author Emperor
 */
public final class Skills implements SavingModule {

    /**
     * Represents an array of skill names.
     */
    public static final String[] SKILL_NAME = {
        "Attack", "Defence", "Strength", "Hitpoints",
        "Ranged", "Prayer", "Magic", "Cooking",
        "Woodcutting", "Fletching", "Fishing", "Firemaking",
        "Crafting", "Smithing", "Mining", "Herblore",
        "Agility", "Thieving", "Slayer", "Farming",
        "Runecrafting", "Construction", "Hunter", "Summoning"
    };
    /**
     * Constants for the skill ids.
     */
    public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
        HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6, COOKING = 7,
        WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
        CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15, AGILITY = 16,
        THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20, CONSTRUCTION = 21, HUNTER = 22, SUMMONING = 23;
    /**
     * Represents currently enabled skills.
     */
    public static final boolean[] ENABLED_SKILLS = new boolean[]{
        true, true, true, true,
        true, true, true, true,
        true, true, true, true,
        true, true, true, true,
        true, true, true, true,
        true, false, true, true
    };
    /**
     * Represents the entity instance.
     */
    private final Entity entity;
    /**
     * An array containing all the player's experience.
     */
    private final double[] experience;
    /**
     * An array containing all the maximum levels.
     */
    private final int[] staticLevels;
    /**
     * An array containing all the current levels.
     */
    private final int[] dynamicLevels;
    /**
     * Represents the amount of prayer points left.
     */
    private double prayerPoints = 1.;
    /**
     * The player's life-points.
     */
    private int lifepoints = 10;
    /**
     * The amount of increased maximum lifepoints.
     */
    private int lifepointsIncrease = 0;
    /**
     * The total experience gained.
     */
    private int experienceGained = 0;
    /**
     * The restoration pulses.
     */
    private final SkillRestoration[] restoration;
    /**
     * If a lifepoints update should occur.
     */
    private boolean lifepointsUpdate;

    /**
     * Constructs a new {@code Skills} {@code Object}.
     *
     * @param entity The entity.
     */
    public Skills(Entity entity) {
        this.entity = entity;
        this.experience = new double[24];
        this.staticLevels = new int[24];
        this.dynamicLevels = new int[24];
        this.restoration = new SkillRestoration[24];
        for (int i = 0; i < 24; i++) {
            this.staticLevels[i] = 1;
            this.dynamicLevels[i] = 1;
        }
        this.experience[HITPOINTS] = 1154;
        this.dynamicLevels[HITPOINTS] = 10;
        this.staticLevels[HITPOINTS] = 10;
        entity.getProperties().setCombatLevel(3);
    }

    public void reset() {
        for (int i = 0; i < 24; i++) {
            this.staticLevels[i] = 1;
            this.dynamicLevels[i] = 1;
        }
        this.experience[HITPOINTS] = 1154;
        this.dynamicLevels[HITPOINTS] = 10;
        this.staticLevels[HITPOINTS] = 10;
        entity.getProperties().setCombatLevel(3);
    }

    /**
     * Resets the skill, and the exp.
     *
     * @param id the ID of the skill.
     */
    public void resetSkill(int id) {
        this.staticLevels[id] = id == HITPOINTS ? 10 : 1;
        this.dynamicLevels[id] = id == HITPOINTS ? 10 : 1;
        this.experience[id] = id == HITPOINTS ? 1154 : 0;
        refresh();
        updateCombatLevel();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.getAppearance().sync();
        }
    }

    /**
     * Configures the skills.
     */
    public void configure() {
        updateCombatLevel();
        int max = 24;
        if (entity instanceof NPC) {
            max = 7;
        }
        for (int i = 0; i < max; i++) {
            if (i != PRAYER && restoration[i] == null) {
                configureRestorationPulse(i);
            }
        }
    }

    /**
     * Called every pulse (600ms).
     */
    public void pulse() {
        if (lifepoints < 1) {
            return;
        }
        for (SkillRestoration aRestoration : restoration) {
            if (aRestoration != null) {
                aRestoration.restore(entity);
            }
        }
    }

    /**
     * Configures a restoration pulse for the given skill id.
     *
     * @param skillId The skill id.
     */
    private void configureRestorationPulse(final int skillId) {
        restoration[skillId] = new SkillRestoration(skillId);
    }

    /**
     * Copies the skills data.
     *
     * @param skills The skills.
     */
    public void copy(Skills skills) {
        for (int i = 0; i < 24; i++) {
            this.staticLevels[i] = skills.staticLevels[i];
            this.dynamicLevels[i] = skills.dynamicLevels[i];
            this.experience[i] = skills.experience[i];
        }
        prayerPoints = skills.prayerPoints;
        lifepoints = skills.lifepoints;
        lifepointsIncrease = skills.lifepointsIncrease;
        experienceGained = skills.experienceGained;
    }

    /**
     * Adds experience to a skill.
     *
     * @param slot       The skill slot.
     * @param experience The experience.
     */
    public void addExperience(int slot, double experience) {
        double mod = ((slot == 0 || slot == 1 || slot == 2 || slot == 3 || slot == 4 || slot == 6 || slot == 18) ?
            Constants.COMBAT_EXP_MODIFIER : Constants.NON_COMBAT_EXP_MODIFIER);
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.getSavedData().getGlobalData().getExperienceModifierTime() > 0 && player.getSavedData().getGlobalData().getExperienceModifier() > 1) {
                mod = (mod * player.getSavedData().getGlobalData().getExperienceModifier());
            }
        }
        if (entity instanceof Player) {
            Player p = (Player) entity;
            p.getAntiMacroHandler().registerExperience(slot, experience);
        }
        this.experience[slot] += (experience * mod);
        if (this.experience[slot] > 200_000_000) {
            this.experience[slot] = 200_000_000;
        }
        experienceGained = experienceGained + (int) (experience * mod);
        int newLevel = getStaticLevelByExperience(slot);
        if (newLevel > staticLevels[slot]) {
            if (dynamicLevels[slot] < newLevel) {
                dynamicLevels[slot] += newLevel - staticLevels[slot];
            }
            staticLevels[slot] = newLevel;
            if (entity instanceof Player) {
                Player p = (Player) entity;
                if (updateCombatLevel()) {
                    if (!p.getSavedData().getGlobalData().hasAnnouncedCombat() && p.getProperties().getCurrentCombatLevel() == Constants.MAX_COMBAT_LEVEL) {
                        p.getSavedData().getGlobalData().setCombatAnnounced(true);
                        p.sendGlobalNewsMessage("ff8c38", " has reached the max combat level of " + Constants.MAX_COMBAT_LEVEL + "!", 5);
                    }
                    p.getUpdateMasks().register(new AppearanceFlag(p));
                }
                handleMilestones(p);
                if (getExperience(slot) == 200_000_000) {
                    if (!p.getSavedData().getGlobalData().getExperienceMilestone(slot)) {
                        p.getSavedData().getGlobalData().setExperienceMilestone(slot);
                        p.sendGlobalNewsMessage("ff0000", " has just reached 200,000,000 experience in " + TextUtils.formatDisplayName(SKILL_NAME[slot]), 6);
                    }
                }
                LevelUp.levelUp(p, slot);
            }
        }
        if (entity instanceof Player) {
            if (entity.asPlayer().getInterfaceState().get(InterfaceConfiguration.XP_DROP_LOCK) == 0) {
                entity.asPlayer().getSavedData().getGlobalData().increaseXpDrops((long) (experience * mod));
            }
            PacketRepository.send(XPDrop.class, new XPDropContext((Player) entity, slot, (int) (experience * mod)));
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, slot));
        }
    }

    private void handleMilestones(Player player) {
        if (player.getSavedData().getGlobalData().getNextMilestone() == -1) {
            return;
        }
        int[] milestones = new int[] { 99, 90, 80, 70, 60, 50, 40, 30, 20, 10 };
        if (areAllAtLeast(99) && player.getSavedData().getGlobalData().getNextMilestone() >= 99) {
            player.getSavedData().getGlobalData().setNextMilestone(-1);
            player.getSavedData().getGlobalData().setLastMilestone(99);
            player.sendGlobalNewsMessage("ff0000", " has just achieved level 99 in all skills!", 6);
            return;
        }
        for (int index = 0; index < milestones.length; index++) {
            if (areAllAtLeast(milestones[index])) {
                if (player.getSavedData().getGlobalData().getLastMilestone() >= milestones[index]) {
                    continue;
                }
                if (player.getSavedData().getGlobalData().getNextMilestone() >= milestones[index]) {
                    player.getSavedData().getGlobalData().setLastMilestone(milestones[index]);
                    player.getSavedData().getGlobalData().setNextMilestone(milestones[index - 1]);
                    player.sendGlobalNewsMessage("45b247", " has just achieved at least level " + milestones[index] + " in all skills!", 4);
                    break;
                }
            }
        }
    }

    /**
     * Adds experience to a skill without using modifiers.
     *
     * @param slot       The skill slot.
     * @param experience The experience.
     */
    public void addExperienceNoMod(int slot, double experience) {
        this.experience[slot] += (experience);
        if (this.experience[slot] > 200_000_000) {
            this.experience[slot] = 200_000_000;
        }
        experienceGained = experienceGained + (int) (experience);
        int newLevel = getStaticLevelByExperience(slot);
        if (newLevel > staticLevels[slot]) {
            if (dynamicLevels[slot] < newLevel) {
                dynamicLevels[slot] += newLevel - staticLevels[slot];
            }
            if (slot == HITPOINTS) {
                lifepoints += newLevel - staticLevels[slot];
            }
            staticLevels[slot] = newLevel;
            if (entity instanceof Player) {
                Player p = (Player) entity;
                if (updateCombatLevel()) {
                    if (!p.getSavedData().getGlobalData().hasAnnouncedCombat() && p.getProperties().getCurrentCombatLevel() == Constants.MAX_COMBAT_LEVEL) {
                        p.getSavedData().getGlobalData().setCombatAnnounced(true);
                        p.sendGlobalNewsMessage("ff8c38", " has reached the max combat level of " + Constants.MAX_COMBAT_LEVEL + "!", 5);
                    }
                    p.getUpdateMasks().register(new AppearanceFlag(p));
                }
                handleMilestones(p);
                if (getExperience(slot) == 200_000_000) {
                    if (!p.getSavedData().getGlobalData().getExperienceMilestone(slot)) {
                        p.getSavedData().getGlobalData().setExperienceMilestone(slot);
                        p.sendGlobalNewsMessage("ff0000", " has just reached 200,000,000 experience in " + TextUtils.formatDisplayName(SKILL_NAME[slot]), 6);
                    }
                }
                LevelUp.levelUp(p, slot);
            }
        }
        if (entity instanceof Player) {
            if (entity.asPlayer().getInterfaceState().get(InterfaceConfiguration.XP_DROP_LOCK) == 0) {
                entity.asPlayer().getSavedData().getGlobalData().increaseXpDrops((long) experience);
            }
            PacketRepository.send(XPDrop.class, new XPDropContext((Player) entity, slot, (int) experience));
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, slot));
        }
    }

    /**
     * Gets the highest combat skill id.
     *
     * @return The id of the highest combat skill.
     */
    public int getHighestCombatSkill() {
        int id = 0;
        int last = 0;
        for (int i = 0; i < 5; i++) {
            if (staticLevels[i] > last) {
                last = staticLevels[i];
                id = i;
            }
        }
        return id;
    }

    /**
     * Returns the dynamic levels to the static levels
     */
    public void restore() {
        for (int i = 0; i < 24; i++) {
            int staticLevel = getStaticLevel(i);
            setLevel(i, staticLevel);
        }
        if (entity instanceof Player) {
            ((Player) entity).getActionSender().sendSound(new Audio(2674));
        }
        rechargePrayerPoints();
    }

    /**
     * Parses the skill data from the buffer.
     *
     * @param buffer The byte buffer.
     */
    @Override
    public void parse(ByteBuffer buffer) {
        for (int i = 0; i < 24; i++) {
            experience[i] = ((double) buffer.getInt() / 10D);
            dynamicLevels[i] = buffer.get() & 0xFF;
            if (i == HITPOINTS) {
                lifepoints = dynamicLevels[i];
            } else if (i == PRAYER) {
                prayerPoints = dynamicLevels[i];
            }
            staticLevels[i] = buffer.get() & 0xFF;
        }
        experienceGained = buffer.getInt();
    }

    /**
     * Saves the skill data on the buffer.
     *
     * @param buffer The byte buffer.
     */
    @Override
    public void save(ByteBuffer buffer) {
        for (int i = 0; i < 24; i++) {
            buffer.putInt((int) (experience[i] * 10));
            if (i == HITPOINTS) {
                buffer.put((byte) lifepoints);
            } else if (i == PRAYER) {
                buffer.put((byte) Math.ceil(prayerPoints));
            } else {
                buffer.put((byte) dynamicLevels[i]);
            }
            buffer.put((byte) staticLevels[i]);
        }
        buffer.putInt(experienceGained);
    }

    /**
     * Refreshes all the skill levels.
     */
    public void refresh() {
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player) entity;
        for (int i = 0; i < 24; i++) {
            PacketRepository.send(SkillLevel.class, new SkillContext(player, i));
        }
    }

    /**
     * Gets the level for the experience.
     *
     * @param exp The experience.
     * @return The level.
     */
    public int getLevelByExperience(double exp) {
        int points = 0;
        int output;
        for (byte lvl = 1; lvl < 100; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);
            if ((output - 1) >= exp) {
                return lvl;
            }
        }
        return 99;
    }

    /**
     * Gets the static level.
     *
     * @param slot The skill's slot.
     * @return The level.
     */
    public int getStaticLevelByExperience(int slot) {
        double exp = experience[slot];
        int points = 0;
        int output;
        for (byte lvl = 1; lvl < 100; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);
            if ((output - 1) >= exp) {
                return lvl;
            }
        }
        return 99;
    }

    /**
     * Gets the experience for a certain level.
     *
     * @param level The level.
     * @return The experience needed.
     */
    public int getExperienceForLevel(int level) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= level; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            if (lvl >= level) {
                return output;
            }
            output = (int) Math.floor(points / 4);
        }
        return 0;
    }

    /**
     * Updates the combat level.
     *
     * @return <code>True</code> if the combat level changed.
     */
    public boolean updateCombatLevel() {
        boolean update;
        int level = calculateCombatLevel();
        update = level != entity.getProperties().getCurrentCombatLevel();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            int summon = staticLevels[SUMMONING] / 8;
            if (summon != player.getFamiliarManager().getSummoningCombatLevel()) {
                player.getFamiliarManager().setSummoningCombatLevel(summon);
                update = true;
            }
        }
        entity.getProperties().setCombatLevel(level);
        return update;
    }

    /**
     * Gets the combat level (ignoring summoning).
     *
     * @return The combat level.
     */
    public int calculateCombatLevel() {
        if (entity instanceof NPC) {
            return ((NPC) entity).getDefinition().getCombatLevel();
        }
        int combatLevel;
        int melee = staticLevels[ATTACK] + staticLevels[STRENGTH];
        int range = (int) (1.5 * staticLevels[RANGE]);
        int mage = (int) (1.5 * staticLevels[MAGIC]);
        if (melee > range && melee > mage) {
            combatLevel = melee;
        } else if (range > melee && range > mage) {
            combatLevel = range;
        } else {
            combatLevel = mage;
        }
        combatLevel = staticLevels[DEFENCE] + staticLevels[HITPOINTS] + (staticLevels[PRAYER] / 2) + (int) (1.3 * combatLevel);
        return combatLevel / 4;
    }

    /**
     * @return the player
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Gets the experience.
     *
     * @param slot The slot.
     * @return The experience.
     */
    public double getExperience(int slot) {
        return experience[slot];
    }

    /**
     * Gets the static skill level.
     *
     * @param slot The slot.
     * @return The static level.
     */
    public int getStaticLevel(int slot) {
        return staticLevels[slot];
    }
    public int getLevelsTillMastery(int slot) {
        return 99 - staticLevels[slot];
    }

    /**
     * Checks if all of the player's levels are at least the given amount.
     *
     * @param level The level.
     * @return Whether or not the levels are at least the amount.
     */
    public boolean areAllAtLeast(int level) {
        for (int i = 0; i < SKILL_NAME.length; i++) {
            if (!ENABLED_SKILLS[i]) {
                continue;
            }
            // TODO CONSTRUCTION
            if (Objects.equals(SKILL_NAME[i], "Construction")) {
                continue;
            }
            if (getStaticLevel(i) < level) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the experience gained.
     *
     * @param experienceGained The experience gained.
     */
    public void setExperienceGained(int experienceGained) {
        this.experienceGained = experienceGained;
    }

    /**
     * Gets the experience gained.
     *
     * @return The experience gained.
     */
    public int getExperienceGained() {
        return experienceGained;
    }

    /**
     * Sets a skill.
     *
     * @param skillId The skill id.
     * @param level   The level.
     * @param exp     The experience.
     */
    public void setSkill(int skillId, int level, double exp) {
        experience[skillId] = exp;
        dynamicLevels[skillId] = level;
        if (skillId == HITPOINTS) {
            lifepoints = level;
        }
        if (skillId == Skills.PRAYER) {
            prayerPoints = level;
        }
        staticLevels[skillId] = getStaticLevelByExperience(skillId);
        if (restoration[skillId] != null) {
            int ticks = 100;
            if (entity instanceof Player) {
                if (((Player) entity).getPrayer().get(PrayerType.BERSERKER)) {
                    ticks = 75;
                }
            }
            restoration[skillId].restart(ticks);
        }
    }

    /**
     * Sets a dynamic level.
     *
     * @param slot  The skill id.
     * @param level The level.
     */
    public void setLevel(int slot, int level, boolean update) {
        if (slot == HITPOINTS) {
            lifepoints = level;
        } else if (slot == PRAYER) {
            prayerPoints = level;
        }
        dynamicLevels[slot] = level;
        if (restoration[slot] != null) {
            int ticks = 100;
            if (entity instanceof Player) {
                if (((Player) entity).getPrayer().get(PrayerType.BERSERKER)) {
                    ticks = 75;
                }
            }
            restoration[slot].restart(ticks);
        }
        if (entity instanceof Player && update) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, slot));
        }
    }

    /**
     * Sets a dynamic level.
     *
     * @param slot  The skill id.
     * @param level The level.
     */
    public void setLevel(int slot, int level) {
        setLevel(slot, level, true);
    }

    /**
     * Gets a dynamic level.
     *
     * @param slot The skill id.
     * @return The dynamic level.
     */
    public int getLevel(int slot) {
        return dynamicLevels[slot];
    }

    /**
     * Sets the current amount of lifepoints.
     *
     * @param lifepoints The lifepoints.
     */
    public void setLifepoints(int lifepoints) {
        this.lifepoints = lifepoints;
        if (this.lifepoints < 0) {
            this.lifepoints = 0;
        }
        lifepointsUpdate = true;
    }

    /**
     * Gets the lifepoints.
     *
     * @return The lifepoints.
     */
    public int getLifepoints() {
        return lifepoints;
    }

    /**
     * Gets the maximum amount of lifepoints.
     *
     * @return The maximum amount.
     */
    public int getMaximumLifepoints() {
        return staticLevels[HITPOINTS] + lifepointsIncrease;
    }

    /**
     * Sets the amount of lifepoints increase.
     *
     * @param amount The amount.
     */
    public void setLifepointsIncrease(int amount) {
        this.lifepointsIncrease = amount;
    }

    /**
     * Updates the current amount of life points (by decrementing).
     *
     * @param amount The amount to decrement with.
     */
    public void decrementLifePoints(double amount) {
        lifepoints -= amount;
        if (lifepoints < 0) {
            lifepoints = 0;
        }
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, HITPOINTS));
        }
    }

    /**
     * Adds lifepoints to the entity.
     *
     * @param health The amount to add.
     * @return The amount of overflow.
     */
    public int heal(int health) {
        lifepoints += health;
        int left = 0;
        if (lifepoints > getMaximumLifepoints()) {
            left = lifepoints - getMaximumLifepoints();
            lifepoints = getMaximumLifepoints();
        }
        lifepointsUpdate = true;
        return left;
    }

    /**
     * @param damage The amount to remove.
     * @return The amount of overflow.
     * @Deprecated Use
     * {@link ImpactHandler#manualHit(Entity, int, ImpactHandler.HitsplatType)}
     * or <br>
     * the <b>hitsplat WILL NOT show and combat will be desynchronized!</b>
     */
    public int hit(int damage) {
        lifepoints -= damage;
        int left = 0;
        if (lifepoints < 0) {
            left = -lifepoints;
            lifepoints = 0;
        }
        lifepointsUpdate = true;
        return left;
    }

    /**
     * Gets the prayer points.
     *
     * @return The prayer points.
     */
    public double getPrayerPoints() {
        return prayerPoints;
    }

    /**
     * Recharges the prayer points.
     */
    public void rechargePrayerPoints() {
        prayerPoints = staticLevels[PRAYER];
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, PRAYER));
        }
    }

    /**
     * Updates the current amount of prayer points (by decrementing).
     *
     * @param amount The amount to decrement with.
     */
    public void decrementPrayerPoints(double amount) {
        if (entity.getAttribute("INFINITE_PRAYER") != null) {
            return;
        }
        prayerPoints -= amount;
        if (prayerPoints < 0) {
            prayerPoints = 0;
        }
        //		if (prayerPoints > staticLevels[PRAYER]) {
        //			prayerPoints = staticLevels[PRAYER];
        //		}
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, PRAYER));
        }
    }

    /**
     * Updates the current amount of prayer points (by incrementing)
     *
     * @param amount The amount to decrement with.
     */
    public void incrementPrayerPoints(double amount) {
        prayerPoints += amount;
        if (prayerPoints < 0) {
            prayerPoints = 0;
        }
        if (prayerPoints > staticLevels[PRAYER]) {
            prayerPoints = staticLevels[PRAYER];
        }
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, PRAYER));
        }
    }

    /**
     * Sets the current prayer points (<b>without checking for being higher than
     * max</b>)
     *
     * @param amount The amount.
     */
    public void setPrayerPoints(double amount) {
        prayerPoints = amount;
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, PRAYER));
        }
    }

    /**
     * Updates the current skill level (by incrementing the current amount with
     * the given amount, up to the given maximum).
     *
     * @param skill   The skill id.
     * @param amount  The amount to increment.
     * @param maximum The maximum amount the skill can be.
     * @return The amount of "overflow".
     */
    public int updateLevel(int skill, int amount, int maximum) {
        if (amount > 0 && dynamicLevels[skill] > maximum) {
            return -amount;
        }
        int left = (dynamicLevels[skill] + amount) - maximum;
        int level = dynamicLevels[skill] += amount;
        if (level < 0) {
            dynamicLevels[skill] = 0;
        } else if (amount < 0 && level < maximum) {
            dynamicLevels[skill] = maximum;
        } else if (amount > 0 && level > maximum) {
            dynamicLevels[skill] = maximum;
        }
        if (restoration[skill] != null) {
            int ticks = 100;
            if (entity instanceof Player) {
                if (((Player) entity).getPrayer().get(PrayerType.BERSERKER)) {
                    ticks = 75;
                }
            }
            restoration[skill].restart(ticks);
        }
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, skill));
        }
        return left;
    }

    /**
     * Decreases a level to its minimum.
     *
     * @param skill        The skill id.
     * @param modification The modification amount.
     */
    public void decreaseLevelToMinimum(int skill, int modification) {
        if (dynamicLevels[skill] > 1) {
            setLevel(skill, dynamicLevels[skill] - modification <= 1 ? 1 : dynamicLevels[skill] - modification);
        }
    }

    /**
     * Updates a level.
     *
     * @param skill  the skill.
     * @param amount the amount.
     * @return the left.
     */
    public int updateLevel(int skill, int amount) {
        return updateLevel(skill, amount, amount >= 0 ? getStaticLevel(skill) + amount : getStaticLevel(skill) - amount);
    }

    /**
     * Drains a certain percentage of a level.
     *
     * @param skill                  The skill.
     * @param drainPercentage        The drain percentage (0.05 indicates 5%
     *                               drain).
     * @param maximumDrainPercentage The maximum drain percentage (0.05
     *                               indicates 5%).
     */
    public void drainLevel(int skill, double drainPercentage, double maximumDrainPercentage) {
        int drain = (int) (dynamicLevels[skill] * drainPercentage);
        int minimum = (int) (staticLevels[skill] * (1.0 - maximumDrainPercentage));
        updateLevel(skill, -drain, minimum);
    }

    /**
     * Sets the static level.
     *
     * @param skill The skill id.
     * @param level The level to set.
     */
    public void setStaticLevel(int skill, int level, boolean update) {
        experience[skill] = getExperienceForLevel(staticLevels[skill] = dynamicLevels[skill] = level);
        if (entity instanceof Player && update) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, skill));
        }
    }

    /**
     * Sets the static level.
     *
     * @param skill The skill id.
     * @param level The level to set.
     */
    public void setStaticLevel(int skill, int level) {
        setStaticLevel(skill, level, true);
    }

    /**
     * Sets the experience.
     *
     * @param skill The skill id.
     * @param exp   The experience to set.
     */
    public void setExperience(int skill, double exp, boolean update) {
        experience[skill] = exp;
        if (entity instanceof Player && update) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, skill));
        }
    }

    /**
     * Sets the experience.
     *
     * @param skill The skill id.
     * @param exp   The experience to set.
     */
    public void setExperience(int skill, double exp) {
        setExperience(skill, exp, true);
    }

    /**
     * Gets the restoration pulses.
     *
     * @return The restoration pulse array.
     */
    public SkillRestoration[] getRestoration() {
        return restoration;
    }

    /**
     * Gets the amount of mastered skills.
     *
     * @return The amount of mastered skills.
     */
    public int getMasteredSkills() {
        int count = 0;
        for (int i = 0; i < getTotalSkills(); i++) {
            if (getStaticLevel(i) >= 99) {
                count++;
            }
        }
        return count;
    }

    public int getTotalSkills(){
        return 23;
    }

    /**
     * Method used to get the skill by the name.
     *
     * @param name the name.
     * @return the skill.
     */
    public static int getSkillByName(final String name) {
        for (int i = 0; i < SKILL_NAME.length; i++) {
            if (SKILL_NAME[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the total level.
     *
     * @return the total level.
     */
    public int getTotalLevel() {
        int level = 0;
        for (int i = 0; i < staticLevels.length; i++) {
            level += getStaticLevel(i);
        }
        return level;
    }

    /**
     * Gets the total experience.
     *
     * @return The total experience.
     */
    public long getTotalExperience() {
        int total = 0;
        for (int i = 0; i < staticLevels.length; i++) {
            total += getExperience(i);
        }
        return total;
    }

    /**
     * Gets the lifepointsUpdate.
     *
     * @return The lifepointsUpdate.
     */
    public boolean isLifepointsUpdate() {
        return lifepointsUpdate;
    }

    /**
     * Sets the lifepointsUpdate.
     *
     * @param lifepointsUpdate The lifepointsUpdate to set.
     */
    public void setLifepointsUpdate(boolean lifepointsUpdate) {
        this.lifepointsUpdate = lifepointsUpdate;
    }

}
