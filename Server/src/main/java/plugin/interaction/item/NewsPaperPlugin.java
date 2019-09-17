package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the news paper.
 *
 * @author 'Vexia
 * TODO 317
 */
public class NewsPaperPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        int id = ((Item) node).getId();
        if (id == 11169) {
            player.getInterfaceState().open(new Component(530));
            final String page1 = "Varrock gets Makeover!          The city of Varrock is the latest recipient of a complete makeover. When interviewed, King Roald said, 'In order to keep visitors coming to see the sights of our beautiful capital, we felt that tidying-up the city would be more effective than just issuing  a decree - make sure you visit the new museum while you are here.'";
            final String page2 = "Obituaries     Goblin-Died Giant Rat-Died Unicorn-Died Varrock Guard-Died Varrock Guard-Died Bear-Died.                                          Classifieds.";
            player.getActionSender().sendString498(page1, 530, 2);
            player.getActionSender().sendString498(page2, 530, 4);
            return true;
        } else {
            player.getDialogueInterpreter().open(70099, "Come to the Al Kharid Market place! Highquality", "produce at low, low prices! Show this flyer to a", "merchant for money off your next purchase,", "courtesy of Ali Morrisane!");
        }
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(7922).getConfigurations().put("option:read", this);
        ItemDefinition.forId(11169).getConfigurations().put("option:read", this);
        return this;
    }
}
