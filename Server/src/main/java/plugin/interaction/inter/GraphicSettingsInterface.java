package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Settings;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The {@link org.gielinor.game.component.ComponentPlugin} for the Graphic Settings interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GraphicSettingsInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(23580, this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
        Settings settings = player.getSettings();

        if (button >= 24883 && button <= 24905) {
            switch (button) {
                case 24883:
                    settings.setHitIcons(!settings.isHitIcons());
                    return true;
                case 24885:
                    settings.setHealthBar(!settings.isHealthBar());
                    return true;
                case 24887:
                    settings.setTweening(!settings.isTweening());
                    return true;
                case 24889:
                    settings.toggleGameframe();
                    return true;
                case 24891:
                    settings.setHitmarks(!settings.isHitmarks());
                    return true;
                case 24893:
                    settings.setCursors(!settings.isCursors());
                    return true;
//                case 24901:
//                    settings.setHdTextures(!settings.isHdTextures());
//                    return true;
                case 24897:
                    settings.setFog(!settings.isFog());
                    return true;
                case 24899:
                    settings.setContextMenu(!settings.isContextMenu());
                    return true;
            }
        }
        int index = 0;
        for (int buttonId = 23586; buttonId <= 23594; buttonId++) {
            if (button == buttonId) {
                settings.setFrameMode((index % 3));
                return true;
            }
            index++;
        }
        return false;
    }
}
