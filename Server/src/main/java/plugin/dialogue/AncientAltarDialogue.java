package plugin.dialogue;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.node.entity.player.link.SpellBookManager;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AncientAltarDialogue extends DialoguePlugin {

    public AncientAltarDialogue() {

    }

    public AncientAltarDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new AncientAltarDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        options("Switch magic book", "Switch prayer book");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        player.animate(new Animation(645));
                        player.lock(1);
                        SpellBookManager.SpellBook spellBook = player.getSpellBookManager().getSpellBook() == SpellBookManager.SpellBook.ANCIENT.getInterfaceId() ?
                            SpellBookManager.SpellBook.MODERN : SpellBookManager.SpellBook.ANCIENT;
                        player.getSpellBookManager().setSpellBook(spellBook);
                        player.getInterfaceState().openTab(Sidebar.MAGIC_TAB.ordinal(), new Component(spellBook.getInterfaceId()));
                        player.getActionSender().sendMessage("You feel a strange " + (player.getSpellBookManager().getSpellBook() != SpellBookManager.SpellBook.ANCIENT.getInterfaceId() ? "drain upon your memory" : "wisdom fill your mind") + "...");
                        end();
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        player.animate(new Animation(645));
                        player.lock(1);
                        boolean ancientCurses = player.getSavedData().getGlobalData().getPrayerBook() == 21356;
                        player.getSavedData().getGlobalData().setPrayerBook(ancientCurses ? 5608 : 21356);
                        player.getPrayer().toggleAll(true);
                        player.getInterfaceState().openTab(Sidebar.PRAYER_TAB.ordinal(), new Component(
                            player.getSavedData().getGlobalData().getPrayerBook()));
                        player.getSkills().rechargePrayerPoints();
//                        if (player.getSkills().getPrayerPoints() > 0) {
//                            double extraPrayer = player.getSkills().getPrayerPoints() * 3;
//                            extraPrayer = extraPrayer / 2;
//                            player.getSkills().incrementPrayerPoints(extraPrayer);
//                        }
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
        return new int[]{ DialogueInterpreter.getDialogueKey("AncientAltarDialogue") };
    }
}
