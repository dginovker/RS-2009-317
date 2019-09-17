package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the magic store dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class MagicStoreDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code MagicStoreDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public MagicStoreDialogue(final Player player) {
        super(player);
    }

    /**
     * Constructs a new {@code MagicStoreDialogue} {@code Object}.
     */
    public MagicStoreDialogue() {
        /**
         * empty.
         */
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new MagicStoreDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc("Welcome to the Magic Guild Store. Would you like to", "buy some magic supplies?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                options("Yes please.", "No thank you.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        player("Yes please.");
                        stage = 10;
                        break;
                    case 2:
                        end();
                        break;
                }
                break;
            case 10:
                end();
                Shops.WIZARDS_GUILD__RUNE_STORE.open(player);
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 461 };
    }

}
