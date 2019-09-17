package plugin.skill.crafting;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.crafting.jewellery.JewelleryCrafting;
import org.gielinor.game.content.skill.member.slayer.Equipment;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to craft jewellery.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class JewelleryCraftPlugin extends UseWithHandler {

    /**
     * Represents the ids to use for this plugin.
     */
    private static final int[] IDS = new int[]{ 2966, 3044, 3294, 3994, 4304, 6189, 11009, 11010, 11666, 12100, 12806, 12807, 12808, 12809, 14921, 18497, 18525, 18526, 21879, 22721, 26814, 28433, 28434, 30018, 30019, 30020, 30021, 30508, 30509, 30510, 36956, 37651, };

    /**
     * Constructs a new {@code JewelleryCraftPlugin} {@code Object}.
     */
    public JewelleryCraftPlugin() {
        super(2357);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int i : IDS) {
            addHandler(i, OBJECT_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        if (event.getPlayer().getInventory().contains(Equipment.ENCHANTED_GEM.getItem().getId())) {
            final Player player = event.getPlayer();
            SkillDialogueHandler skillDialogueHandler = new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.ONE_OPTION, new Item(11849)) {

                @Override
                public void create(final int amount, int index) {
                    player.getPulseManager().run(new SlayerRingPulse(player, event.getUsedItem(), amount));
                }

                @Override
                public int getAll(int index) {
                    return player.getInventory().getCount(event.getUsedItem());
                }

            };
            skillDialogueHandler.open();
            return true;
        }
        JewelleryCrafting.open(event.getPlayer());
        return true;
    }

    /**
     * Represents the {@link org.gielinor.game.content.skill.SkillPulse} for offering bones on an altar.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static class SlayerRingPulse extends SkillPulse<Item> {

        /**
         * Represents the animation to use.
         */
        private static final Animation ANIMATION = new Animation(3243);
        /**
         * Represents the amount to make.
         */
        private int amount;
        /**
         * Represents the ticks passed.
         */
        private int ticks;

        /**
         * Constructs a new {@code SlayerRingPulse} {@Code Object}
         *
         * @param player The player.
         * @param item   The bone item.
         * @param amount The amount to offer.
         */
        public SlayerRingPulse(Player player, Item item, int amount) {
            super(player, item);
            this.amount = amount;
            this.resetAnimation = false;
        }

        @Override
        public boolean checkRequirements() {
            if (player.getSkills().getLevel(Skills.CRAFTING) < 75) {
                player.getDialogueInterpreter().sendPlaneMessage(false, "You need a Crafting level of at least 75 to do this.");
                return false;
            }
            if (!player.getSavedData().getActivityData().hasLearnedSlayerOption(1)) {
                player.getDialogueInterpreter().sendPlaneMessage(false, "You're not sure how to do that yet!");
                return false;
            }
            return player.getInventory().contains(Equipment.ENCHANTED_GEM.getItem().getId()) && player.getInventory().contains(2357) && player.getInventory().contains(1592);
        }

        @Override
        public void animate() {
            if (ticks % 5 == 0) {
                player.animate(ANIMATION);
            }
        }

        @Override
        public boolean reward() {
            if (++ticks % 5 != 0) {
                return false;
            }
            if (player.getInventory().remove(new Item(Equipment.ENCHANTED_GEM.getItem().getId()), new Item(2357))) {
                player.getInventory().add(new Item(11849));
                player.getSkills().addExperience(Skills.CRAFTING, 15);
            }
            amount--;
            return amount < 1;
        }
    }

}
