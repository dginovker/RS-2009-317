package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link org.gielinor.game.interaction.OptionHandler} for item packs.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PackOptionHandler extends OptionHandler {

    private static final Logger log = LoggerFactory.getLogger(PackOptionHandler.class);

    /**
     * Represents an item pack.
     */
    public enum Pack {
        BAIT(Item.BAIT_PACK, new Item(313, 100)),
        FEATHER(Item.FEATHER_PACK, new Item(314, 100)),
        //UNFINISHED_BROAD_BOLTS(11887, new Item(11876, 100)),
        //BROAD_ARROWHEAD(11885, new Item(11874, 100)),
        WATER_FILLED_VIAL(Item.WATER_FILLED_VIAL_PACK, new Item(228, 100)),
        EMPTY_VIAL(Item.EMPTY_VIAL_PACK, new Item(230, 100)),
        EYE_OF_NEWT(Item.EYE_OF_NEWT_PACK, new Item(222, 100)),
        AMYLASE(14747, new Item(14742, 100));

        /**
         * The id of the pack.
         */
        private final int itemId;

        /**
         * The items it gives.
         */
        private final Item item;

        /**
         * Constructs a new {@code Pack}.
         *
         * @param itemId The id of the pack.
         * @param item   The items it gives.
         */
        Pack(int itemId, Item item) {
            this.itemId = itemId;
            this.item = item;
        }

        /**
         * Gets a {@code Pack} for id.
         *
         * @param id The id of the item.
         * @return The pack.
         */
        public static Pack forId(int id) {
            for (Pack pack : Pack.values()) {
                if (pack.itemId == id) {
                    return pack;
                }
            }
            return null;
        }
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (Pack pack : Pack.values()) {
            if (ItemDefinition.forId(pack.itemId) == null) {
                log.warn("Item definition missing for item [{}].", pack.itemId);
                continue;
            }
            ItemDefinition.forId(pack.itemId).getConfigurations().put("option:open", this);
        }
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getLocks().lockInteractions(2);
        Pack pack = Pack.forId(node.getId());
        if (pack == null || !player.getInventory().contains(node.getId())) {
            return false;
        }
        if (player.getInventory().remove(((Item) node))) {
            player.getInventory().add(pack.item);
            AchievementDiary.finalize(player, AchievementTask.ITEM_PACK_OPEN);
            return true;
        }
        return false;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}
