package org.gielinor.game.content.skill.free.gather;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.content.donators.DonatorConfigurations;
import org.gielinor.game.content.global.EquipmentSet;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.firemaking.Log;
import org.gielinor.game.content.skill.member.farming.wrapper.PatchWrapper;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.misc.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.interaction.item.MaxCapePlugin;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * Handles a gathering skill, such as woodcutting, mining, ...
 *
 * @author Emperor
 */
public final class GatheringSkillPulse extends SkillPulse<GameObject> {

    private static final Logger log = LoggerFactory.getLogger(GatheringSkillPulse.class);

    /**
     * If the player is mining.
     */
    private boolean mining;

    /**
     * The amount of ticks it takes to get a log.
     */
    private int ticks;

    /**
     * Constructs a new {@code GatheringSkillPulse} {@code Object}.
     *
     * @param player The player.
     * @param node   The gathering resource.
     */
    public GatheringSkillPulse(Player player, GameObject node) {
        super(player, node);
    }

    @Override
    public void start() {
        resource = SkillingResource.forId(node.getId());
        if (resource == null) {
            if (node.getName().toLowerCase().contains("rocks")) {
                player.getActionSender().sendMessage("This rock contains no ore.");
                return;
            }
            log.warn("[{}] attempted to mine a potentially unhandled resource: [{}] with ID: [{}].",
                player.getName(), node.getName(), node.getId());
            player.getActionSender().sendMessage("Oh no! That resource is undefined. We've logged this incident, but feel");
            player.getActionSender().sendMessage("free to submit a report on our forums. Please include the resource ID: " + node.getId() + ".");
            return;
        }
        if (SkillingResource.isEmpty(node.getId()) && resource.getSkillId() == Skills.MINING) {
            player.getActionSender().sendMessage("This rock contains no ore.");
            return;
        }
        mining = resource.getSkillId() == Skills.MINING;
        super.start();
    }

    @Override
    public boolean checkRequirements() {
        if (mining) {
            if (!ObjectDefinition.forId(resource.getId()).hasAction("Prospect")) {
                log.warn("[{}] attempted to mine a potentially invalid rock: [{}] with ID: [{}].",
                    node.getName(), node.getId());
                player.getActionSender().sendMessage("Oh no! That rock seems to be invalid. This incident has been logged.");
                return false;
            }
        }
        if (player.getSkills().getLevel(resource.getSkillId()) < resource.getLevel()) {
            player.getActionSender().sendMessage("You do not have the required level to " + (mining ? "mine this rock." : "cut this tree."));
            return false;
        }
        if (player.getDonorManager().getDonorStatus().getToolsRequired()) {
            if (setTool() == null) {
                player.getActionSender().sendMessage("You need a" + (mining ? " pickaxe" : "n axe") + " to " + (mining ? "mine this rock." : "cut this tree."));
                player.getActionSender().sendMessage("You do not have a" + (mining ? " pickaxe" : "n axe") + " which you have the " + (mining ? "mining" : "woodcutting") + " level to use.");
                return false;
            }
        } else {
            tool = mining ? SkillingTool.DRAGON_PICKAXE : SkillingTool.DRAGON_AXE;
        }
        if (player.getInventory().freeSlots() < 1) {
            player.getDialogueInterpreter().sendPlaneMessage("Your inventory is too full to hold any more " + (mining ? "ore" : "logs") + ".");
            return false;
        }
        return true;
    }

    @Override
    public void animate() {
        if (resource.getAnimation() != null) {
            player.animate(resource.getAnimation());
        } else if (tool.getAnimation() != null) {
            if (!tool.equals(SkillingTool.INFERNO_ADZE)) {
                player.animate(tool.getAnimation());
            } else {
                player.animate(mining ? new Animation(10222) : tool.getAnimation());
            }
        }
    }

