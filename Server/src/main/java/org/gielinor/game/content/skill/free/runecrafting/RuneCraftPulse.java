package org.gielinor.game.content.skill.free.runecrafting;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the rune crafting pulse(main method).
 *
 * @author 'Vexia
 * @date 02/11/2013
 */
public final class RuneCraftPulse extends SkillPulse<Item> {

    /**
     * Represent the rune essence item.
     */
    private static final Item RUNE_ESSENCE = new Item(1436);

    /**
     * Represents the pure essence item.
     */
    private static final Item PURE_ESSENCE = new Item(7936);

    /**
     * Represents the binding necklace item.
     */
    private static final Item BINDING_NECKLACE = new Item(5521);

    /**
     * Represents the animation used for this pulse.
     */
    private static final Animation ANIMATION = new Animation(791);

    /**
     * Represents the graphics used for this pulse.
     */
    private static final Graphics GRAPHICS = new Graphics(186, 100);

    /**
     * Represents the altar.
     */
    private final Altar altar;

    /**
     * Represents the rune we're crafting.
     */
    private final Rune rune;

    /**
     * Represents the combination rune(if any)
     */
    private final CombinationRune combo;

    /**
     * Represents if it's a combination pulse.
     */
    private final boolean combination;

    /**
     * Represents the talisman to remove.
     */
    private Talisman talisman;

    /**
     * Constructs a new {@code RuneCraftPulse} {@code Object}.
     *
     * @param player the player.
     * @param node   the node.
     * @param altar  the altar.
     */
    public RuneCraftPulse(Player player, Item node, final Altar altar, final boolean combination, final CombinationRune combo) {
        super(player, node);
        this.altar = altar;
        this.rune = altar.getRune();
        this.combination = combination;
        this.combo = combo;
    }

    @Override
    public boolean checkRequirements() {
        if (!altar.isOurania() && player.getSkills().getLevel(Skills.RUNECRAFTING) < rune.getLevel()) {
            player.getActionSender().sendMessage("You need a Runecrafting level of at least " + rune.getLevel() + " to craft this rune.");
            return false;
        }
        if (combination && !player.getInventory().containsItem(PURE_ESSENCE)) {
            player.getActionSender().sendMessage("You need pure essence to craft this rune.");
            return false;
        }
        if (!altar.isOurania() && !rune.isNormal() && !player.getInventory().containsItem(PURE_ESSENCE)) {
            player.getActionSender().sendMessage("You need pure essence to craft this rune.");
            return false;
        }
        if (!altar.isOurania() && rune.isNormal() && !player.getInventory().containsItem(PURE_ESSENCE) && !player.getInventory().containsItem(RUNE_ESSENCE)) {
            player.getActionSender().sendMessage("You need rune essence or pure essence in order to craft this rune.");
            return false;
        }
        if (altar.isOurania() && !player.getInventory().containsItem(PURE_ESSENCE)) {
            player.getActionSender().sendMessage("You need pure essence to craft this rune.");
            return false;
        }
        if (combination && player.getSkills().getLevel(Skills.RUNECRAFTING) < combo.getLevel()) {
            player.getActionSender().sendMessage("You need a Runecrafting level of at least " + combo.getLevel() + " to combine this rune.");
            return false;
        }
        if (node != null) {
            if (node.getName().contains("rune") && !player.getAttribute("spell:imbue", false)) {
                final Rune r = Rune.forItem(node);
                final Talisman t = Talisman.forName(r.name());
                if (!player.getInventory().containsItem(t.getTalisman())) {
                    player.getActionSender().sendMessage("You don't have the correct talisman to combine this rune.");
                    return false;
                }
                talisman = t;
            }
        }
        player.lock(4);
        return true;
    }

    @Override
    public void animate() {
        player.animate(ANIMATION);
        player.graphics(GRAPHICS);
    }

    @Override
    public boolean reward() {
        if (!combination) {
            craft();
        } else {
            combine();
        }
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        player.animate(ANIMATION);
    }

    @Override
    public void message(int type) {
        switch (type) {
            case 1:
                if (altar != Altar.OURANIA) {
                    if (altar == Altar.WATER) {
                        AchievementDiary.finalize(player, AchievementTask.CRAFT_WATER_RUNES);
                    }
                    player.getActionSender().sendMessage("You bind the temple's power into " + (combination ? combo.getRune().getName().toLowerCase() : rune.getRune().getName().toLowerCase()) + "s.", 1);
                } else {
                    player.getActionSender().sendMessage("You bind the temple's power into runes.", 1);
                }
                break;
        }
    }

