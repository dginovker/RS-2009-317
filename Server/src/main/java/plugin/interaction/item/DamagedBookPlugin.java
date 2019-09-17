package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} for Damaged God books.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DamagedBookPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(3839).getConfigurations().put("option:check", this);
        ItemDefinition.forId(3841).getConfigurations().put("option:check", this);
        ItemDefinition.forId(3843).getConfigurations().put("option:check", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        // "You add the page to the book..."
        //  "The book is now complete!"
        // Zamorak:
        //"You can now use it to bless unholy symbols!"
        // Guthix:
        // "You can now use it to bless unblessed holy symbols!"
        // Saradomin:
        //"You can now use it to bless holy symbols!"

//        "Select a relevant passage"
//        ZAM:
//        "Wedding Ceremony"
//        "Last Rites",
//                "Blessing",
//                "Preach"
//
//        GUTH:
//        "Wedding Ceremony"
//        "Last Rites",
//                "Blessing",
//                "Preach"
//
//        SARA:
//        "Partnerships",
//                "Last Rites",
//                "Blessings",
//                "Preach"


        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }
}
