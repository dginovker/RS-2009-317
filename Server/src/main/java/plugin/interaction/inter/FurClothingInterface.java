package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin related to the custom fur clothing interface.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FurClothingInterface extends ComponentPlugin {

    /**
     * Represents the item array of clothing.
     */
    private static final Item[] ITEMS = new Item[]{
        new Item(10065), new Item(10067),
        new Item(10053), new Item(10055),
        new Item(10057), new Item(10059),
        new Item(10061), new Item(10063),
        new Item(10075), new Item(10069),
        new Item(10045), new Item(10043),
        new Item(10041), new Item(10051),
        new Item(10049), new Item(10047),
        new Item(10039), new Item(10037),
        new Item(10035), new Item(10071)
    };

    /**
     * Gets the value message.
     *
     * @param item The item.
     * @return The value message.
     */
    public static String getValue(Item item) {
        FurClothing furClothing = FurClothing.forId(item.getId());
        if (furClothing == null) {
            return "Something went wrong... Please report this on forums (" + item.getId() + ").";
        }
        int index = furClothing.getIndex(item.getId());
        if (index == -1) {
            return "Something went wrong... Please report this on forums (" + item.getId() + ").";
        }
        return furClothing.getProduct(index).getName() + ": Requires " + (furClothing.isAnyFur(index) ? "any " : "") +
            furClothing.getFur(index).getCount() + " " + furClothing.getFur(index).getName().toLowerCase() +
            (furClothing.getFur(index).getCount() > 1 ? "s" : "") + " and " + furClothing.getCoins(index).getCount() + " coins.";
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(477, this);
        new FancyDressOwnerDialogue().init();
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        final Object[] data = FurClothing.getData(slot);
        if (data == null) {
            return false;
        }
        final FurClothing clothing = ((FurClothing) data[0]);
        final int index = (int) data[1];
        switch (opcode) {
            case 133:
                switch (button) {
                    case 75:
                        player.getActionSender().sendMessage(clothing.getValueMessage(index));
                        break;
                }
                break;
            case 236://1
                clothing.buy(player, index, 1);
                break;
            case 157://5
                clothing.buy(player, index, 5);
                break;
            case 243://10
                clothing.buy(player, index, 10);
                break;
        }
        return true;
    }

    /**
     * Method used to open the fur clothing interface.
     *
     * @param player the player.
     */
    public static void open(final Player player) {
        Shops.CUSTOM_FUR_CLOTHING.open(player);
        // player.getInterfaceState().open(new Component(477));
        // PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 477, 75, 91, new Container(21, ITEMS), true));
    }

    /**
     * Represents fur clothing.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public enum FurClothing {
        POLAR(new Item[][]{
            { new Item(10117, 2), new Item(10065), new Item(Item.COINS, 20) },
            { new Item(10117, 2), new Item(10067), new Item(Item.COINS, 20) } }, 0, 1),
        COMMON(new Item[][]{ { new Item(10121, 2), new Item(10053), new Item(Item.COINS, 20) }, { new Item(10121, 2), new Item(10055), new Item(Item.COINS, 20) } }, 2, 3),
        FELDIP(new Item[][]{ { new Item(10119, 2), new Item(10057), new Item(Item.COINS, 20) }, { new Item(10119, 2), new Item(10059), new Item(Item.COINS, 20) } }, 4, 5),
        DESERT(new Item[][]{ { new Item(10123, 2), new Item(10061), new Item(Item.COINS, 20) }, { new Item(10123, 2), new Item(10063), new Item(Item.COINS, 20) } }, 6, 7),
        LARUPIA(new Item[][]{ { new Item(10095), new Item(10045), new Item(Item.COINS, 500) }, { new Item(10095), new Item(10043), new Item(Item.COINS, 100) }, { new Item(10095), new Item(10041), new Item(Item.COINS, 100) } }, 10, 11, 12),
        GRAAHK(new Item[][]{ { new Item(10099), new Item(10051), new Item(Item.COINS, 750) }, { new Item(10099), new Item(10049), new Item(Item.COINS, 150) }, { new Item(10099), new Item(10047), new Item(Item.COINS, 150) } }, 13, 14, 15),
        KYATT(new Item[][]{ { new Item(10103), new Item(10039), new Item(Item.COINS, 1000) }, { new Item(10103), new Item(10037), new Item(Item.COINS, 250) }, { new Item(10103), new Item(10035), new Item(Item.COINS, 250) } }, 16, 17, 18),
        DARK_KEBBIT(new Item[][]{ { new Item(10115, 2), new Item(10075), new Item(Item.COINS, 600) } }, 8),
        SPOTTED_KEBBIT(new Item[][]{ { new Item(10125, 2), new Item(10069), new Item(Item.COINS, 400) } }, 9),
        DASHING_KEBBIT(new Item[][]{ { new Item(10127, 2), new Item(10071), new Item(Item.COINS, 800) } }, 19);

        /**
         * Represents the items of the clothing in paralled to childs.
         */
        private final Item[][] clothing;

        /**
         * Represents the child ids of the clothing set.
         */
        private final int[] childs;

        /**
         * Constructs a new {@code FancyDressOwnerDialogue} {@code Object}.
         *
         * @param clothing the clothing.
         * @param childs   the childs.
         */
        FurClothing(final Item[][] clothing, final int... childs) {
            this.clothing = clothing;
            this.childs = childs;
        }

        /**
         * Method used to buy a clothing piece.
         *
         * @param player the player.
         * @param index  the index.
         * @param amount the amount.
         */
        public void buy(final Player player, final int index, int amount) {
            if (!player.getInventory().containsItem(getCoins(index))) {
                player.getActionSender().sendMessage("You don't have enough coins.");
                return;
            }
            if (!isAnyFur(index) ? !player.getInventory().containsItem(getFur(index)) :
                (!player.getInventory().containsItem(getFur(index, true))) &&
                    !player.getInventory().containsItem(getFur(index)) || amount == 0) {
                player.getActionSender().sendMessage("You don't have the material required to make this item.");
                return;
            }
            final Item fur = !isAnyFur(index) ? getFur(index) :
                !player.getInventory().containsItem(getFur(index)) ?
                    getFur(index, true) : getFur(index);
            int inventoryAmount = player.getInventory().getCount(fur);
            if (amount > inventoryAmount) {
                amount = inventoryAmount;
            }
            final Item coins = new Item(Item.COINS, getCoins(index).getCount() * amount);
            if (!player.getInventory().containsItem(coins)) {
                player.getActionSender().sendMessage("You don't have enough coins.");
                return;
            }
            if (player.getInventory().remove(coins, new Item(fur.getId(), fur.getCount() * amount))) {
                player.getInventory().add(new Item(getProduct(index).getId(), amount));
                player.getInventory().refresh();
                player.getInventory().update(true);
            }
        }

        /**
         * Gets the tile child.
         *
         * @return the title.
         */
        public int getTitleChild() {
            return childs[0];
        }

        /**
         * Gets the fur item.
         *
         * @param index the index.
         * @param all   if both furs.
         * @return the fur.
         */
        public Item getFur(int index, boolean all) {
            return all ? (new Item(clothing[index][0].getId() - 2)) : clothing[index][0];
        }

        /**
         * Gets the fur item.
         *
         * @param index the index.
         * @return the fur.
         */
        public Item getFur(int index) {
            return getFur(index, false);
        }

        /**
         * Gets the product item.
         *
         * @param index the index.
         * @return the product.
         */
        public Item getProduct(int index) {
            return clothing[index][1];
        }

        /**
         * Gets a {@link plugin.interaction.inter.FurClothingInterface.FurClothing} by the product id.
         *
         * @param id The product id.
         * @return The {@code FurClothing}.
         */
        public static FurClothing forId(int id) {
            for (FurClothing furClothing : FurClothing.values()) {
                for (Item[] items : furClothing.getClothing()) {
                    if (items[1].getId() == id) {
                        return furClothing;
                    }
                }
            }
            return null;
        }

        /**
         * Gets the index by the product id.
         *
         * @param id The product id.
         * @return The index.
         */
        public int getIndex(int id) {
            int index = 0;
            for (Item[] items : getClothing()) {
                if (items[1].getId() == id) {
                    return index;
                }
                index++;
            }
            return -1;
        }

        /**
         * Gets the coins item.
         *
         * @param index the index.
         * @return the coins.
         */
        public Item getCoins(int index) {
            return clothing[index][2];
        }

        /**
         * Checks if it takes any type of fur(tatty or not).
         *
         * @param index the index.
         * @return <code>True</code> if so.
         */
        public boolean isAnyFur(int index) {
            return index > 0 && ordinal() > 3 && ordinal() < 7;
        }

        /**
         * Gets the value message.
         *
         * @param index the index.
         * @return the value message.
         */
        public String getValueMessage(int index) {
            return getProduct(index).getName() + ": Requires " + (isAnyFur(index) ? "any " : "") +
                getFur(index).getCount() + " " + getFur(index).getName().toLowerCase() +
                (getFur(index).getCount() > 1 ? "s" : "") + " and " + getCoins(index).getCount() + " coins.";
        }

        /**
         * Gets the clothing.
         *
         * @return the clothing.
         */
        public Item[][] getClothing() {
            return clothing;
        }

        /**
         * Gets the childs.
         *
         * @return the childs.
         */
        public int[] getChilds() {
            return childs;
        }

        /**
         * Method used to get the data by the slot.
         *
         * @param slot the slot.
         * @return the clothing & the current index.
         */
        public static Object[] getData(int slot) {
            for (FurClothing cloth : values()) {
                for (int i = 0; i < cloth.getChilds().length; i++) {
                    if (cloth.getChilds()[i] == slot) {
                        return new Object[]{ cloth, i };
                    }
                }
            }
            return null;
        }
    }

    /**
     * Represents the fancy dress owner dialogue plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class FancyDressOwnerDialogue extends DialoguePlugin {

        /**
         * Constructs a new {@code FancyDressOwnerDialogue} {@code Object}.
         */
        public FancyDressOwnerDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code FancyDressOwnerDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public FancyDressOwnerDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new FancyDressOwnerDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Now you look like someone who goes to a lot of fancy", "dress parties.");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Errr....what are you saying exactly?");
                    stage = 1;
                    break;
                case 1:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I'm just saying that perhaps you would like to persure my", "selection of garments.");
                    stage = 2;
                    break;
                case 2:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Or, if that doesn't interest you, then maybe you have", "something else to offer? I'm always on the lookout for", "interesting or unusual new materials.");
                    stage = 3;
                    break;
                case 3:
                    interpreter.sendOptions("Select an Option", "Okay, let's see what you've got, then.", "I think I might just leave the persuing for now, thanks.", "Can you make clothing sutiable for hunting?", "What sort of unusual materials did you have in mind?");
                    stage = 4;
                    break;
                case 4:

                    switch (optionSelect) {
                        case FOUR_OPTION_ONE:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Okay, let's see what you've got, then.");
                            stage = 10;
                            break;
                        case FOUR_OPTION_TWO:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I think I might just leave the persuing for now, thanks.");
                            stage = 100;
                            break;
                        case FOUR_OPTION_THREE:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you make clothing suitable for hunting?");
                            stage = 30;
                            break;
                        case FOUR_OPTION_FOUR:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What sort of unusual materials did you have in mind?");
                            stage = 41;
                            break;
                    }

                    break;
                case 100:
                    end();
                    break;
                case 30:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Certainly. Take a look at my range of made-to-oder", "items. If you can supply the furs, I'll gladly make any of", "these for you.");
                    stage = 31;
                    break;
                case 31:
                    end();
                    FurClothingInterface.open(player);
                    break;
                case 41:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Well, soem more colourful feathers might be useful. For", "some surreal reason, all I normally seem to get offered", "are large quantities of rather beaten-up looking chicken", "feathers.");
                    stage = 42;
                    break;
                case 42:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "People must have some very strange pastimes around", "these parts, that's all I can say.");
                    stage = 43;
                    break;
                case 43:
                    interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Ok, let's see what you've got then.");
                    stage = 44;
                    break;
                case 44:
                    end();
                    Shops.FANCY_CLOTHES_STORE.open(player);
                    break;
                case 10:
                    end();
                    Shops.FANCY_CLOTHES_STORE.open(player);
                    break;
            }
            return true;

        }

        @Override
        public int[] getIds() {
            return new int[]{ 554 };
        }

    }
}
