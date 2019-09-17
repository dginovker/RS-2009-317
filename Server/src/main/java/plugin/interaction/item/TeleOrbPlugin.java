package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.perk.PerkManagement;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the Tele-orb item plugin interactions.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class TeleOrbPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (player.sendDonorNotification("to use this orb.")) {
            return true;
        }
        Item item = (Item) node;
        switch (option) {
            case "titles":
                player.getInterfaceState().open(new Component(23035));
                return true;
            case "perks":
                PerkManagement.openPage(player, 0, true);
                return true;
            case "teleports":
                player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("donor-orb-teleports"));
                return true;
        }
        return false;
    }


    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(Item.DONOR_ORB).getConfigurations().put("option:titles", this);
        ItemDefinition.forId(Item.DONOR_ORB).getConfigurations().put("option:perks", this);
        ItemDefinition.forId(Item.DONOR_ORB).getConfigurations().put("option:teleports", this);
        return null;
    }
}
