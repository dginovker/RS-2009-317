package plugin.dialogue;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * @author 'Vexia
 */
public class VarrockBrokenCart extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getDialogueInterpreter().open(70099, "You search the cart but are surprised to find very little there. It's a", "little odd for a travelling trader not to have anything to trade.");
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(23055).getConfigurations().put("option:search", this);
        return this;
    }

}
