package plugin.interaction.inter;

import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.free.smithing.SmithingConstants;
import org.gielinor.game.content.skill.free.smithing.smelting.SmeltingPulse;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents the Smelting interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SmeltingInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(SmithingConstants.SMELTING_INTERFACE, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        Smeltable smeltable = Smeltable.forId(button);
        if (smeltable == null) {
            return false;
        }
        int amount = smeltable.getItemSelectDialogue().amountForId(button);
        if (amount == -1) {
            player.setAttribute("runscript", new RunScript() {

                @Override
                public boolean handle() {
                    int amount = (int) value;
                    player.getPulseManager().run(new SmeltingPulse(player, null, smeltable, amount));
                    return false;
                }
            });
            player.getInterfaceState().closeChatbox();
            player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
            return true;
        }
        player.getPulseManager().run(new SmeltingPulse(player, null, smeltable, amount));
        return true;
    }

    /**
     * Represents an ore smeltable.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public enum Smeltable {

        BRONZE(new Item(436), new Item(438), new Item(2349), 1, 6.2, OptionSelect.SMELTING_BRONZE),
        //BLURITE(new Item(668), new Item(453, 1), new Item(9467), 8, 8, null),
        IRON(new Item(440), null, new Item(2351), 15, 12.5, OptionSelect.SMELTING_IRON),
        SILVER(new Item(442), null, new Item(2355), 20, 13.7, OptionSelect.SMELTING_SILVER),
        STEEL(new Item(440), new Item(453, 2), new Item(2353), 30, 17.5, OptionSelect.SMELTING_STEEL),
        GOLD(new Item(444), null, new Item(2357), 40, 22.5, OptionSelect.SMELTING_GOLD),
        MITHRIL(new Item(447), new Item(453, 4), new Item(2359), 50, 30, OptionSelect.SMELTING_MITHRIL),
        ADAMANT(new Item(449), new Item(453, 6), new Item(2361), 70, 37.5, OptionSelect.SMELTING_ADAMANT),
        RUNE(new Item(451), new Item(453, 8), new Item(2363), 85, 50, OptionSelect.SMELTING_RUNE),;

        /**
         * The primary ore needed
         */
        private final Item primary;
        /**
         * The secondary ore needed
         */
        private final Item secondary;
        /**
         * The bar given
         */
        private final Item bar;
        /**
         * The level required to smelt this ore
         */
        private final int level;
        /**
         * The experience given for smelting this ore
         */
        private final double experience;
        /**
         * The item select dialogue for button handling.
         */
        private final OptionSelect optionSelect;
        /**
         * The mapping of smeltable items
         */
        private static final Map<Integer, Smeltable> smeltable = new HashMap<>();

        static {
            for (Smeltable smelt : Smeltable.values()) {
                smeltable.put(smelt.ordinal(), smelt);
            }
        }

        /**
         * Represents a smithable with a primary ore and a secondary ore to smelt
         *
         * @param primary      the primary ore to smelt
         * @param secondary    the secondary ore needed to smelt
         * @param bar          the bar given
         * @param level        the level required
         * @param experience   the experience given for smelting
         * @param optionSelect
         */
        Smeltable(Item primary, Item secondary, Item bar, int level, double experience, OptionSelect optionSelect) {
            this.primary = primary;
            this.secondary = secondary;
            this.bar = bar;
            this.level = level;
            this.experience = experience;
            this.optionSelect = optionSelect;
        }

        /**
         * The primary ore needed
         *
         * @return the primary
         */
        public Item getPrimary() {
            return primary;
        }

        /**
         * The secondary ore needed
         *
         * @return the secondary
         */
        public Item getSecondary() {
            return secondary;
        }

        /**
         * The bar given
         *
         * @return the bar
         */
        public Item getBar() {
            return bar;
        }

        /**
         * The level required to smelt this ore
         *
         * @return the level
         */
        public int getLevel() {
            return level;
        }

        /**
         * The experience given for smelting this ore
         *
         * @return the experience
         */
        public double getExperience() {
            return experience;
        }

        /**
         * Gets the item select dialogue.
         *
         * @return The optionSelect.
         */
        public OptionSelect getItemSelectDialogue() {
            return optionSelect;
        }

        /**
         * Gets the smeltable by an item id.
         *
         * @param itemId The id of the item.
         * @return The smeltable.
         */
        public static Smeltable forItemId(int itemId) {
            for (Smeltable smelt : Smeltable.values()) {
                if (smelt.getItemSelectDialogue() == null) {
                    continue;
                }
                if (smelt.getPrimary().getId() == itemId) {
                    return smelt;
                }
            }
            return null;
        }

        /**
         * Gets the smeltable by a button id.
         *
         * @param id The id of the button.
         * @return The smeltable.
         */
        public static Smeltable forId(int id) {
            for (Smeltable smelt : Smeltable.values()) {
                if (smelt.getItemSelectDialogue() == null) {
                    continue;
                }
                for (int buttonId : smelt.getItemSelectDialogue().getIds()) {
                    if (id == buttonId) {
                        return smelt;
                    }
                }
            }
            return null;
        }
    }

}
