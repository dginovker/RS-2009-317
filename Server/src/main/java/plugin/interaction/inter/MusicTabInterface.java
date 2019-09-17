package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.music.MusicEntry;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The {@link org.gielinor.game.component.ComponentPlugin} for the music tab.
 *
 * @author <a href="http://Gielinor.org>Gielinor</a>
 */
public final class MusicTabInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(962, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int buttonId, int slot, int itemId) {
        if (buttonId == -1) {
            return false;
        }
        MusicEntry musicEntry = MusicEntry.forButtonId(buttonId);
        if (musicEntry == null) {
            return false;
        }
        musicEntry = player.getMusicPlayer().getUnlocked().get(musicEntry.getIndex());
        if (musicEntry == null) {
            player.getActionSender().sendMessage("You have not unlocked this piece of music yet!");
            return true;
        }
        player.getMusicPlayer().play(musicEntry);
        return true;
    }

}