package plugin.activity.barrow;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.PluginManifest;
import org.gielinor.rs2.plugin.PluginType;

/**
 * The tunnel entrance dialogue handling plugin.
 *
 * @author Emperor
 */
@PluginManifest(type = PluginType.DIALOGUE)
public final class TunnelEntranceDialogue extends DialoguePlugin {

    /**
     * The crypt index.
     */
    @SuppressWarnings("unused")
    private int index;

    /**
     * Constructs a new {@code TunnelEntranceDialogue} {@code Object}.
     */
    public TunnelEntranceDialogue() {
        super();
    }

    /**
     * Constructs a new {@code TunnelEntranceDialogue} {@code Object}.
     *
     * @param player The player.
     */
    public TunnelEntranceDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new TunnelEntranceDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        this.index = (Integer) args[0];
        player.getDialogueInterpreter().sendPlaneMessage(false, "You find a hidden tunnel, do you want to enter?");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        if (stage++ == 0) {
            player.getDialogueInterpreter().sendOptions(null, "Yeah, I'm fearless!", "No way, that looks scary!");
            return true;
        }
        switch (optionSelect) {
            case TWO_OPTION_ONE:
                int offsetX = 0;
                int offsetY = 0;
                int configValue = player.getConfigManager().get(452);
                if ((configValue & (1 << 7)) != 0) {
                    offsetX = 34;
                    offsetY = 34;
                } else if ((configValue & (1 << 6)) != 0) {
                    offsetY = 34;
                } else if ((configValue & (1 << 9)) != 0) {
                    offsetX = 34;
                }
                int x = 3534 + offsetX;
                int y = 9677 + offsetY;
                player.getProperties().setTeleportLocation(Location.create(x, y, 0));
            case TWO_OPTION_TWO:
                end();
                return true;
        }
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("barrow_tunnel") };
    }
}
