package org.gielinor.game.content.skill.free.fishing;

import org.gielinor.content.donators.DonatorConfigurations;
import org.gielinor.game.content.global.ClueBottle;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.summoning.familiar.Forager;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.path.Pathfinder;
import org.gielinor.game.world.update.flag.context.Animation;

import java.util.Arrays;
import java.util.List;

/**
 * Handles a fishing pulse.
 *
 * @author 'Vexia
 */
public final class FishingPulse extends SkillPulse<NPC> {

    /**
     * Represents the fishing option.
     */
    private final FishingOption option;

    /**
     * Represents the fish type.
     */
    private Fish fish;

    /**
     * Represents the base location the npc was at.
     */
    private final Location location;

    /**
     * Constructs a new {@code FishingPulse} {@code Object}.
     *
     * @param player the player.
     * @param npc    the fishing spot NPC.
     * @param option The fishing option.
     */
    public FishingPulse(final Player player, final NPC npc, final FishingOption option) {
        super(player, npc);
        this.option = option;
        this.location = npc.getLocation();
        if (option != null) {
            this.fish = option.getRandomFish(player);
        }
    }

    @Override
    public void start() {
        if (player.getFamiliarManager().hasFamiliar() && player.getFamiliarManager().getFamiliar() instanceof Forager) {
            final Forager forager = (Forager) player.getFamiliarManager().getFamiliar();
            Location dest = player.getLocation().transform(player.getDirection());
            Pathfinder.find(forager.getLocation(), dest).walk(forager);
        }
        super.start();
    }

    @Override
    public boolean checkRequirements() {
        if (option == null) {
            return false;
        }
        if (player.getDonorManager().getDonorStatus().getToolsRequired()) {
            if ((option.getTool().getId() == 311 && !(player.getInventory().contains(10129) || player.getEquipment().contains(10129) || player.getInventory().contains(311)))) {
                player.getDialogueInterpreter().sendPlaneMessage("You need a " + option.getTool().getName().toLowerCase() + " to catch these fish.");
                stop();
            } else if (!player.getInventory().containsItem(option.getTool())) {
                player.getDialogueInterpreter().sendPlaneMessage("You need a " + option.getTool().getName().toLowerCase() + " to catch these fish.");
                stop();
                return false;
            }
        }
        if (option.getBait() != null && !player.getInventory().containsItem(option.getBait())) {
            player.getDialogueInterpreter().sendPlaneMessage("You don't have any " + option.getBait().getName().toLowerCase() + "s left.");
            stop();
            return false;
        }
        if (player.getSkills().getLevel(Skills.FISHING) < fish.getLevel()) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a fishing level of " + fish.getLevel() + " to catch " + (fish == Fish.SHRIMP || fish == Fish.ANCHOVIE ? "" : "a") + " " + fish.getItem().getName().toLowerCase() + ".".trim());
            stop();
            return false;
        }
        if (player.getInventory().freeSlots() == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("You don't have enough space in your inventory.");
            stop();
            return false;
        }
        if (location != node.getLocation() || !node.isActive()) {/**checks if the npc moved.*/
            stop();
            return false;
        }
        return true;
    }

    @Override
    public void animate() {
        if (option.getTool().getId() == 311 && (player.getInventory().contains(10129) || player.getEquipment().contains(10129))) {
            player.animate(Animation.create(5108)); // Barb-tail harpoon
            return;
        }
        player.animate(option.getAnimation());
    }

    @Override
    public boolean reward() {
        if (getDelay() == 1) {
            super.setDelay(5);
            return false;
        }
        if (player.getFamiliarManager().hasFamiliar() && player.getFamiliarManager().getFamiliar() instanceof Forager) {
            final Forager forager = (Forager) player.getFamiliarManager().getFamiliar();
            forager.handlePassiveAction();
        }
        if (success()) {

            int bonusFish = getBonusFish() ? 1 : 0;
            Item fishItem = fish.getItem().copy();
            fishItem.increase(bonusFish);

            if (!(player.getInventory().add(fishItem, player) && option.getBait() != null) || player.getInventory().remove(option.getBait())) {
                DonatorConfigurations.handleDoubleResources(player, () -> {
                    player.getInventory().add(fishItem, player);
                    return null;
                });
                player.getSkills().addExperience(Skills.FISHING, fish.getExperience());
                if (bonusFish > 0) {
                    message(3);
                } else {
                    message(2);
                }
                fish = option.getRandomFish(player);
            }

            ClueBottle.receiveClueBottle(fish, player.getSkills().getStaticLevel(Skills.FISHING)).ifPresent(
                clueBottle -> {
                    player.getActionSender().sendMessage("You find a shiny bottle in the water...");
                    if(player.getInventory().freeSlots() > 0)
                        player.getInventory().add(clueBottle.getClueBottleID());
                    else {
                        player.getActionSender().sendMessage("...due to a full inventory the bottle falls on the ground!");
                        GroundItemManager.create(new Item(clueBottle.getClueBottleID(), 1), player);
                    }
                }
            );

        }
        return player.getInventory().freeSlots() == 0;
    }

    @Override
    public void message(int type) {
        switch (type) {
            case 0:
                player.getActionSender().sendMessage(option.getStartMessage());
                break;
            case 2:
                player.getActionSender().sendMessage(fish == Fish.ANCHOVIE || fish == Fish.SHRIMP ? "You catch some " + fish.getItem().getName().toLowerCase().replace("raw", "").trim() + "." : "You catch a " + fish.getItem().getName().toLowerCase().replace("raw", "").trim() + ".", 1);
                if (fish == Fish.SHRIMP) {
                    AchievementDiary.decrease(player, AchievementTask.SHRIMP_100, 1);
                }
                if (fish == Fish.ANCHOVIE && player.getLocation().getRegionId() == 13105) {
                    AchievementDiary.finalize(player, AchievementTask.CATCH_ANCHOVIES);
                }
                if (fish == Fish.SALMON && player.getLocation().getRegionId() == 12850 || player.getLocation().getRegionId() == 12851) {
                    AchievementDiary.finalize(player, AchievementTask.CATCH_SALMON);
                }
                break;
            case 3:
                message(2);
                player.getActionSender().sendMessage("<col=FF0000>You manage to catch an extra fish.</col>");
                break;
        }
    }

    /**
     * Method used to check if the catch was a success.
     *
     * @return <code>True</code> if so.
     */
    private boolean success() {
        if (getDelay() == 1) {
            return false;
        }
        int level = 1 + player.getSkills().getLevel(Skills.FISHING) + player.getFamiliarManager().getBoost(Skills.FISHING);
        double hostRatio = Math.random() * fish.getLevel();
        double clientRatio = Math.random() * ((level * 1.25 - fish.getLevel()));

        return hostRatio < clientRatio;
    }

    /**
     * @return Whether the best possible perk available is triggered; (PRO > AVID > KEEN)
     */
    private boolean getBonusFish() {
        List<Perk> fishingPerks = Arrays.asList(Perk.PRO_FISHERMAN, Perk.AVID_FISHERMAN, Perk.KEEN_FISHERMAN);

        return player.getPerkManager().isTriggered(fishingPerks.stream()
            .filter(perk -> perk.enabled(player))
            .findFirst()
            .orElse(null));
    }

}
