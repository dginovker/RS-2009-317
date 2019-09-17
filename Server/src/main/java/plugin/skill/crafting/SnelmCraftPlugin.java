package plugin.skill.crafting;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the crafting of a snelm helmet.
 *
 * @author Vexia
 */
public class SnelmCraftPlugin extends UseWithHandler {

    /**
     * The snelm data.
     */
    private static final int[][] DATA = new int[][]{
        { 3345, 3327 }, { 3355, 3337 },//blamish
        { 3349, 3341 }, { 3341, 3359 },//ochre
        { 3347, 3329 }, { 3357, 3339 },//blood
        { 3351, 3333 }, { 3361, 3343 },//blue
        { 3353, 3335 },//bark
    };

    /**
     * Constructs a new {@Code SnelmCraftPlugin} {@Code Object}
     */
    public SnelmCraftPlugin() {
        super(1755);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int[] aDATA : DATA) {
            for (int anADATA : aDATA) {
                addHandler(aDATA[0], ITEM_TYPE, this);
            }
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        int[] snelm = null;
        for (int[] aDATA : DATA) {
            for (int k = 0; k < aDATA.length; k++) {
                if (aDATA[0] == event.getUsedItem().getId()) {
                    snelm = aDATA;
                    break;
                }
            }
        }
        if (snelm == null) {
            return false;
        }
        player.lock(1);
        player.getPulseManager().run(new SnelmCraftPulse(player, event.getUsedItem(), snelm));
        return true;
    }

    /**
     * Handles the crafting of a snelm helmet.
     *
     * @author Vexia
     */
    public static final class SnelmCraftPulse extends SkillPulse {

        /**
         * The snelm data.
         */
        private final int[] data;

        /**
         * Constructs a new {@Code SnelmCraftPulse} {@Code Object}
         *
         * @param player the player.
         * @param node   the node.
         */
        @SuppressWarnings("unchecked")
        public SnelmCraftPulse(Player player, Item node, int[] data) {
            super(player, node);
            this.setDelay(1);
            this.data = data;
        }

        @Override
        public boolean checkRequirements() {
            if (player.getSkills().getStaticLevel(Skills.CRAFTING) < 15) {
                player.getActionSender().sendMessage("You need a Crafting level of at least 15 in order to do this.");
                return false;
            }
            return true;
        }

        @Override
        public void animate() {

        }

        @Override
        public boolean reward() {
            player.getActionSender().sendMessage("You craft the shell into a helmet.", 1);
            player.getInventory().replace(new Item(data[1]), node.getIndex());
            player.getSkills().addExperience(Skills.CRAFTING, 32.5);
            return true;
        }

    }
}
