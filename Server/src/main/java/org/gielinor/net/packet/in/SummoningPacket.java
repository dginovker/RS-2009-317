package org.gielinor.net.packet.in;

import java.util.List;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InventoryInterfaceContext;
import org.gielinor.net.packet.out.InventoryInterface;

/**
 * Represents the incoming summoning packet.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SummoningPacket implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder packetBuilder) {
        byte index = (byte) packetBuilder.get();
        if (!player.getFamiliarManager().hasFamiliar() || player.getFamiliarManager().getFamiliar().getOrbOptions() == null) {
            player.getActionSender().sendMessage("You don't have a follower.");
            return;
        }
        List<String> optionList = player.getFamiliarManager().getFamiliar().getOrbOptions();
        String option = optionList.get(index);
        if (option == null || option.isEmpty()) {
            return;
        }
        // TODO Special attack => "Cast <green>X
        switch (option.toLowerCase()) {
            case "follower details":
                player.getInterfaceState().openTab(Sidebar.SUMMONING_TAB, new Component(25904));
                player.getInterfaceState().setCurrentTabIndex(Sidebar.SUMMONING_TAB.ordinal());
                player.getActionSender().sendSidebarTab(Sidebar.SUMMONING_TAB.ordinal());
                break;

            case "special attack":
                player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(player));
                break;

            case "call follower":
                player.getFamiliarManager().getFamiliar().call();
                break;

            case "dismiss follower":
                player.getDialogueInterpreter().open("dismiss_dial");
                break;

            case "take bob":
                if (!player.getFamiliarManager().getFamiliar().isBurdenBeast()) {
                    player.getActionSender().sendMessage("Your familiar is not a beast of burden.");
                    break;
                }
                BurdenBeast burdenBeast = (BurdenBeast) player.getFamiliarManager().getFamiliar();
                if (burdenBeast.getContainer().isEmpty()) {
                    player.getActionSender().sendMessage("Your familiar is not carrying any items.");
                    break;
                }
                burdenBeast.withdrawAll();
                break;

            case "renew familiar": // TODO

                break;

            case "select left-click option":
                Component component = new Component(25951).setCloseEvent(new CloseEvent() {

                    @Override
                    public void close(Player player, Component component) {
                        player.getInterfaceState().openDefaultTabs();
                    }

                    @Override
                    public boolean canClose(Player player, Component component) {
                        return true;
                    }
                });
                player.getInterfaceState().removeTabs(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
                player.getInterfaceState().setOverlay(component);
                PacketRepository.send(InventoryInterface.class, new InventoryInterfaceContext(player, null, component));
                //openTab(Sidebar.INVENTORY_TAB, component);
                break;
        }
    }
}

