package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the PK shop.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PKShopPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(6537).getConfigurations().put("option:talk-to", this);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
// TODO PK Shop
        }
        return true;
    }
}
