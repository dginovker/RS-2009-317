package org.gielinor.game.content.skill.member.thieving;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.misc.RandomUtil;
import org.gielinor.utilities.string.TextUtils;
import java.util.concurrent.TimeUnit;

/**
 * Represents the pulse used to thieve a stall.
 *
 * @author 'Vexia
 */
public final class StallThiefPulse extends SkillPulse<GameObject> {

    private static final long SILK_TRADER_COOLDOWN_IN_MILLIS = TimeUnit.MINUTES.toMillis(30);

    /**
     * Represents the stealing animation.
     */
    private static final Animation ANIMATION = new Animation(832);

    /**
     * Represents the stall being thieved.
     */
    private final Stall stall;

    /**
     * Represents the ticks passed.
     */
    private int ticks;

    /**
     * Constructs a new {@code StallThiefPulse} {@code Object}.
     *
     * @param player the player.
     * @param node   the node.
     * @param stall  the stall.
     */
    public StallThiefPulse(Player player, GameObject node, final Stall stall) {
        super(player, node);
        this.stall = stall;
    }

    @Override
    public boolean checkRequirements() {
        if (!node.isActive() || stall == null) {
            return false;
        }
        if (player.inCombat()) {
            player.getActionSender().sendMessage("You cant steal from the market stall during combat!");
            return false;
        }
        if (player.getSkills().getLevel(Skills.THIEVING) < stall.getLevel()) {
            player.getActionSender().sendMessage("You need to be level " + stall.getLevel() + " to steal from the " + node.getName().toLowerCase() + ".");
            return false;
        }
        if (player.getInventory().freeSlots() == 0) {
            player.getActionSender().sendMessage("You don't have enough inventory space.");
            return false;
        }
        return true;
    }

    @Override
    public void animate() {
    }

    @Override
    public boolean reward() {
        if (ticks == 0) {
            player.animate(ANIMATION);
        }
        if (++ticks % 2 != 0) {
            return false;
        }
        final boolean success = success();
        if (success) {
            final Item item = stall.getRandomLoot();
            if (stall == Stall.SILK_STALL) {
                // The silk trader won't talk to you for 30 minutes after stealing silk.
                player.getSavedData().getGlobalData().setSilkSteal(
                    System.currentTimeMillis() + SILK_TRADER_COOLDOWN_IN_MILLIS);
            }

            ObjectBuilder.replace(node, node.transform(stall.getTemporary()), stall.getDelay());
            player.getInventory().add(item, player);
            player.getSkills().addExperience(Skills.THIEVING, stall.getExperience());
            double randomOffset = RandomUtil.random(70, 130);
            randomOffset = randomOffset / 100; // turning 70 and 130 into 0.7 and 1.3 respectively
            double randomCoins = stall.getLevel() * 10;
            randomCoins *= randomOffset; // offset the amount by +- 30%
            int coins = (int) randomCoins;

            player.getInventory().add(new Item(Item.COINS, coins), player);
            player.getActionSender().sendMessage("You steal " + (TextUtils.isPlusN(item.getName()) ? "an" : "a") +
                " " + item.getName().toLowerCase() + " and " + coins + " coins from the " + node.getName().toLowerCase() + ".");
            return true;
        }
        for (NPC npc : RegionManager.getLocalNpcs(player.getLocation(), 5)) {
            if (!npc.getProperties().getCombatPulse().isAttacking() && (npc.getId() == 32 || npc.getId() == 2236)) {
                npc.sendChat("Hey! Get your hands off there!");
                npc.getProperties().getCombatPulse().attack(player);
                break;
            }
        }
        return true;
    }

    @Override
    public void message(int type) {
        switch (type) {
            case 0:
                player.getActionSender().sendMessage("You attempt to steal " + stall.getMessage() + "");
                break;
        }
    }

    /**
     * Checks if the thief is successful.
     *
     * @return {@code True} if so.
     */
    private boolean success() {
        return ((RandomUtil.getRandom(3) * player.getSkills().getLevel(Skills.THIEVING)) / 3) > (stall.getLevel() / 2);
    }

}
