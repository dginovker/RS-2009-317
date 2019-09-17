package plugin.skill.cooking;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to make dough.
 *
 * @author 'Vexia
 */
public final class MakeDoughPlugin extends UseWithHandler {

    /**
     * Represents the water sources.
     */
    private static final Item[][] SOURCES = new Item[][]{ { new Item(1929), new Item(1925) }, { new Item(1921), new Item(1923) }, { new Item(1937), new Item(1935) } };

    /**
     * Constructs a new {@code MakeDoughPlugin} {@code Object}.
     */
    public MakeDoughPlugin() {
        super(1933);
    }


    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (Item[] SOURCE : SOURCES) {
            addHandler(SOURCE[0].getId(), ITEM_TYPE, this);
        }
        new MakeDoughDialogue().init();
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        Item[] data = null;
        for (Item[] SOURCE : SOURCES) {
            if (SOURCE[0].getId() == event.getUsedItem().getId() || SOURCE[0].getId() == event.getBaseItem().getId()) {
                data = SOURCE;
                break;
            }
        }
        player.getDialogueInterpreter().open(59 << 16 | 1, (Object) data);
        return false;
    }

    /**
     * Represents the plugin used to make dough..
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class MakeDoughDialogue extends DialoguePlugin {

        /**
         * Represents the dough items to make.
         */
        private static final Item[] DOUGHS = new Item[]{ new Item(2307), new Item(1953), new Item(2283), new Item(1863) };

        /**
         * Represents the pot of flour item.
         */
        private static final Item FLOUR = new Item(1933);

        /**
         * Represents the water source data.
         */
        private Item[] waterData;

        /**
         * Constructs a new {@code MakeDoughDialogue} {@code Object}.
         */
        public MakeDoughDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code MakeDoughDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public MakeDoughDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new MakeDoughDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            waterData = (Item[]) args[0];
            interpreter.sendOptions("What do you wish to make?", "Bread dough.", "Pastry dough.", "Pizza dough.", "Pitta dough.");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            int buttonId = optionSelect == null ? -1 : optionSelect.getId();
            switch (stage) {
                case 0:
                    makeDough(DOUGHS[buttonId - 2482]);
                    end();
                    break;
            }
            return true;
        }

        /**
         * Method used to make a dough item.
         *
         * @param dough the dough.
         */
        private void makeDough(final Item dough) {
            if (player.getInventory().remove(waterData[0]) && player.getInventory().remove(FLOUR)) {
                player.getInventory().add(waterData[1]);
                player.getInventory().add(dough);
                player.getActionSender().sendMessage("You mix the flour and water to make some " + dough.getName().toLowerCase() + ".", 1);
            }
        }

        @Override
        public int[] getIds() {
            return new int[]{ 59 << 16 | 1 };
        }

    }

}
