package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.login.LoginConfiguration;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the plugin used for the login interface.
 *
 * @author 'Vexia
 */
public final class LoginInterfacePlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(15244, this);
        return null;
    }

    @Override
    public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
        player.getPulseManager().run(new Pulse(1) {

            @Override
            public boolean pulse() {
                LoginConfiguration.configureGameWorld(player);
                return true;
            }
        });
        return true;
    }

}
