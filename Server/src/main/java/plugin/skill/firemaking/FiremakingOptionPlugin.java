package plugin.skill.firemaking;

import org.gielinor.game.content.skill.free.firemaking.FiremakingPulse;
import org.gielinor.game.content.skill.free.firemaking.Log;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.in.ItemOnItemPacketHandler;
import org.gielinor.rs2.plugin.Plugin;

import java.util.Arrays;

/**
 * Represents the plugin used to handle the lighting of a fire.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FiremakingOptionPlugin extends UseWithHandler {

    /**
     * Constructs a new {@code FireMakingOptionPlugin} {@code Object}.
     */
    public FiremakingOptionPlugin() {
        super(Arrays.stream(Log.values()).mapToInt(Log::getLogId).toArray());
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(FiremakingPulse.TINDERBOX.getId(), ITEM_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        Item item = event.getUsedItem();
        if (item == null) {
            return false;
        }
        if (event.getOpcode() == ItemOnItemPacketHandler.ITEM_ON_GROUND_ITEM) {
            return false;
        }
        Player player = event.getPlayer();
        player.getPulseManager().run(new FiremakingPulse(player, item, null));
        return true;
    }

}
