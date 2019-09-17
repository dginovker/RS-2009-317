package plugin.random.treespirit;

import java.nio.ByteBuffer;

import org.gielinor.game.content.anticheat.AntiMacroEvent;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.HintIconManager;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.PluginManager;

/**
 * The Tree spirit random event.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public final class TreeSpiritRandomEvent extends AntiMacroEvent {

    /**
     * Constructs a new {@code TreeSpiritRandomEvent} {@code Object}.
     */
    public TreeSpiritRandomEvent() {
        super("Tree spirit", true, false);
    }

    @Override
    public boolean start(Player player, boolean login, Object... args) {
        super.init(player);
        int level = player.getProperties().getCurrentCombatLevel();
        int npcId = 443;
        if (level < 11) {
            npcId = 438;
        } else if (level < 21) {
            npcId = 439;
        } else if (level < 41) {
            npcId = 440;
        } else if (level < 61) {
            npcId = 441;
        } else if (level < 91) {
            npcId = 442;
        }
        Location location = Location.getRandomLocation(player.getLocation(), 2, true);
        TreeSpiritNPC npc = new TreeSpiritNPC(npcId, location);
        npc.player = player;
        npc.event = this;
        npc.init();
        if (location == player.getLocation()) {
            npc.moveStep();
        }
        HintIconManager.registerHintIcon(player, npc);
        return true;
    }

    @Override
    public void terminate() {
        super.terminate();
        if (player != null) {
            player.getHintIconManager().clear();
        }
    }

    @Override
    public AntiMacroEvent create(Player player) {
        TreeSpiritRandomEvent event = new TreeSpiritRandomEvent();
        event.player = player;
        return event;
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public void configure() {
        PluginManager.definePlugin(new TreeSpiritNPC());
    }

    @Override
    public void save(ByteBuffer buffer) {

    }

    @Override
    public void parse(ByteBuffer buffer) {

    }

}