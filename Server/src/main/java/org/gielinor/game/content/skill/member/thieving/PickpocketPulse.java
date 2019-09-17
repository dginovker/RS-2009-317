package org.gielinor.game.content.skill.member.thieving;

import java.security.SecureRandom;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.global.distraction.treasuretrail.ClueLevel;
import org.gielinor.game.content.global.distraction.treasuretrail.ClueScrollPlugin;
import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.npc.drop.DropFrequency;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.node.item.ChanceItem;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the pulse used to pickpocket an npc.
 *
 * @author 'Vexia
 */
public final class PickpocketPulse extends SkillPulse<NPC> {

    /**
     * Represents the animation specific to pickpocketing.
     */
    private static final Animation ANIMATION = new Animation(881);

    /**
     * Represents the npc animation.
     */
    private static final Animation NPC_ANIM = new Animation(422);

    /**
     * Represents the animation specific to pickpocketing.
     */
    private static final Animation STUN_ANIMATION = new Animation(424);

    /**
     * Represents the sound to send when failed.
     */
    private static final Audio AUDIO = new Audio(2727, 1, 0);

    /**
     * Represents the pickpocket type.
     */
    private final Pickpocket type;

    /**
     * Represents the tickets to be rewarded.
     */
    private int ticks;

    /**
     * Constructs a new {@code PickpocketPulse} {@code Object}.
     *
     * @param player the player.
     * @param node   the node.
     * @param type   the type.
     */
    public PickpocketPulse(Player player, NPC node, final Pickpocket type) {
        super(player, node);
        this.type = type;
        this.resetAnimation = false;
    }

    @Override
    public boolean checkRequirements() {
        if (!interactable()) {
            return false;
        }
        if (player.getSkills().getLevel(Skills.THIEVING) < type.getLevel()) {
            player.getActionSender().sendMessage("You need to be a level " + type.getLevel() + " thief in order to pick this pocket.");
            return false;
        }
        player.faceTemporary(node, 2);
        node.getWalkingQueue().reset();
        node.getLocks().lockMovement(2);
        return true;
    }

    @Override
    public void animate() {
    }

    /**
     * Multiplies the player's loot
     *
     * @return the multiplication
     */
    public int multiply() {
        SecureRandom secureRandom = new SecureRandom();
        int chance = secureRandom.nextInt(35);
        switch (chance) {
            case 0:
                if (player.getSkills().getLevel(Skills.THIEVING) > (type.getLevel() + 10)
                    && player.getSkills().getLevel(Skills.AGILITY) > type.getLevel()) {
                    player.getActionSender().sendMessage("Your lightning-fast reactions allow you to steal double the loot.");
                    player.animate(ANIMATION);
                }
                return 2;
            case 5:
                if (player.getSkills().getLevel(Skills.THIEVING) > (type.getLevel() + 20)
                    && player.getSkills().getLevel(Skills.AGILITY) > (type.getLevel() + 10)) {
                    player.getActionSender().sendMessage("Your lightning-fast reactions allow you to steal triple the loot.");
                    player.animate(ANIMATION);
                }
                return 3;
            case 10:
                if (player.getSkills().getLevel(Skills.THIEVING) > (type.getLevel() + 30)
                    && player.getSkills().getLevel(Skills.AGILITY) > (type.getLevel() + 20)) {
                    player.getActionSender().sendMessage("Your lightning-fast reactions allow you to steal quadruple the loot.");
                    player.animate(ANIMATION);
                }
                return 4;
            default:
                return 1;
        }
    }