    @Override
    public boolean reward() {
        if (++ticks % (resource == SkillingResource.RUNE_ESSENCE ? 1 : 3) != 0) {
            return false;
        }
        if (!checkReward()) {
            return false;
        }
        boolean additional = false;
        if (resource.getReward() > 0) {
            int reward = resource.getReward();
            if (mining) {
                if (!resource.getName().contains("RUNITE") && player.getPerkManager().isTriggered(Perk.ROCK_SMASHER)) {
                    additional = true;
                }
                if (!resource.getName().contains("RUNITE") && MaxCapePlugin.isWearing(player) && RandomUtil.random(4) == 2) {
                    additional = true;
                }
            }
            if (resource.name().contains("GEM_ROCK")) {
                reward = generateGemStone();
            }
            if (resource == SkillingResource.RUNE_ESSENCE && player.getSkills().getLevel(Skills.MINING) > 29) {
                reward = 7936;
            }
            if (reward != 3239 || RandomUtil.random(100) < 10) { //Hollow tree (bark)
                if (resource == SkillingResource.SANDSTONE || resource == SkillingResource.GRANITE) {
                    int value = RandomUtil.randomize(resource == SkillingResource.GRANITE ? 3 : 4);
                    reward += value << 1;
                    player.getSkills().addExperience(resource.getSkillId(), value * 10);
                }
                if (checkInfernoAdze(reward)) {
                    player.getInventory().add(new Item(reward, 1));
                    if (additional) {
                        player.getInventory().add(new Item(reward, 1), true);
                        player.getActionSender().sendMessage("You receive double the ores!", 1);
                    }
                    int finalReward = reward;
                    DonatorConfigurations.handleDoubleResources(player, () -> {
                        player.getInventory().add(new Item(finalReward, 1));
                        return null;
                    });
                }
                if (resource.name().contains("IRON") && player.getLocation().getRegionId() == 13107) {
                    AchievementDiary.finalize(player, AchievementTask.MINE_IRON_ORE);
                }
                if (resource.name().contains("WILLOW") && player.getLocation().getRegionId() == 12338) {
                    AchievementDiary.finalize(player, AchievementTask.WILLOW_LOGS_DRAYNOR);
                }
                if (resource.name().contains("GEM_ROCK")) {
                    player.getActionSender().sendMessage("You get an " + ItemDefinition.forId(reward).getName().toLowerCase() + ".", 1);
                } else {
                    if (mining) {
                        player.getActionSender().sendMessage("You mine some " + ItemDefinition.forId(reward).getName().toLowerCase() + ".", 1);
                    } else {
                        birdsNestDrop();
                        player.getActionSender().sendMessage("You get some " + ItemDefinition.forId(reward).getName().toLowerCase() + ".", 1);
                        if (resource.name().contains("OAK") &&
                            (player.getAchievementRepository().getAchievements().get(AchievementTask.CHOP_BURN_OAK_LOGS) == null ||
                                player.getAchievementRepository().getAchievements().get(AchievementTask.CHOP_BURN_OAK_LOGS) == 0)) {
                            AchievementDiary.decrease(player, AchievementTask.CHOP_BURN_OAK_LOGS, 1);
                        }
                        if (reward == 3239) {
                            player.getSkills().addExperience(resource.getSkillId(), 275.2); //Add extra experience for bark.
                        }
                    }
                }
            }
        }
        double experience = resource.getExperience();
        double bonusExperience = 0;
        if (EquipmentSet.LUMBERJACK.isUsing(player)) {
            bonusExperience = (experience * 2.5) / 100;
        } else {
            double lumberjackModifier = getLumberjackModifier();
            if (lumberjackModifier > 0) {
                bonusExperience = (experience * lumberjackModifier) / 100;
            }
        }
        if (mining && additional) {
            bonusExperience += resource.getExperience();
        }
        player.getSkills().addExperience(resource.getSkillId(), experience + bonusExperience);
        if (resource.getRespawnRate() != 0) {
            // Give extra since it's a private server
            int rewardAmount = resource.getRewardAmount();
            int charge = 1000 / rewardAmount;
            node.setCharge(node.getCharge() - RandomUtil.random(charge, charge << 2));
            if (node.getCharge() < 1) {
                node.setCharge(1000);
                if (resource.isFarming()) {
                    PatchWrapper tree = player.getFarmingManager().getPatchWrapper(node.getWrapper().getId());
                    tree.addConfigValue(tree.getNode().getStumpBase());
                    tree.getCycle().setGrowthTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(resource.getRespawnDuration() + 10));
                    return true;
                }
                if (resource.getEmptyId() > -1) {
                    ObjectBuilder.replace(node, node.transform(resource.getEmptyId()), resource.getRespawnDuration());
                } else {
                    ObjectBuilder.replace(node, node.transform(0), resource.getRespawnDuration());
                }
                node.setActive(false);
                return true;
            }
        }
        return false;
    }

    private int generateGemStone() {
        int roll = RandomUtil.random(128);
        if (roll < 4) return Item.UNCUT_DIAMOND;
        else if (roll < 9) return Item.UNCUT_RUBY;
        else if (roll < 14) return Item.UNCUT_EMERALD;
        else if (roll < 23) return Item.UNCUT_SAPPHIRE;
        else if (roll < 38) return Item.UNCUT_RED_TOPAZ;
        else if (roll < 68) return Item.UNCUT_JADE;
            // Members may find uncut onyx and dragonstone.
            // For non-members this is considered an opal.
            // Effectively, members have slightly lower chance at opals,
            // which is considered the worst gemstone.
        else if (roll < 69 && player.getDonorManager().hasMembership()) {
            roll = RandomUtil.random(8);
            if (roll < 1) { // 1:128 then 1:8 -> 1:1024.
                log.info("[{}] mined an uncut onyx from gem rocks.", player.getName());
                return Item.UNCUT_ONYX;
            } else if (roll < 3) return Item.UNCUT_DRAGONSTONE;
        }
        return Item.UNCUT_OPAL;
    }

    private boolean checkInfernoAdze(int logId) {
        if (tool == SkillingTool.INFERNO_ADZE) {
            if (!mining) {
                int randomInt = RandomUtil.random(0, 3);
                if (randomInt == 1) {
                    Log log = Log.forId(logId);
                    if (log != null) {
                        player.getSkills().addExperience(Skills.FIREMAKING, log.getXp());
                    }
                    player.getActionSender().sendMessage("Your Inferno adze burns the logs, giving you the experience.", 1);
                    return false;
                }
            }
        }
        return true;
    }

    private void birdsNestDrop() {
        SecureRandom random = new SecureRandom();
        if (random.nextInt((MaxCapePlugin.isWearing(player) || MaxCapePlugin.containsCape(player)) ? 180 : 200) == 1) {
            Location location = player.getLocation();
            GroundItem groundItem = GroundItemManager.create(new Item(5070 + random.nextInt(4)), location, player);
            groundItem.setDecayTime(50);
            player.getActionSender().sendMessage("A bird's nest falls out of the tree, and lands by your feet.");
        }
    }

    /**
     * Checks if the player gets rewarded.
     *
     * @return <code>True</code> if so.
     */
    private boolean checkReward() {
        int skill = mining ? Skills.MINING : Skills.WOODCUTTING;
        int level = 40 + player.getSkills().getLevel(skill) + player.getFamiliarManager().getBoost(skill); // TODO Lower maybe?
        double hostRatio = Math.random() * (100.0 * resource.getRate());
        double clientRatio = Math.random() * ((level - resource.getLevel()) * (1.0 +
            (tool == SkillingTool.INFERNO_ADZE ?
                mining ? 0.65D : tool.getRatio()
                : tool.getRatio())));
        return hostRatio < clientRatio;
    }

    @Override
    public void message(int type) {
        switch (type) {
            case 0:
                player.getActionSender().sendMessage("You swing your " + (mining ? "pickaxe at the rock..." : "axe at the tree..."), 1);
                break;
        }
    }

    /**
     * Sets the tool used.
     */
    private SkillingTool setTool() {
        tool = mining ? SkillingTool.getPickaxe(player) : SkillingTool.getHatchet(player);
        return tool;
    }

    /**
     * Gets the modifier for lumberjack pieces.
     *
     * @return The modifier.
     */
    public double getLumberjackModifier() {
        double bonusModifier = 0;
        if (player.getEquipment().contains(EquipmentSet.LUMBERJACK.getSet()[0])) { // Hat
            bonusModifier += 0.4;
        }
        if (player.getEquipment().contains(EquipmentSet.LUMBERJACK.getSet()[1])) { // Top
            bonusModifier += 0.8;
        }
        if (player.getEquipment().contains(EquipmentSet.LUMBERJACK.getSet()[2])) { // Legs
            bonusModifier += 0.6;
        }
        if (player.getEquipment().contains(EquipmentSet.LUMBERJACK.getSet()[3])) { // Boots
            bonusModifier += 0.2;
        }
        return bonusModifier;
    }
}
