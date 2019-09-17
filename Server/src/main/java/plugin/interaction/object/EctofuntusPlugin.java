package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.free.prayer.Ectofuntus;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The ectofuntus plugin.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class EctofuntusPlugin extends OptionHandler {

    @Override
    public boolean handle(final Player player, Node node, String option) {
        Ectofuntus.handleObjects(player, node.getId());
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(16648).getConfigurations().put("option:worship", this);
        ObjectDefinition.forId(16113).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(16110).getConfigurations().put("option:climb-up", this);
        ObjectDefinition.forId(16526).getConfigurations().put("option:jump-down", this);
        ObjectDefinition.forId(16525).getConfigurations().put("option:jump-up", this);
        ObjectDefinition.forId(Ectofuntus.GRINDER).getConfigurations().put("option:wind", this);
        ObjectDefinition.forId(Ectofuntus.GRINDER).getConfigurations().put("option:status", this);
        ObjectDefinition.forId(Ectofuntus.BIN).getConfigurations().put("option:empty", this);
        return this;
    }

}