    /**
     * The martin rewards.
     */
    private static final ChanceItem[] MARTIN_REWARDS = new ChanceItem[]{
        new ChanceItem(5318, 1, 4, DropFrequency.COMMON),
        new ChanceItem(5319, 1, 3, DropFrequency.COMMON), new ChanceItem(5324, 1, 3, DropFrequency.COMMON),
        new ChanceItem(5322, 1, 2, DropFrequency.COMMON), new ChanceItem(5320, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(5323, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(5321, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(5096, 1, 1, DropFrequency.COMMON), new ChanceItem(5097, 1, 1, DropFrequency.COMMON),
        new ChanceItem(5098, 1, 1, DropFrequency.COMMON), new ChanceItem(5099, 1, 2, DropFrequency.COMMON),
        new ChanceItem(5100, 1, 1, DropFrequency.COMMON), new ChanceItem(5305, 1, 4, DropFrequency.COMMON),
        new ChanceItem(5307, 1, 3, DropFrequency.COMMON), new ChanceItem(5308, 1, 2, DropFrequency.COMMON),
        new ChanceItem(5306, 1, 3, DropFrequency.COMMON), new ChanceItem(5319, 1, 3, DropFrequency.COMMON),
        new ChanceItem(5309, 1, 2, DropFrequency.COMMON), new ChanceItem(5310, 1, 1, DropFrequency.COMMON),
        new ChanceItem(5311, 1, 1, DropFrequency.COMMON), new ChanceItem(5101, 1, 1, DropFrequency.COMMON),
        new ChanceItem(5102, 1, 1, DropFrequency.COMMON), new ChanceItem(5103, 1, 1, DropFrequency.COMMON),
        new ChanceItem(5104, 1, 2, DropFrequency.COMMON), new ChanceItem(5105, 1, 1, DropFrequency.COMMON),
        new ChanceItem(5106, 1, 1, DropFrequency.COMMON), new ChanceItem(5291, 1, 1, DropFrequency.COMMON),
        new ChanceItem(5292, 1, 1, DropFrequency.COMMON), new ChanceItem(5293, 1, 1, DropFrequency.COMMON),
        new ChanceItem(5294, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(5295, 1, 1, DropFrequency.RARE),
        new ChanceItem(5296, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(5297, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(5298, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(5299, 1, 1, DropFrequency.RARE),
        new ChanceItem(5300, 1, 1, DropFrequency.VERY_RARE), new ChanceItem(5301, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(5302, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(5303, 1, 1, DropFrequency.RARE),
        new ChanceItem(5304, 1, 1, DropFrequency.VERY_RARE), new ChanceItem(5282, 1, 1, DropFrequency.UNCOMMON),
        new ChanceItem(5280, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(5281, 1, 1, DropFrequency.UNCOMMON)
    };

    /**
     * Gets a random loot.
     *
     * @return {@code Item} loot.
     */
    public Item getRandomLoot(Player player) {
        if (type == Pickpocket.MARTIN_THE_MASTER_GARDENER) {
            return RandomUtil.getChanceItem(MARTIN_REWARDS).getRandomItem();
        }
        if ((type == Pickpocket.FEMALE_HAM_MEMBER || type == Pickpocket.MALE_HAM_MEMBER) &&
            RandomUtil.random(250) <= 5 && !player.getTreasureTrailManager().hasClue()) {
            return ClueScrollPlugin.getClue(ClueLevel.EASY);
        }
        int[] loot = type.getLoot()[RandomUtil.random(type.getLoot().length)];
        if (loot.length == 1) {
            loot = new int[]{ loot[0], 1 };
        }
        Item item = new Item(loot[0]);
        int count = loot[1] * (item.getDefinition().isStackable() ? multiply() : 1);
        if (item.getId() == Item.COINS) {
            return new Item(loot[0], count * Constants.COIN_DROP_MULTIPLIER);
        }
        return new Item(loot[0], count);
    }

    @Override
    public boolean reward() {
        if (ticks == 0) {
            player.animate(ANIMATION);
        }
        if (++ticks % 3 != 0) {
            return false;
        }
        final boolean success = success();
        if (success) {
            player.getSkills().addExperience(Skills.THIEVING, type.getExperience());
            player.getActionSender().sendMessage(type.getRewardMessage().replace("@name", node.getName().toLowerCase()));
            player.getInventory().add(getRandomLoot(player), player);
            if (node.getName().equalsIgnoreCase("man")) {
                AchievementDiary.decrease(player, AchievementTask.PICKPOCKET_MEN_10, 1);
            }
            if (node.getName().equals("Man") || node.getName().equals("Woman") && node.getLocation().getRegionId() == 12850) {
                AchievementDiary.finalize(player, AchievementTask.PICKPOCKET_MAN_WOMAN);
            }
            if (node.getId() == 3299) {
                AchievementDiary.finalize(player, AchievementTask.PICKPOCKET_MARTIN);
            }
        } else {
            if (type == Pickpocket.FEMALE_HAM_MEMBER || type == Pickpocket.MALE_HAM_MEMBER) {
                node.animate(NPC_ANIM);
                node.faceTemporary(player, 1);
                boolean bundledOut = RandomUtil.getRandom(10) == 1;
                if (bundledOut) {
                    node.sendChat("Stop! " + TextUtils.formatDisplayName(player.getName()) + " is a thief!");
                    player.getActionSender().sendMessage(node.getName() + ": Stop! " + TextUtils.formatDisplayName(player.getName()) + " is a thief!");
                } else {
                    node.sendChat("Keep thine hands to thineself " + TextUtils.formatDisplayName(player.getName()) + ".");
                    player.getActionSender().sendMessage(node.getName() + ": Keep thine hands to thineself " + TextUtils.formatDisplayName(player.getName()) + ".");
                }
                player.animate(STUN_ANIMATION);
                player.getActionSender().sendSound(new Audio(1842));
                player.getStateManager().set(EntityState.STUNNED, 4);
                player.getActionSender().sendSound(AUDIO);
                player.setAttribute("thief-delay", World.getTicks() + 4);
                player.getImpactHandler().manualHit(player, type.getStunDamage(), HitsplatType.NORMAL);
                if (bundledOut) {
                    player.playAnimation(Animation.create(837));
                    player.getActionSender().sendMessage("You're beaten unconscious and bundled out of the HAM Camp.");
                    player.lock(7);
                    World.submit(new Pulse(2) {

                        int count = 0;

                        @Override
                        public boolean pulse() {
                            this.setDelay(1);
                            switch (count++) {
                                case 0:
                                    player.getInterfaceState().openComponent(8677);
                                    break;
                                case 3:
                                    player.getProperties().setTeleportLocation(Location.create(3185, 9609, 0));
                                    player.playAnimation(Animation.RESET);
                                    break;
                                case 5:
                                    player.unlock();
                                    player.getInterfaceState().close();
                                    player.getInterfaceState().openDefaultTabs();
                                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                                    player.getDialogueInterpreter().sendPlaneMessage("You slowly wake up in an unknown place.");
                                    return true;
                            }
                            return false;
                        }
                    });
                }
            } else {
                node.animate(NPC_ANIM);
                node.faceTemporary(player, 1);
                node.sendChat(type.getForceMessage());
                player.animate(STUN_ANIMATION);
                player.getActionSender().sendSound(new Audio(1842));
                player.getStateManager().set(EntityState.STUNNED, 4);
                player.getActionSender().sendSound(AUDIO);
                player.setAttribute("thief-delay", World.getTicks() + 4);
                player.getImpactHandler().manualHit(player, type.getStunDamage(), HitsplatType.NORMAL);
                player.getActionSender().sendMessage(type.getFailMessage().replace("@name", node.getName().toLowerCase()));
            }
        }
        return true;
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void message(int type) {
        switch (type) {
            case 0:
                player.getActionSender().sendMessage(this.type.getStartMessage().replace("@name", node.getName().toLowerCase()));
                break;
        }
    }

    /**
     * Checks if the npc is interable.
     *
     * @return <code>True</code> if so.
     */
    private boolean interactable() {
        if (DeathTask.isDead(((Entity) node)) || ((NPC) node).isHidden(player) || !node.isActive() || player.getAttribute("thief-delay", 0) > World.getTicks()) {
            return false;
        } else if (player.inCombat()) {
            player.getActionSender().sendMessage("You can't pickpocket during combat.");
            return false;
        } else if (player.getInventory().freeSlots() == 0) {
            player.getActionSender().sendMessage("You don't have enough inventory space.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the pickpocket is a success.
     *
     * @return <code>True</code> if so.
     */
    private boolean success() {
        double level = player.getSkills().getLevel(Skills.THIEVING);
        double req = type.getLevel();
        double successChance = Math.ceil((level * 50 - req * 15) / req / 3 * 4);
        if (Perk.SLEIGHT_OF_HAND.enabled(player)) {
            successChance += RandomUtil.random(30, 45);
            if (RandomUtil.random(5) == 1) {
                return true;
            }
        }
        successChance += player.getDonorManager().getDonorStatus().getThievingSuccessModifier();
        int roll = RandomUtil.random(99);
        return successChance >= roll;
    }

}
