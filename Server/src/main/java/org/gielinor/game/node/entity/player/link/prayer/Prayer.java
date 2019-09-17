package org.gielinor.game.node.entity.player.link.prayer;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.content.skill.SkillBonus;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.prayer.QuickPrayer;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents a managing class of a players prayers.
 *
 * @author 'Vexia
 * @author Emperor
 * @date 06/10/2013
 */
public final class Prayer {

    /**
     * Represents the list of active prayers.
     */
    private final List<PrayerType> active = new ArrayList<>();

    /**
     * Represents the current draining task.
     */
    private final DrainTask task = new DrainTask();

    /**
     * Represents the player instance.
     */
    private final Player player;

    /**
     * Represents the {@link org.gielinor.game.content.skill.free.prayer.QuickPrayer} instance.
     */
    private final QuickPrayer quickPrayer;

    /**
     * Constructs a new {@code Prayer} {@code Object}.
     */
    public Prayer(Player player) {
        this.player = player;
        this.quickPrayer = new QuickPrayer(player);
    }

    /**
     * Method used to toggle a prayer.
     *
     * @param type the type of prayer.
     */
    public final boolean toggle(final PrayerType type) {
        return permitted(type) && type.toggle(player, !active.contains(type));
    }

    /**
     * Toggles all prayers on or off.
     */
    public void toggleAll(boolean off) {
        for (PrayerType type : PrayerType.values()) {
            player.getConfigManager().set(type.getConfig(), off ? 0 : 1);
        }
        if (off) {
            getActive().clear();
            player.getAppearance().setHeadIcon(-1);
        }
        player.getAppearance().sync();
    }

    /**
     * Method used to reset this prayer managers cached prayers.
     */
    public void reset() {
        for (PrayerType type : getActive()) {
            player.getConfigManager().set(type.getConfig(), 0);
        }
        getActive().clear();
        player.getAppearance().setHeadIcon(-1);
        player.getAppearance().sync();
    }

    /**
     * Starts the redemption effect.
     */
    public void startRedemption() {
        player.getAudioManager().send(160);
        player.graphics(Graphics.create(436));
        player.getSkills().heal((int) (player.getSkills().getStaticLevel(Skills.PRAYER) * 0.25));
        player.getSkills().setPrayerPoints(0.0);
        reset();
    }

    /**
     * Starts the wrath effect.
     *
     * @param killer The entity who killed the player.
     *               <p>
     *               TODO Add GFX to client
     */
    public void startWrath(Entity killer) {
        Location l = player.getLocation();
        int x = l.getX();
        int y = l.getY();
        int z = l.getZ();
        player.getActionSender().sendPositionedGraphics(new Graphics(2036), new Location(x + 1, y + 1, z));
        player.getActionSender().sendPositionedGraphics(new Graphics(2036), new Location(x - 1, y - 1, z));
        player.getActionSender().sendPositionedGraphics(new Graphics(2036), new Location(x - 1, y + 1, z));
        player.getActionSender().sendPositionedGraphics(new Graphics(2036), new Location(x + 1, y - 1, z));
        player.getActionSender().sendPositionedGraphics(new Graphics(2036), new Location(x + 2, y, z));
        player.getActionSender().sendPositionedGraphics(new Graphics(2036), new Location(x - 2, y, z));
        player.getActionSender().sendPositionedGraphics(new Graphics(2036), new Location(x, y + 2, z));
        player.getActionSender().sendPositionedGraphics(new Graphics(2036), new Location(x, y - 2, z));
        player.getActionSender().sendPositionedGraphics(new Graphics(2036), new Location(x + 2, y + 2, z));
        player.getActionSender().sendPositionedGraphics(new Graphics(2036), new Location(x - 2, y - 2, z));
        player.getActionSender().sendPositionedGraphics(new Graphics(2036), new Location(x + 2, y - 2, z));
        player.getActionSender().sendPositionedGraphics(new Graphics(2036), new Location(x - 2, y + 2, z));
        int maximum = (int) ((player.getSkills().getStaticLevel(Skills.PRAYER) / 50) * 25) - 1;
        if (killer != player && killer.getLocation().withinDistance(player.getLocation(), 5)) {
            killer.getImpactHandler().manualHit(player, 1 + RandomUtil.randomize(maximum), HitsplatType.NORMAL);
        }
        if (player.getProperties().isMultiZone()) {
            @SuppressWarnings("rawtypes")
            List targets = null;
            if (killer instanceof NPC) {
                targets = RegionManager.getSurroundingNPCs(player, player, killer);
            } else {
                targets = RegionManager.getSurroundingPlayers(player, player, killer);
            }
            for (Object o : targets) {
                Entity entity = (Entity) o;
                if (entity.isAttackable(player, CombatStyle.MAGIC)) {
                    entity.getImpactHandler().manualHit(player, 1 + RandomUtil.randomize(maximum), HitsplatType.NORMAL);
                }
            }
        }
    }

