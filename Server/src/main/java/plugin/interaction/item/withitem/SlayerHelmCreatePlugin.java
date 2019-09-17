package plugin.interaction.item.withitem;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.slayer.Equipment;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import plugin.interaction.item.SlayerHelmetPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Require players to have the ability to create helmet recolours
 *
 * @author Corey
 */
public final class SlayerHelmCreatePlugin extends UseWithHandler {

    public enum SlayerHelmAssembly {
        REGULAR_SLAYER_HELM(SlayerHelmetPlugin.SLAYER_HELMET, "You carefully combine all the pieces and create a Slayer Helmet.",
            Equipment.EARMUFFS.getItem(),
            Equipment.FACEMASK.getItem(),
            Equipment.NOSE_PEG.getItem(),
            Equipment.SPINY_HELMET.getItem(),
            SlayerHelmetPlugin.BLACK_MASKS[0],
            Equipment.ENCHANTED_GEM.getItem()) {
            @Override
            public boolean canMake(Player player) {
                if (!player.getSavedData().getActivityData().hasLearnedSlayerOption(2)) {
                    player.getActionSender().sendMessage("You do not have the required knowledge to create a Slayer helmet.");
                    return false;
                }
                return true;
            }
        },

        BLACK_SLAYER_HELM(SlayerHelmetPlugin.BLACK_SLAYER_HELMET,
            SlayerHelmetPlugin.SLAYER_HELMET,
            SlayerHelmetPlugin.KBD_HEAD),

        GREEN_SLAYER_HELM(SlayerHelmetPlugin.GREEN_SLAYER_HELMET,
            SlayerHelmetPlugin.SLAYER_HELMET,
            SlayerHelmetPlugin.KQ_HEAD),

        RED_SLAYER_HELM(SlayerHelmetPlugin.RED_SLAYER_HELMET,
            SlayerHelmetPlugin.SLAYER_HELMET,
            SlayerHelmetPlugin.ABYSSAL_HEAD),

        PURPLE_SLAYER_HELM(SlayerHelmetPlugin.PURPLE_SLAYER_HELMET,
            SlayerHelmetPlugin.SLAYER_HELMET,
            SlayerHelmetPlugin.DARK_CLAW);

        private Item product;
        private String message;
        private Item[] ingredients;

        public Item getProduct() {
            return product;
        }

        public String getMessage() {
            return message;
        }

        public Item[] getIngredients() {
            return ingredients;
        }

        public int[] getIngredientIds() {
            return Arrays.stream(ingredients).mapToInt(Item::getId).toArray();
        }

        public boolean canMake(Player player) {
            return true;
        }

        SlayerHelmAssembly(Item product, Item... ingredients) {
            this.product = product;
            this.message = null;
            this.ingredients = ingredients;
        }

        SlayerHelmAssembly(Item product, String message, Item... ingredients) {
            this.product = product;
            this.message = message;
            this.ingredients = ingredients;
        }

        public static SlayerHelmAssembly forUseWithEvent(Item baseItem, Item usedItem) {
            for (SlayerHelmAssembly helm : values()) {
                List<Integer> ingredients = Arrays.stream(helm.getIngredientIds()).boxed().collect(Collectors.toList());
                if (ingredients.contains(baseItem.getId()) && ingredients.contains(usedItem.getId())) {
                    return helm;
                }
            }
            return null;
        }

        public static SlayerHelmAssembly forHelmetId(int helmetId) {
            return Arrays.stream(values()).filter(helm -> helm.product.getId() == helmetId).findFirst().orElse(null);
        }

        // gather all unique ingredient item ids from all enum values
        public static int[] uniqueIngredientIds() {
            return Arrays.stream(values())
                .map(SlayerHelmAssembly::getIngredients)
                .flatMap(Arrays::stream)
                .mapToInt(Item::getId)
                .distinct()
                .toArray();
        }
    }

    public SlayerHelmCreatePlugin() {
        super(SlayerHelmAssembly.uniqueIngredientIds());
    }

    @Override
    public boolean handle(NodeUsageEvent nodeUsageEvent) {
        if (nodeUsageEvent == null || nodeUsageEvent.getBaseItem() == null || nodeUsageEvent.getUsedItem() == null) {
            return false;
        }

        SlayerHelmAssembly helm = SlayerHelmAssembly.forUseWithEvent(nodeUsageEvent.getBaseItem(), nodeUsageEvent.getUsedItem());
        Player player = nodeUsageEvent.getPlayer();
        if (helm == null) {
            return false;
        }
        if (!player.getInventory().containItems(helm.getIngredientIds())) {
            return false;
        }
        if (!helm.canMake(player)) {
            return true;
        }
        if (player.getSkills().getLevel(Skills.CRAFTING) < 55) {
            player.getActionSender().sendMessage("You need a Crafting level of 55 to create a Slayer helmet.");
            return true;
        }
        player.getInventory().remove(helm.getIngredients());
        player.getInventory().add(helm.getProduct());
        if (helm.getMessage() != null) {
            player.getActionSender().sendMessage(helm.getMessage());
        }
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        Arrays.stream(SlayerHelmAssembly.uniqueIngredientIds()).forEach(i -> addHandler(i, ITEM_TYPE, this));
        return this;
    }
}
