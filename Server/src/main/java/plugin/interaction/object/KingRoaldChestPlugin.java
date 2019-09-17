package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.quest.QuestState;
import org.gielinor.game.content.global.quest.impl.TheLostKingdom;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;

/**
 * Represents the chest in Varrock Palace, in King Roald's room.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class KingRoaldChestPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(5108).getConfigurations().put("option:open", this);
        PluginManager.definePlugin(new KingRoaldChestDialogue());
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, final String option) {
        if (player.getQuestRepository().getQuest(TheLostKingdom.NAME).getState() == QuestState.COMPLETED) {
            player.getDialogueInterpreter().open("roald-chest");
            return true;
        }
        if (player.getQuestRepository().getQuest(TheLostKingdom.NAME).getStage() < 21) {
            return false;
        }
        Item[] jester = TheLostKingdom.getJesterItems(player);
        if (jester == null) {
            player.getActionSender().sendMessage("You already have all of the Silly jester outfit.");
            return true;
        }
        if (player.getInventory().freeSlots() == 0) {
            player.getActionSender().sendMessage("You don't have enough space in your inventory for that.");
            return true;
        }
        player.lock(2);
        Item jesterItem = jester[0];
        player.getDialogueInterpreter().sendItemMessage(jesterItem, "You take a " + jesterItem.getDefinition().getName() + " from the chest!");
        player.getInventory().add(jesterItem, true);
        return true;
    }

    /**
     * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for King Roald's chest.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static class KingRoaldChestDialogue extends DialoguePlugin {

        public KingRoaldChestDialogue() {
        }

        public KingRoaldChestDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new KingRoaldChestDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            options("Jester outfit", "Purchase gloves", "Nothing");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    switch (optionSelect) {
                        case THREE_OPTION_ONE:
                            Item[] items = TheLostKingdom.getJesterItems(player);
                            if (items == null) {
                                end();
                                interpreter.sendPlaneMessage(false, "You already have all of the pieces.");
                                return true;
                            }
                            if (player.getInventory().freeSlots() < items.length) {
                                end();
                                interpreter.sendPlaneMessage(false, "Not enough space in your inventory for that.");
                                return true;
                            }
                            player.getInventory().add(items);
                            end();
                            break;
                        case THREE_OPTION_TWO:
                            end();
                            Shops.KING_ROALDS_USELESS_JUNK.open(player);
                            break;
                        case THREE_OPTION_THREE:
                            end();
                            break;
                    }
                    stage = 1;
                    break;

                case END:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ DialogueInterpreter.getDialogueKey("roald-chest") };
        }
    }
}
