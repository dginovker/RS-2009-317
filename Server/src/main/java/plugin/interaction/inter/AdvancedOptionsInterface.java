package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The {@link org.gielinor.game.component.ComponentPlugin} for the advanced options interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AdvancedOptionsInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(26387, this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
        switch (button) {
            case 26393:
                player.getSettings().toggleSidePanelTransparent();
                return true;
            case 26397:
                player.getSettings().toggleRemainingXP();
                return true;
            case 26401:
                player.getSettings().toggleRoofRemoval();
                return true;
            case 26405:
                player.getSettings().toggleDataOrbs();
                return true;
            case 26409:
                player.getSettings().toggleChatboxTransparent();
                return true;
            case 26413:
                player.getSettings().toggleClickThroughChatbox();
                return true;
            case 26419:
                player.getSettings().toggleSidePanelsBottom();
                return true;
            case 26423:
                player.getSettings().toggleSidePanelHotkeys();
                return true;
        }
        return false;
    }
}