    /**
     * Starts the retribution effect.
     *
     * @param killer The entity who killed this player.
     */
    public void startRetribution(Entity killer) {
        Location l = player.getLocation();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (x != 0 || y != 0) {
                    Projectile.create(l, l.transform(x, y, 0), 438, 0, 0, 10, 20, 0, 11).send();
                }
            }
        }
        player.getAudioManager().send(159);
        player.graphics(Graphics.create(437));
        int maximum = (int) (player.getSkills().getStaticLevel(Skills.PRAYER) * 0.25) - 1;
        if (killer != player && killer.getLocation().withinDistance(player.getLocation(), 1)) {
            killer.getImpactHandler().manualHit(player, 1 + RandomUtil.randomize(maximum), HitsplatType.NORMAL);
        }
        if (player.getProperties().isMultiZone()) {
            @SuppressWarnings("rawtypes") List targets = null;
            if (killer instanceof NPC) {
                targets = RegionManager.getSurroundingNPCs(player, player, killer);
            } else {
                targets = RegionManager.getSurroundingPlayers(player, player, killer);
            }
            for (Object o : targets) {
                Entity entity = (Entity) o;
                if (entity.isAttackable(player, CombatStyle.MAGIC)) {
                    entity.getImpactHandler().manualHit(player, 1 + RandomUtil.randomize(maximum), HitsplatType.NORMAL);
                }
            }
        }
    }

    /**
     * Method used to check if we're permitted to toggle this prayer.
     *
     * @param type the type.
     * @return <code>True</code> if permitted to be toggled.
     */
    private boolean permitted(final PrayerType type) {
        if (player.getSkills().getPrayerPoints() < 1) {
            player.getActionSender().sendMessage("You have run out of prayer points, you must recharge at an altar.");
            reset();
            return false;
        }
        return player.getZoneMonitor().canUsePrayer() && player.getSkills().getPrayerPoints() > 0 && type.permitted(player);
    }

    /**
     * Method used to return value of <code>True</code> if the {@link #active} prayers contains the prayer type.
     *
     * @param type the type of prayer.
     * @return <code>True</code> if so.
     */
    public boolean get(PrayerType type) {
        return active.contains(type);
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the task.
     *
     * @return The task.
     */
    public DrainTask getTask() {
        return task;
    }

    /**
     * Gets the active prayers.
     *
     * @return The active.
     */
    public List<PrayerType> getActive() {
        return active;
    }

    /**
     * Gets the {@link org.gielinor.game.content.skill.free.prayer.QuickPrayer} instance.
     *
     * @return The quick prayer instance.
     */
    public QuickPrayer getQuickPrayer() {
        return quickPrayer;
    }

    /**
     * Gets the skill bonus for the given skill id.
     *
     * @param skillId The skill id.
     * @return The bonus for the given skill.
     */
    public double getSkillBonus(int skillId) {
        double bonus = 0.0;
        for (PrayerType type : active) {
            for (SkillBonus b : type.getBonuses()) {
                if (b.getSkillId() == skillId) {
                    bonus += b.getBonus();
                }
            }
        }
        return bonus;
    }

}