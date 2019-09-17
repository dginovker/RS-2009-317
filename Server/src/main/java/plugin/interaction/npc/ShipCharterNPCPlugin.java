package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.global.travel.ship.ShipCharter;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to handle the "charter" option.
 * @author 'Vexia
 * @version 1.0
 */
public final class ShipCharterNPCPlugin extends OptionHandler {

    /**
     * Represents the ship charter npcs.
     */
    private static final int[] IDS = new int[]{ 4650, 4651, 4652, 4653, 4654, 4655, 4656 };

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int id : IDS) {
            NPCDefinition.forId(id).getConfigurations().put("option:charter", this);
        }
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        ShipCharter.open(player);
        return true;
    }

}
