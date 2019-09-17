package plugin.interaction.item.withitem;

import org.gielinor.game.content.global.LightSource;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.firemaking.FiremakingPulse;
import org.gielinor.game.content.skill.member.slayer.Equipment;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.zone.impl.DarkZone;
import org.gielinor.net.packet.in.ItemOnItemPacketHandler;
import org.gielinor.rs2.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the plugin used to ignite a light source.
 *
 * @author 'Vexia
 * @version 1.0
 */
public class LightSourcePlugin extends UseWithHandler {

    private static final Logger log = LoggerFactory.getLogger(LightSourcePlugin.class);

    /**
     * Constructs a new {@code LightSourcePlugin.java} {@code Object}.
     */
    public LightSourcePlugin() {
        super(36, 596, 4527, 4522, 4535, 4544, 4015, 5014, Equipment.UNLIT_BUG_LANTERN.getItem().getId());
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(FiremakingPulse.TINDERBOX.getId(), ITEM_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent nodeUsageEvent) {
        if (nodeUsageEvent.getOpcode() == ItemOnItemPacketHandler.ITEM_ON_GROUND_ITEM) {
            return false;
        }
        final Player player = nodeUsageEvent.getPlayer();
        Item item = (Item) nodeUsageEvent.getUsedWith();
        int light = item.getId();
        if (light == FiremakingPulse.TINDERBOX.getId()) {
            light = nodeUsageEvent.getUsedItem().getId();
        }
        final LightSource source = LightSource.forId(light);
        if (source == null) {
            log.warn("Light source unhandled: [{}].", light);
            return true;
        }
        if (source.getLevel() > player.getSkills().getLevel(Skills.FIREMAKING)) {
            player.getActionSender().sendMessage("You need a firemaking level of at least " + source.getLevel() + " to light this.");
            return true;
        }
        player.getInventory().replace(source.getProduct(), (nodeUsageEvent.getUsedItem().getId() == FiremakingPulse.TINDERBOX.getId() ? ((Item) nodeUsageEvent.getUsedWith()).getSlot() : nodeUsageEvent.getUsedItem().getSlot()));
        DarkZone.checkDarkArea(player);
        return true;
    }

    @Override
    public boolean isWalks() {
        return false;
    }

}
