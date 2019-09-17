package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.global.travel.glider.GliderPulse;
import org.gielinor.game.content.global.travel.glider.Gliders;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the glider interface component.
 *
 * @author Emperor
 * @author 'Vexia
 */
public final class GliderInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(802, this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
        final Gliders glider = Gliders.forId(button);
        if (glider == null) {
            return false;
        }
        player.getPulseManager().run(new GliderPulse(1, player, glider));
        return true;
    }
}
