package org.gielinor.net.packet.in;

import java.util.List;

import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.interaction.Interaction;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.ai.AIPlayer;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.world.GameConstants;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.PlayerContext;
import org.gielinor.net.packet.out.ClearMinimapFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the NPC option packet.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class NPCOptionPacketHandler implements IncomingPacket {

    private static final Logger log = LoggerFactory.getLogger(NPCOptionPacketHandler.class);

    /**
     * Represents the attack option opcode.
     */
    private static final int ATTACK = 72;

    /**
     * Represents the first click option opcode.
     */
    private static final int OPTION_1 = 155;

    /**
     * Represents the second click option opcode.
     */
    private static final int OPTION_2 = 17;

    /**
     * Represents the third click option opcode.
     */
    private static final int OPTION_3 = 31;

    /**
     * Represents the fourth click option opcode.
     */
    private static final int OPTION_4 = 18;

    private static final int THIRD_CLICK = 21;

    /**
     * Represents the interface on NPC opcode.
     */
    private static final int OPTION_SPELL = 131;

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        if (player == null) {
            return;
        }
        player.debug("NPCOptionPacketHandler[opcode]: " + opcode);
        if (player.getLocks().isInteractionLocked() || !player.getInterfaceState().close()) {
            if (player.getAttribute("REMOVE_NPC") == null) {
                return;
            }
        }
        if (!(player instanceof AIPlayer) && player.getLocks().isMovementLocked() || !player.getInterfaceState().close() ||
            !player.getInterfaceState().closeSingleTab() || !player.getDialogueInterpreter().close()) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            player.debug("[WalkPacket] did not handle - [locked=" + player.getLocks().isMovementLocked() + "]!");
            return;
        }
        if (player.getAttribute("REMOVE_NPC") == null) {
            player.getInterfaceState().closeChatbox();
        }
        int optionIndex = -1;
        int index = -1;
        switch (packet.opcode()) {
            case OPTION_1:
                optionIndex = 0;
                index = packet.getLEShort() & 0xFFFF;
                break;
            case ATTACK:
                optionIndex = 1;
                index = packet.getShortA() & 0xFFFF;
                break;
            case OPTION_2:
                optionIndex = 2;
                index = packet.getLEShortA() & 0xFFFF;
                break;
            case OPTION_3:
                optionIndex = 3;
                index = packet.getShort() & 0xFFFF;
                break;
            case OPTION_4:
                optionIndex = 4;
                index = packet.getLEShort() & 0xFFFF;
                break;
            case THIRD_CLICK:
                optionIndex = 3;
                index = packet.getShort() & 0xFFFF;
                break;
            case OPTION_SPELL:
                index = packet.getLEShortA();
                if (index < 1 || index > GameConstants.MAX_NPCS) {
                    PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
                    break;
                }
                int spellId = packet.getShortA();
                NPC npc = Repository.getNpcs().get(index);
                if (npc == null || !player.getLocation().withinDistance(npc.getLocation())) {
                    PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
                    break;
                }
                ComponentDefinition componentDefinition = ComponentDefinition.forId(spellId);
                if (componentDefinition == null) {
                    log.warn("Invalid interface button id: {}.", spellId);
                    break;
                }
                int interfaceId = componentDefinition.getParentId();
                if (interfaceId == -1) {
                    interfaceId = componentDefinition.getId();
                }
                if (spellId >= 30000 && spellId <= 30350) {
                    interfaceId = 29999;
                }
                if (spellId == 25942) {
                    if (!player.getFamiliarManager().hasFamiliar()) {
                        player.getActionSender().sendMessage("You don't have a familiar.");
                        break;
                    }
                    player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(npc));
                    break;
                }
                if (spellId == 25943) {
                    if (!player.getFamiliarManager().hasFamiliar()) {
                        player.getActionSender().sendMessage("You don't have a familiar.");
                        break;
                    }
                    player.getFamiliarManager().attack(npc);
                    break;
                }
                switch (interfaceId) {
                    case 29999:
                        MagicSpell.castSpell(player, SpellBook.LUNAR, spellId, npc);
                        break;
                    case 1151:
                        MagicSpell.castSpell(player, SpellBook.MODERN, spellId, npc);
                        break;
                    case 12855:
                        MagicSpell.castSpell(player, SpellBook.ANCIENT, spellId, npc);
                        break;
                    default:
                        player.debug("Option usage [inter=" + interfaceId + ", child=" + spellId + ", target=" + npc + "].");
                }
                break;
        }
        if (index != -1 && optionIndex >= 0) {
            handleNPCInteraction(player, optionIndex, index, packet.opcode());
        }
    }

    /**
     * Handles the NPC interaction.
     *
     * @param player      the player.
     * @param optionIndex The option index.
     * @param index       the index.
     */
    private static void handleNPCInteraction(Player player, int optionIndex, final int index, int opcode) {
        player = PlayerOptionPacketHandler.getPlayer(player);
        if (index < 1 || index > GameConstants.MAX_NPCS) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            return;
        }
        final NPC npc = Repository.getNpcs().get(index);
        if (npc == null) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            return;
        }
        if (player.getAttribute("REMOVE_NPC") != null) {
            player.setAttribute("REMOVE_NPC", npc);
            player.getDialogueInterpreter().open("RottenPotato");
            return;
        }
        if (player.getAttribute("removenpc", false)) {
            npc.clear();
            player.getActionSender().sendMessage("Removed npc=" + npc.toString());
            return;
        }
        NPC shown = npc.getShownNPC(player);
        Option option = shown.getInteraction().get(optionIndex);
        // TODO
        if (option == null) {
            shown.getInteraction().setDefault();
            option = shown.getInteraction().get(optionIndex);
        }
        if (option == null) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            Interaction.handleInvalidInteraction(player, npc, Option.NULL);
            return;
        }
        player.getActionSender().sendDebugPacket(opcode, "NPCOption",
            "ID: " + shown.getId(),
            "Original: " + npc.getId(),
            "Index: " + index,
            "Location: " + npc.getLocation(),
            "Option: " + option.getName(),
            "Slot: " + option.getIndex());
        if (player.getAttribute("control_npc_target") != null) {
            player.setAttribute("control_npc", npc);
            player.removeAttribute("control_npc_target");
            player.getActionSender().sendMessage("Now controlling npc : " + npc.getName() + ", " + npc.getId());
            return;
        }
        handleAIPLegion(player, opcode, optionIndex, index);
        npc.getInteraction().handle(player, option);
    }


    /**
     * Handles the AIPlayer legion.
     *
     * @param player The player.
     * @param args   The arguments.
     */
    private static void handleAIPLegion(Player player, int opcode, int... args) {
        if (player.isArtificial()) {
            List<AIPlayer> legion = player.getAttribute("aip_legion");
            if (legion != null) {
                for (AIPlayer aip : legion) {
                    if (aip != player) {
                        handleNPCInteraction(aip, args[0], args[1], opcode);
                    }
                }
            }
        }
    }

    /**
     * Gets the NPC instance (used for AIP controlling).
     *
     * @param player The player.
     * @return The npc instance.
     */
    public static NPC getNPC(Player player) {
        NPC control = player.getAttribute("control_npc");
        if (control != null && control.getLocation().withinDistance(player.getLocation())) {
            return control;
        }
        return null;
    }

}