    /**
     * Method used to craft runes.
     */
    private void craft() {

        final Item[] essences = getEssence();

        final Item item = new Item(essences[0].getId(), getEssenceAmount());
        int amount = player.getInventory().getCount(item) * getMultiplier();

        if (altar.isOurania()) {
            if (player.getInventory().remove(item, false)) {
                double sumExperience = 0;
                for (int i = 0; i < amount; i++) {
                    Rune rune = null;
                    while (rune == null) {
                        final Rune temp = Rune.values()[RandomUtil.random(Rune.values().length)];
                        if (player.getSkills().getLevel(Skills.RUNECRAFTING) >= temp.getLevel()) {
                            rune = temp;
                        } else {
                            if (RandomUtil.random(3) == 1) {
                                rune = temp;
                            }
                        }
                    }
                    sumExperience += rune.getExperience() * 2.0;
                    player.getInventory().add(rune.getRune(), false);
                }
                player.getSkills().addExperience(Skills.RUNECRAFTING, sumExperience);
                player.getInventory().update(true);
            }
        } else {
            if (player.getInventory().remove(item) && player.getInventory().add(new Item(rune.getRune().getId(), amount))) {
                player.getSkills().addExperience(Skills.RUNECRAFTING, rune.getExperience() * amount);
                if (amount >= 56 && rune == Rune.COSMIC) {
                    AchievementDiary.finalize(player, AchievementTask.CRAFT_56_COSMIC);
                }
            }
        }

        if(essences.length > 1)
            craft();

    }

    /**
     * Method used to combine runes.
     */
    private void combine() {
        final Item remove = node.getName().contains("talisman") ? node : talisman != null ?
            talisman.getTalisman() : Talisman.forName(Rune.forItem(node).name()).getTalisman();
        boolean imbued = player.getAttribute("spell:imbue", false);
        if (imbued || player.getInventory().remove(remove)) {
            int amount = 0;
            int essenceAmt = player.getInventory().getCount(PURE_ESSENCE);
            final Item rune = node.getName().contains("rune") ? Rune.forItem(node).getRune() : Rune.forName(Talisman.forItem(node).name()).getRune();
            int runeAmt = player.getInventory().getCount(rune);
            if (essenceAmt > runeAmt) {
                amount = runeAmt;
            } else {
                amount = essenceAmt;
            }
            if (player.getInventory().remove(new Item(PURE_ESSENCE.getId(), amount)) &&
                player.getInventory().remove(new Item(rune.getId(), amount))) {
                for (int i = 0; i < amount; i++) {
                    if (RandomUtil.random(1, 3) == 1 || hasBindingNeckalce()) {
                        player.getInventory().add(new Item(combo.getRune().getId(), 1));
                        player.getSkills().addExperience(Skills.RUNECRAFTING, combo.getExperience());
                    }
                }
                if (combo == CombinationRune.LAVA) {
                    AchievementDiary.finalize(player, AchievementTask.LAVA_RUNES);
                }
                if (hasBindingNeckalce()) {
                    player.getEquipment().get(Equipment.SLOT_AMULET).setCharge(player.getEquipment().get(Equipment.SLOT_AMULET).getCharge() - 1);
                    if (1000 - player.getEquipment().get(Equipment.SLOT_AMULET).getCharge() > 14) {
                        player.getEquipment().remove(BINDING_NECKLACE, true);
                        player.getActionSender().sendMessage("Your binding necklace crumbles into dust.", 1);
                    }
                }
            }
        }
    }

    /**
     * Gets the essence amount.
     *
     * @return the amount of essence.
     */
    private int getEssenceAmount() {
        if (altar.isOurania() && player.getInventory().containsItem(PURE_ESSENCE)) {
            return player.getInventory().getCount(PURE_ESSENCE);
        }
        if (!rune.isNormal() && player.getInventory().containsItem(PURE_ESSENCE)) {
            return player.getInventory().getCount(PURE_ESSENCE);
        } else if (rune.isNormal() && player.getInventory().containsItem(PURE_ESSENCE)) {
            return player.getInventory().getCount(PURE_ESSENCE);
        } else {
            return player.getInventory().getCount(RUNE_ESSENCE);
        }
    }

    /**
     * Gets the rune essence that needs to be defined.
     *
     * @return the item.
     */
    private Item[] getEssence() {
        if (altar.isOurania() && player.getInventory().containsItem(PURE_ESSENCE)) {
            return new Item[]{PURE_ESSENCE};
        }
        if (!rune.isNormal() && player.getInventory().containsItem(PURE_ESSENCE)) {
            return new Item[]{PURE_ESSENCE};
        } else if (rune.isNormal() && player.getInventory().containsItem(PURE_ESSENCE)) {

            if(player.getInventory().containsItem(RUNE_ESSENCE))
                return new Item[]{RUNE_ESSENCE, PURE_ESSENCE};
            else
                return new Item[]{PURE_ESSENCE};

        } else {
            return new Item[]{RUNE_ESSENCE};
        }
    }

    /**
     * Gets the multiplied amount of runes to make.
     *
     * @return the amount.
     */
    public int getMultiplier() {
        if (altar.isOurania()) {
            return 1;
        }
        int i = 0;
        for (int level : rune.getMultiple()) {
            if (player.getSkills().getLevel(Skills.RUNECRAFTING) >= level) {
                i++;
            }
        }
        return i != 0 ? i : 1;
    }

    /**
     * Method used to check if the player has a binding necklace.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasBindingNeckalce() {
        return player.getEquipment().containsItem(BINDING_NECKLACE);
    }

    /**
     * Gets the altar.
     *
     * @return The altar.
     */
    public Altar getAltar() {
        return altar;
    }

}
