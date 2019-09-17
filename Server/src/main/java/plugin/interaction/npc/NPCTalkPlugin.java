package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.MovementPulse;

/**
 * Handles the NPC talk-to option.
 *
 * @author Emperor
 */
public final class NPCTalkPlugin extends OptionHandler {

    @Override
    public Location getDestination(Node n, Node node) {
        NPC npc = (NPC) node;
        if (npc.getAttribute("facing_booth", false)) {
            int offsetX = npc.getDirection().getStepX() << 1;
            int offsetY = npc.getDirection().getStepY() << 1;
            return npc.getLocation().transform(offsetX, offsetY, 0);
        }
        if (npc.getLocation().inArea(new ZoneBorders(2326, 3685, 2333, 3688))) {
            return new Location(npc.getLocation().getX(), 3688, 0);
        }
        if (npc.getLocation().equals(2847, 5105, 0) || npc.getLocation().equals(2848, 5105, 0)) {
            return npc.getLocation().transform(0, -2, 0); // home bankers
        }
        return null;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        final NPC npc = (NPC) node;
        if (!npc.getDefinition().hasAttackOption() && player.getAttribute("REMOVE_NPCS", 0) == 1) {
            npc.clear();
            return true;
        }
        // Random events.
        if (npc.getAttribute("RANDOM_EVENT_PLAYER") != null && player != npc.getAttribute("RANDOM_EVENT_PLAYER")) {
            player.getActionSender().sendMessage("They do not wish to talk to you.");
            return true;
        }
        if (!npc.getAttribute("facing_booth", false)) {
            npc.faceLocation(player.getLocation());
        }
        if (npc.getLocation().inArea(new ZoneBorders(2065, 3887, 2130, 3937)) && !player.getEquipment().contains(9083)) {
            player.getInterfaceState().close();
            player.lock(7);
            World.submit(new Pulse(2) {

                int count = 0;

                @Override
                public boolean pulse() {
                    this.setDelay(1);
                    switch (count++) {
                        case 0:
                            player.getInterfaceState().openComponent(8677);
                            break;
                        case 3:
                            player.getProperties().setTeleportLocation(Location.create(2621, 3688, 0));
                            break;
                        case 5:
                            player.unlock();
                            player.getInterfaceState().close();
                            player.getInterfaceState().openDefaultTabs();
                            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                            player.getDialogueInterpreter().sendPlaneMessage(npc.getName() + " realizes you are not of the Moonclan, and sends", "you back to Rellekka!");
                            return true;
                    }
                    return false;
                }
            });
            return true;
        }
        if (player.getLocation().getDistance(node.getLocation()) <= 4 &&
            ((NPC) node).getPulseManager().getCurrent() instanceof MovementPulse) {
            ((NPC) node).getPulseManager().getCurrent().stop();
        }
        if (player.getDialogueInterpreter().open(npc.getId(), npc)) {
            return true;
        }
        Shops shop = Shops.forId(npc.getId());
        if (shop != null) {
            shop.getShop().open(player);
            return true;
        }
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.setOptionHandler("talk-to", this);
        return this;
    }
}
