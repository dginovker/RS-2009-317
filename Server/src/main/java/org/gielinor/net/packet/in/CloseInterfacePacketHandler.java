package org.gielinor.net.packet.in;

import java.util.List;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.content.dialogue.DialogueAction;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.request.trade.TradeModule;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles clicking on most buttons in the interface.
 *
 * @author Graham Edgecombe
 */
public class CloseInterfacePacketHandler implements IncomingPacket {

    private static final Logger log = LoggerFactory.getLogger(CloseInterfacePacketHandler.class);

    @Override
    public void decode(final Player player, int opcode, PacketBuilder packet) {
        player.debug("CloseInterfacePacketHandler[" + opcode + "]");
        int interfaceId = -1;
        switch (opcode) {
            /*
             * Statement continue.
             */
            case 40:
                interfaceId = packet.getShort();
                // TODO 317 Dialogue actions
                if (player.getDialogueInterpreter().getDialogue() == null
                    && player.getDialogueInterpreter().getDialogueStage() == null) {
                    player.getInterfaceState().closeChatbox();
                    List<DialogueAction> actions = player.getDialogueInterpreter().getActions();
                    if (actions.size() > 0) {
                        DialogueAction action = actions.get(0);
                        action.handle(player, null);
                        actions.remove(action);
                        actions.clear();
                    }
                    break;
                }
                if (player.getDialogueInterpreter().handle(interfaceId, -1)) {
                    break;
                }
                if (!player.getDialogueInterpreter().handle(interfaceId, null)) {
                    player.getDialogueInterpreter().getDialogue().end(); // TODO 317 End?
                    player.getInterfaceState().closeChatbox();
                    break;
                }
                break;
            /*
             * Game interface.
             */
            case 130:
                interfaceId = packet.getShort();
                if (player.isDebug()) {
                    log.debug("interfaceId={}", interfaceId);
                }
                // TODO: Fix CloseEvent for Components!
                // TODO Add to plugin
                if (interfaceId == 27135) {
                    if (player.getSavedData().getGlobalData().getStarterPackage() == null) {
                        player.getActionSender().sendMessage("Please select a starter package.");
                        player.getInterfaceState().open(new Component(27135));
                        break;
                    }
                }
                if (interfaceId == 23057) {
                    player.getInterfaceState().close();
                    player.removeAttribute("TITLES_PAGE");
                    player.removeAttribute("TITLE_CATEGORY");
                    player.removeAttribute("TITLE_VIEW");
                    player.removeAttribute("PERKS_PAGE");
                    player.removeAttribute("PERK_VIEW");
                }
                final TradeModule module = TradeModule.getExtension(player);
                if (module != null) {
                    module.decline();
                    break;
                }
                ActivityManager.handleDeclines(player);
                player.getInterfaceState().close();
                break;
        }
    }

}
