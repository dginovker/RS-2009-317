package plugin.dialogue;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;

/**
 * Handles the SpellbookSwapDialogue dialogue.
 *
 * @author Vexia
 */
public class SpellbookSwapDialogue extends DialoguePlugin {

    public SpellbookSwapDialogue() {

    }

    public SpellbookSwapDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new SpellbookSwapDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        interpreter.sendOptions("Select a Spellbook", "Ancient", "Modern");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                int type = 0;
                switch (optionSelect.getButtonId()) {
                    case 1:
                        type = 1;
                        break;
                    case 2:
                        type = 2;
                        break;
                }
                final SpellBook book = type == 1 ? SpellBook.ANCIENT : SpellBook.MODERN;
                player.getSpellBookManager().setSpellBook(book);
                player.getInterfaceState().openTab(Sidebar.MAGIC_TAB, new Component(book.getInterfaceId()));
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 3264731 };
    }
}
