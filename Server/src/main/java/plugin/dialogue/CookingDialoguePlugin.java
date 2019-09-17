package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.consumable.ConsumableProperties;
import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.global.consumable.CookingProperties;
import org.gielinor.game.content.global.consumable.Food;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the dialogue used to handle the amount to make of a cookable item.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CookingDialoguePlugin extends DialoguePlugin {

    private static final Logger log = LoggerFactory.getLogger(CookingDialoguePlugin.class);

    /**
     * Represents the cooking dialogue id.
     */
    private static final int DIALOGUE_ID = 43989;

    /**
     * Represents the sinew item.
     */
    private static final Item SINEW = new Item(9436);

    /**
     * Represents the food we're cooking.
     */
    private Food food;

    /**
     * Represents the obejct we're cooking on.
     */
    private GameObject object;

    /**
     * Constructs a new {@code CookingDialoguePlugin} {@code Object}.
     */
    public CookingDialoguePlugin() {
    }

    /**
     * Constructs a new {@code CookingDialoguePlugin} {@code Object}.
     *
     * @param player the player.
     */
    public CookingDialoguePlugin(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new CookingDialoguePlugin(player);
    }

    @Override
    public void init() {
        super.init();
        try {
            new MeatPlugin().newInstance(null);
        } catch (Throwable t) {
            log.error("Failed to init cooking dialogue plugin.", t);
        }
    }

    @Override
    public boolean open(Object... args) {
        food = ((Food) args[0]);
        object = ((GameObject) args[1]);
        if (args.length == 3) {
            interpreter.sendOptions("Select an Option", "Dry the meat into sinew.", "Cook the meat.");
            stage = 100;
            return true;
        }
        if (player.getInventory().getCount(food.getRaw()) == 1) {
            end();
            food.cook(player, object, 1);
            return true;
        }
        display();
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                end();
                int amount = getAmount(optionSelect.getChildId());
                if (amount == -1) {
                    player.setAttribute("runscript", new RunScript() {

                        @Override
                        public boolean handle() {
                            int amount = (int) value;
                            food.cook(player, object, amount);
                            return false;
                        }
                    });
                    player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
                }
                food.cook(player, object, amount);
                break;
            case 100:
                switch (optionSelect.getId()) {
                    case 2461:
                        food = Consumables.findFood(SINEW).orElse(null);
                        display();
                        break;
                    case 2462:
                        food = MeatPlugin.MEAT;
                        display();
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DIALOGUE_ID };
    }

    /**
     * Method used to display the content food.
     */
    public void display() {
        player.getInterfaceState().openChatbox(1743);
        player.getActionSender().sendItemZoomOnInterface(food.getRaw().getId(), 160, 13716);
        player.getActionSender().sendString(food.getRaw().getName(), 13717);
        stage = 0;
    }

    /**
     * Method used to get the amount to make based off the button id.
     *
     * @param buttonId the button id.
     * @return the amount to make.
     */
    private int getAmount(final int buttonId) {
        switch (buttonId) {
            case 13720:
                return 1;
            case 13719:
                return 5;
            case 13718:
                return -1;
            case 13717:
                return 28;
        }
        return 0;
    }

    /**
     * Represents the meat consumable food.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class MeatPlugin extends Food {

        /**
         * Represents the meat plugin.
         */
        public static final MeatPlugin MEAT = new MeatPlugin(2142, 2132, 2146, new ConsumableProperties(3), new MeatProperty(1, 30, 30, "You cook a piece of beef.", "You accidentally burn the meat."));
        /**
         * Represents the cooking dialogue id.
         */
        private static final int DIALOGUE_ID = 43989;

        /**
         * Constructs a new {@code MeatPlugin} {@code Object}.
         */
        public MeatPlugin() {
        }

        /**
         * Constructs a new {@code Meat} {@code Object}.
         *
         * @param item              the item.
         * @param raw               the raw item.
         * @param burnt             the burnt item.
         * @param foodProperties    the food properties.
         * @param cookingProperties the cooking properties.
         */
        public MeatPlugin(int item, int raw, int burnt, ConsumableProperties foodProperties, CookingProperties cookingProperties) {
            super(item, raw, burnt, foodProperties, cookingProperties);
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            Consumables.add(MEAT);
            Consumables.add(new MeatPlugin(9436, 2132, 2146, null, new MeatProperty(1, 30, 30, "You dry a piece of beef and extract the sinew.", "You accidentally burn the meat.")));
            Consumables.add(new MeatPlugin(2134, 2132, 2146, new ConsumableProperties(3), new MeatProperty(1, 30, 30, "You cook a piece of rat meat.", "You accidentally burn the meat.")));
            Consumables.add(new MeatPlugin(2136, 2132, 2146, new ConsumableProperties(3), new MeatProperty(1, 30, 30, "You cook a piece of bear meat.", "You accidentally burn the meat.")));
            return this;
        }

        @Override
        public boolean interact(final Player player, final Node node) {
            player.getDialogueInterpreter().open(DIALOGUE_ID, this, node, true);
            return false;
        }

        /**
         * Represents the meat properties used to override for sinew.
         *
         * @author 'Vexia
         * @date 23/12/2013
         */
        public static class MeatProperty extends CookingProperties {

            /**
             * Constructs a new {@code Meat} {@code Object}.
             *
             * @param level      the level.
             * @param experience the experience.
             * @param burnLevel  the burn level.
             * @param messages   the messages.
             */
            public MeatProperty(int level, double experience, int burnLevel, String... messages) {
                super(level, experience, burnLevel, true, messages);
            }

            @Override
            public boolean cook(final Food food, final Player player, final GameObject object) {
                return super.cook(food, player, object);
            }

        }
    }

}
