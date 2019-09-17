package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.global.quest.impl.CurseOfTheUndead;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the Ancient Altar for spellbook switching.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AncientAltarPlugin extends OptionHandler {

    /**
     * Handles the interaction option.
     *
     * @param player The player who used the option.
     * @param node   The node the player selected an option on.
     * @param option The option selected.
     * @return <code>True</code> if successful.
     */
    @Override
    public boolean handle(Player player, Node node, String option) {
        if (node.getId() == 6552) {
            if (player.getQuestRepository().getQuest(CurseOfTheUndead.NAME).getStage() == 100) {
                player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("AncientAltarDialogue"));
                return true;
            }
            player.lock(1);
            player.animate(new Animation(645));
            SpellBook spellBook = player.getSpellBookManager().getSpellBook() == SpellBook.ANCIENT.getInterfaceId() ?
                SpellBook.MODERN : SpellBook.ANCIENT;
            player.getSpellBookManager().setSpellBook(spellBook);
            player.getInterfaceState().openTab(Sidebar.MAGIC_TAB.ordinal(), new Component(spellBook.getInterfaceId()));
            player.getActionSender().sendMessage("You feel a strange " + (player.getSpellBookManager().getSpellBook() != SpellBook.ANCIENT.getInterfaceId() ? "drain upon your memory" : "wisdom fill your mind") + "...");
            return true;
        }
        player.setTeleportTarget(Location.create(3233, 9313, 0));
        return true;
    }

    /**
     * Creates a new instance.
     *
     * @param arg The argument.
     * @return The plugin instance created.
     */
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(6552).getConfigurations().put("option:pray-at", this);
        ObjectDefinition.forId(6481).getConfigurations().put("option:enter", this);
        return this;
    }
}
