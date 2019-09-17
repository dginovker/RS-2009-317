package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.content.skill.member.slayer.Master;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

import plugin.interaction.inter.SlayerRewardInterface;

/**
 * Represents the plugin used for an npc with the trade option.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class NPCTradePlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.setOptionHandler("trade", this);
        NPCDefinition.setOptionHandler("trade-with", this);
        NPCDefinition.setOptionHandler("get-rewards", this);
        NPCDefinition.setOptionHandler("rewards", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        final NPC npc = (NPC) node;
        if (option.equals("rewards")) {
            Master master = Master.forId(npc.getId());
            if (master != null) {
                player.getInterfaceState().open(new Component(24546));
                SlayerRewardInterface.configure(player, 1);
                return true;
            }
            return false;
        }
        if (npc.getId() == 4518 || npc.getId() == 4511) {
            if (!(player.getInventory().contains(9083) || player.getEquipment().contains(9083))) {
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
        }
        Shops shop = Shops.forId(npc.getId());
        if (shop != null) {
            shop.getShop().open(player);
        }
        return true;
    }

}
