package org.gielinor.game.content.skill.free.firemaking;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.game.world.update.flag.player.FaceLocationFlag;
import org.gielinor.utilities.misc.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the pulse used to light a log.
 *
 * @author 'Vexia
 */
public final class FiremakingPulse extends SkillPulse<Item> {

    private static final Logger log = LoggerFactory.getLogger(FiremakingPulse.class);

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(733);

    /**
     * Represents the tinderbox item.
     */
    public static final Item TINDERBOX = new Item(20590);

    /**
     * Represents the log being burned.
     */
    private final Log fire;

    /**
     * Represents the ground item.
     */
    private GroundItem groundItem;

    /**
     * Represents the ticks.
     */
    private int ticks;

    /**
     * Constructs a new {@code FiremakingPulse}.
     *
     * @param player     The player.
     * @param node       The node.
     * @param groundItem The ground item if not null.
     */
    public FiremakingPulse(Player player, Item node, GroundItem groundItem) {
        super(player, node);
        this.fire = Log.forId(node.getId());
        if (groundItem == null) {
            this.groundItem = new GroundItem(node, player.getLocation(), player);
            player.setAttribute("remove-log", true);
        } else {
            this.groundItem = groundItem;
            player.removeAttribute("remove-log");
        }
    }

    @Override
    public boolean checkRequirements() {
        if (fire == null) {
            log.warn("{} - attempted to make a bad fire with id: {}.", player.getName(), node.getId());
            return false;
        }
        if (RegionManager.getObject(player.getLocation()) != null || player.getZoneMonitor().isInZone("bank")) {
            player.getActionSender().sendMessage("You can't light a fire here.");
            return false;
        }
        if (!player.getInventory().containsItem(TINDERBOX)) {
            player.getActionSender().sendMessage("You do not have the required items to light this.");
            return false;
        }
        if (player.getSkills().getLevel(Skills.FIREMAKING) < fire.getLevel()) {
            player.getActionSender().sendMessage("You need a firemaking level of " + fire.getLevel() + " to light this log.");
            return false;
        }
        if (player.getAttribute("remove-log", false)) {
            player.removeAttribute("remove-log");
            if (player.getInventory().remove(node, node.getSlot(), true, false)) {
                GroundItemManager.create(groundItem);
            }
        }
        return true;
    }

    @Override
    public void animate() {
    }

    @Override
    public boolean reward() {
        if (getLastFire() >= World.getTicks()) {
            createFire();
            return true;
        }
        if (ticks == 0) {
            player.animate(ANIMATION);
        }
        if (++ticks % 3 != 0) {
            return false;
        }
        if (ticks % 12 == 0) {
            player.animate(ANIMATION);
        }
        if (!success()) {
            return false;
        }
        createFire();
        return true;
    }

    /**
     * Creates the fire.
     */
    public void createFire() {
        if (!groundItem.isActive()) {
            return;
        }
        final GameObject object = new GameObject(fire.getFireId(), player.getLocation());
        ObjectBuilder.add(object, fire.getLife(), getAsh(player, fire, object));
        GroundItemManager.destroy(groundItem);
        player.moveStep();
        player.getSkills().addExperience(Skills.FIREMAKING, fire.getXp());
        player.faceLocation(FaceLocationFlag.getFaceLocation(player, object));
        setLastFire();
    }

    @Override
    public void message(int type) {
        switch (type) {
            case 0:
                player.getActionSender().sendMessage("You attempt to light the logs...", 1);
                break;
            case 1:
                player.getActionSender().sendMessage("The fire catches and the logs begin to burn.", 1);
                if (fire.name().equals("OAK") &&
                    (player.getAchievementRepository().getAchievements().get(AchievementTask.CHOP_BURN_OAK_LOGS) != null &&
                        player.getAchievementRepository().getAchievements().get(AchievementTask.CHOP_BURN_OAK_LOGS) == 1)) {
                    AchievementDiary.finalize(player, AchievementTask.CHOP_BURN_OAK_LOGS);
                }
                burnAllLogs();
                break;
        }
    }

    private void burnAllLogs() {
        int randomNumber = RandomUtil.random(101);
        if (player.getPerkManager().isTriggered(Perk.FIRESTARTER)
            || player.getDonorManager().getDonorStatus().getBurnAllLogsChance() != -1
            && randomNumber <= player.getDonorManager().getDonorStatus().getBurnAllLogsChance()) {
            Item logItem = new Item(fire.getLogId(), 28);
            int logCount = player.getInventory().getCount(logItem);
            double expReward = fire.getXp() * logCount;

            player.getInventory().remove(logItem);
            player.getSkills().addExperience(Skills.FIREMAKING, expReward);

            player.getActionSender().sendMessage("<col=FF0000>The " + logItem.getName() + " in your inventory all catch fire too, but you're seemingly unharmed.</col>");
            player.playGraphics(Graphics.create(346, 100));
        }
    }

    /**
     * Gets the last firemake.
     *
     * @return the tick.
     */
    public int getLastFire() {
        return player.getAttribute("last-firemake", 0);
    }

    /**
     * Sets the last fire.
     */
    public void setLastFire() {
        player.setAttribute("last-firemake", World.getTicks() + 2);
    }

    /**
     * Gets the ground item ash.
     *
     * @param object the object.
     * @return {@code GroundItem} the itemm.
     */
    public static GroundItem getAsh(final Player player, Log fire, final GameObject object) {
        final GroundItem ash = new GroundItem(new Item(592), object.getLocation(), player);
        ash.setDecayTime(fire.getLife() + 200);
        return ash;
    }

    /**
     * Checks if the player gets rewarded.
     *
     * @return <code>True</code> if so.
     */
    private boolean success() {
        int level = 1 + player.getSkills().getLevel(Skills.FIREMAKING);
        double hostRatio = Math.random() * fire.getLevel();
        double clientRatio = Math.random() * ((level - fire.getLevel()) * (1 + (fire.getLevel() * 0.01)));
        return hostRatio < clientRatio;
    }

}
