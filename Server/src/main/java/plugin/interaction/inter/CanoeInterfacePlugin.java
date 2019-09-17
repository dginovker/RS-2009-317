package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.global.travel.canoe.Canoe;
import org.gielinor.game.content.global.travel.canoe.CanoeExtension;
import org.gielinor.game.content.global.travel.canoe.CanoeStation;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the canoe interface plugins.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CanoeInterfacePlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(18178, this);
        ComponentDefinition.put(53, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        final CanoeExtension extension = CanoeExtension.extension(player);
        switch (component.getId()) {
            case 18178:
                final Canoe canoe = Canoe.forChild(button);
                if (canoe == null) {
                    return false;
                }
                extension.craft(canoe);
                return true;
            case 18220:
                final CanoeStation station = CanoeStation.forButton(button);
                if (station == null) {
                    return false;
                }
                if (extension.getStage() < 3) {
                    return false;
                }
                extension.travel(station);
                return true;
        }
        return false;
    }

}
