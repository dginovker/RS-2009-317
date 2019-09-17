package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * The dialogue plugin for Ava.
 *
 * @author Logan G. <logan@Gielinor.org>
 *         TODO 317 : RuneScape interface for buying the inventions
 */
public final class AvaDialogue extends DialoguePlugin {

    /**
     * The option selected.
     */
    private int option = -1;

    /**
     * Constructs a new {@link AvaDialogue} {@link DialoguePlugin}.
     */
    public AvaDialogue() {
    }

    /**
     * Constructs a new {@link AvaDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public AvaDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new AvaDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello, can I interest you in a new device?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                player("What is the device?");
                stage = 2;
                break;
            case 2:
                npc("It's a great invention, it attracts arrows that you",
                    "fire so you lose less ammunition.");
                stage = 3;
                break;
            case 3:
                interpreter.sendOptions("Select a device", "Basic: 200k coins", "Upgraded: 200k coins +75 steel arrows");
                stage = 4;
                break;
            case 4:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        option = 1;
                        npc("That will cost you 200,000 coins.", "Are you sure?");
                        break;
                    case TWO_OPTION_TWO:
                        option = 2;
                        npc("That will cost you 200,000 coins and 75 steel arrows.", "Are you sure?");
                        break;
                }
                stage = 5;
                break;
            case 5:
                options("Yes, here's 200k coins" + (option == 2 ? " and 75 steel arrows" : "") + ".", "No thank you.");
                stage = 6;
                break;
            case 6:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if (option == 2 && !player.getInventory().contains(new Item(886, 75))) {
                            player("I'm afraid I don't have 75 steel arrows.");
                            stage = END;
                            break;
                        }
                        if (!player.getInventory().contains(new Item(Item.COINS, 200000))) {
                            player("I'm afraid I don't have enough money.");
                            stage = END;
                            break;
                        }
                        if (!player.getInventory().hasRoomFor(new Item(option == 2 ? 10499 : 10498, 1))) {
                            npc("You don't have enough inventory space.");
                            stage = END;
                            break;
                        }
                        if (player.getInventory().remove(new Item(Item.COINS, 200000))) {
                            if (option == 2) {
                                player.getInventory().remove(new Item(886, 75));
                            }
                            npc("Here's your device; take good care of your chicken.");
                            if (option == 2) {
                                AchievementDiary.finalize(player, AchievementTask.UPGRADED_DEVICE_AVA);
                            }
                            player.getInventory().add(new Item(option == 2 ? 10499 : 10498, 1));
                            stage = END;
                            break;
                        }
                        stage = END;
                        break;
                    case TWO_OPTION_TWO:
                        player("No thank you.");
                        stage = END;
                        break;
                }
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5198 };
    }
}
