package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class BanditShopkeeperDialogue extends DialoguePlugin {

    /**
     * Constructs a new <code>BanditShopkeeperDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     */
    public BanditShopkeeperDialogue() {
    }

    /**
     * Constructs a new <code>BanditShopkeeperDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     *
     * @param player The player.
     */
    public BanditShopkeeperDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BanditShopkeeperDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        player("Hello.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                npc("Stuff for sale.", "You buying?");
                stage = 1;
                break;
            case 1:
                options("Yes", "No");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        Shops.KARAMJA_WINES_SPIRITS_AND_BEERS.open(player);
                        break;
                    case TWO_OPTION_TWO:
                        npc("No?", "'Bye then.");
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
        return new int[]{ 1917 };
    }
}
