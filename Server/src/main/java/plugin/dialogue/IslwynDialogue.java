package plugin.dialogue;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * The dialogue plugin for Islwyn.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public final class IslwynDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@link IslwynDialogue} {@link DialoguePlugin}.
     */
    public IslwynDialogue() {
    }

    /**
     * Constructs a new {@link IslwynDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public IslwynDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new IslwynDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello! What can I help you with?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                options("Ask about Crystal equipment", "Ask about charging a crystal", "Nothing");
                stage = 1;
                break;
            case 1:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        npc("Crystal equipment are weapons used by elven warriors,", "they can be obtained by bringing me a Tiny elf",
                            "crystal. You can find one by killing Elf warriors,", "and I can charge it for a minimum fee.");
                        stage = 0;
                        break;
                    case 2:
                        player("Could you charge a Tiny elf crystal for me?");
                        stage = 2;
                        break;
                    case 3:
                        player("Nothing.");
                        stage = END;
                        break;
                }
                break;
            case 2:
                if (player.getInventory().contains(6103)) {
                    npc("I can charge it into a Crystal bow or a Crystal shield.", "It will cost you 300k coins.", "What would you like?");
                    stage = 3;
                    break;
                }
                npc("I'm afraid you don't have a Tiny elf crystal to charge.");
                stage = END;
                break;
            case 3:
                options("Crystal Shield", "Crystal Bow", "Never mind.");
                stage = 4;
                break;
            case 4:
                int itemId = -1;
                switch (optionSelect.getButtonId()) {
                    case 1:
                        itemId = 4224;
                        break;
                    case 2:
                        itemId = 4212;
                        break;
                    case 3:
                        end();
                        return true;
                }
                if (!player.getInventory().contains(new Item(Item.COINS, 300000))) {
                    npc("You don't seem to have 300,000 coins.");
                    stage = END;
                    break;
                }
                if (player.getInventory().remove(new Item(6103), new Item(Item.COINS, 300000))) {
                    npc.animate(Animation.create(722));
                    player.getInventory().add(new Item(itemId));
                    interpreter.sendItemMessage(itemId, "Islwyn charges the tiny elf crystal into a " + ItemDefinition.forId(itemId).getName() + "!");
                    stage = END;
                    return true;
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
        return new int[]{ 1680 };
    }

}
