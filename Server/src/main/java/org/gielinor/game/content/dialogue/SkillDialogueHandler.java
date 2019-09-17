package org.gielinor.game.content.dialogue;

import org.gielinor.game.content.dialogue.impl.Dialogue;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;


/**
 * Represents a skill dialogue handler class.
 *
 * @author 'Vexia
 */
public class SkillDialogueHandler {

    /**
     * Represents the skill dialogue id.
     */
    public static final int SKILL_DIALOGUE = 3 << 16;

    /**
     * Represents the player.
     */
    private final Player player;

    /**
     * Represents the skill dialogue type.
     */
    private final SkillDialogue type;

    /**
     * Represents the object data passed through.
     */
    private final Object[] data;

    /**
     * Constructs a new {@code SkillDialogueHandler} {@code Object}.
     *
     * @param player the player.
     * @param type   the type.
     * @param data   the data.
     */
    public SkillDialogueHandler(final Player player, final SkillDialogue type, final Object... data) {
        this.player = player;
        this.type = type;
        this.data = data;
    }

    /**
     * Method used to open a skill dialogue.
     */
    public void open() {
        player.getDialogueInterpreter().open(SKILL_DIALOGUE, this);
    }

    /**
     * Method used to display the content on the dialogue.
     */
    public void display() {
        type.display(player, this);
    }

    /**
     * Method used to create a product.
     *
     * @param amount the amount.
     * @param index  the index.
     */
    public void create(final int amount, int index) {
    }

    /**
     * Gets the total amount of items to be made.
     *
     * @param index the index.
     * @return the amount.
     */
    public int getAll(int index) {
        return 0;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the type.
     *
     * @return The type.
     */
    public SkillDialogue getType() {
        return type;
    }

    /**
     * Gets the passed data.
     *
     * @return the data.
     */
    public Object[] getData() {
        return data;
    }

    /**
     * Represents a skill dialogue type.
     *
     * @author 'Vexia
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static enum SkillDialogue {
        ONE_OPTION(Dialogue.ITEM_SELECT_1.getInterfaceId()),
        TWO_OPTION(Dialogue.ITEM_SELECT_2.getInterfaceId()),
        THREE_OPTION(Dialogue.ITEM_SELECT_3.getInterfaceId()),
        FOUR_OPTION(Dialogue.ITEM_SELECT_4.getInterfaceId()),
        FIVE_OPTION(Dialogue.ITEM_SELECT_5.getInterfaceId());

        /**
         * Represents the interface id.
         */
        private final int interfaceId;

        /**
         * Constructs a new {@code SkillDialogue} {@code Object}.
         *
         * @param interfaceId The interface id.
         */
        private SkillDialogue(final int interfaceId) {
            this.interfaceId = interfaceId;
        }

        /**
         * Method used to display the content for this type.
         *
         * @param player  the player.
         * @param handler the handler.
         */
        public void display(final Player player, final SkillDialogueHandler handler) {
            player.getDialogueInterpreter().sendItemSelectDialogue(getItemArray(handler.getData()));
        }

        /**
         * Gets the amount.
         *
         * @param handler  the handler.
         * @param buttonId the buttonId.
         * @return the amount.
         */
        public int getAmount1(SkillDialogueHandler handler, final int buttonId) {
            OptionSelect optionSelect = OptionSelect.forId(handler.getType().getInterfaceId());
            int amount = optionSelect.amountForId(optionSelect.getChildId());
            return amount == 28 ? handler.getAll(getIndex(handler, buttonId)) : amount;
        }

        /**
         * Gets the amount.
         *
         * @param handler  the handler.
         * @param buttonId the buttonId.
         * @return the amount.
         */
        public int getAmount(SkillDialogueHandler handler, final int buttonId) {
            int amount = OptionSelect.forId(handler.getType().getInterfaceId()).getAmount(buttonId);
            return amount == 28 ? handler.getAll(getIndex(handler, buttonId)) : amount;
        }

        /**
         * Gets the index selected.
         *
         * @param handler  the handler.
         * @param buttonId the buttonId.
         * @return the index selected.
         */
        public int getIndex(SkillDialogueHandler handler, final int buttonId) {
            return OptionSelect.forId(handler.getType().getInterfaceId()).getIndex(buttonId);
        }

        /**
         * Gets an item array from the {@link org.gielinor.game.content.dialogue.SkillDialogueHandler} object data.
         *
         * @param data The object data.
         * @return The item array.
         */
        public Item[] getItemArray(Object... data) {
            Item[] items = new Item[data.length];
            for (int index = 0; index < data.length; index++) {
                items[index] = (Item) data[index];
            }
            return items;
        }

        /**
         * Gets the proper {@link org.gielinor.game.content.dialogue.SkillDialogueHandler.SkillDialogue} for the amount of items.
         *
         * @param amount The amount of items that are to be shown.
         * @return The proper skill dialogue.
         */
        public static SkillDialogue forAmount(int amount) {
            for (SkillDialogue skillDialogue : SkillDialogue.values()) {
                if ((skillDialogue.ordinal() + 1) == amount) {
                    return skillDialogue;
                }
            }
            return null;
        }

        /**
         * Gets the interfaceId.
         *
         * @return The interfaceId.
         */
        public int getInterfaceId() {
            return interfaceId;
        }

    }
}
