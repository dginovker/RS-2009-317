package org.gielinor.net.packet.in;

import java.util.List;

import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.ai.AIPlayer;
import org.gielinor.game.node.entity.player.link.SpellBookManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.GameConstants;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.PlayerContext;
import org.gielinor.net.packet.out.ClearMinimapFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.interaction.item.BanHammerPlugin;
import plugin.interaction.item.RubberChickenPlugin;

/**
 * Represents the player option packet.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class PlayerOptionPacketHandler implements IncomingPacket {

    private static final Logger log = LoggerFactory.getLogger(PlayerOptionPacketHandler.class);

    /**
     * Represents the first click option opcode.
     */
    private static final int OPTION_1 = 128;

    /**
     * Represents the second click option opcode.
     */
    private static final int OPTION_2 = 73;

    /**
     * Represents the third click option opcode.
     */
    private static final int OPTION_3 = 139;

    /**
     * Represents the fourth click option opcode.
     */
    private static final int OPTION_4 = 153;

    /**
     * Represents the fourth click option opcode.
     */
    private static final int OPTION_5 = 39;

    /**
     * Represents the interface on player opcode.
     */
    private static final int OPTION_SPELL = 249;

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        if (player == null) {
            return;
        }
        if (player.getLocks().isInteractionLocked() || !player.getInterfaceState().close()) {
            return;
        }
        if (!(player instanceof AIPlayer) && player.getLocks().isMovementLocked() || !player.getInterfaceState().close() ||
            !player.getInterfaceState().closeSingleTab() || !player.getDialogueInterpreter().close()) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            player.debug("[WalkPacket] did not handle - [locked=" + player.getLocks().isMovementLocked() + "]!");
            return;
        }
        player.getInterfaceState().closeChatbox();
        int optionIndex = -1;
        int index = -1;
        switch (packet.opcode()) {
            case OPTION_1:
                optionIndex = 0;
                index = packet.getShort() & 0xFFFF;
                break;
            case OPTION_4:
                optionIndex = 1;
                index = packet.getLEShort() & 0xFFFF;
                break;
            case OPTION_2:
                optionIndex = 2;
                index = packet.getLEShort() & 0xFFFF;
                break;
            case OPTION_3:
                optionIndex = 3;
                index = packet.getLEShort() & 0xFFFF;
                break;
            case OPTION_SPELL:
                index = packet.getShort() - 128;
                int spellId = packet.getLEShort() & 0xFFFF;
                final Player target = Repository.getPlayers().get(index);
                player.getActionSender().sendDebugPacket(opcode, "PlayerSpell", "Spell ID: " + spellId, "Index: " + index);
                if (target == null || !player.getLocation().withinDistance(target.getLocation())) {
                    PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
                    break;
                }
                ComponentDefinition componentDefinition = ComponentDefinition.forId(spellId);
                if (componentDefinition == null) {
                    log.info("[{}] used invalid interface spell: {}.", player.getName(), spellId);
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
                    player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(target));
                    break;
                }
                if (spellId == 25943) {
                    if (!player.getFamiliarManager().hasFamiliar()) {
                        player.getActionSender().sendMessage("You don't have a familiar.");
                        break;
                    }
                    player.getFamiliarManager().attack(target);
                    break;
                }
                switch (interfaceId) {
                    case 1151:
                        MagicSpell.castSpell(player, SpellBookManager.SpellBook.MODERN, spellId, target);
                        break;
                    case 12855:
                        MagicSpell.castSpell(player, SpellBookManager.SpellBook.ANCIENT, spellId, target);
                        break;
                    case 29999:
                        MagicSpell.castSpell(player, SpellBookManager.SpellBook.LUNAR, spellId, target);
                        break;
                }
                break;

        }
        if (index != -1 && optionIndex >= 0) {
            handlePlayerInteraction(player, optionIndex, index);
        }
    }

    /**
     * Handles player interaction.
     *
     * @param player      The   player interacting.
     * @param optionIndex The option index.
     * @param index       The target index.
     */
    private static void handlePlayerInteraction(Player player, int optionIndex, int index) {
        if (index < 1 || index > GameConstants.MAX_PLAYERS) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            return;
        }
        final Player target = Repository.getPlayers().get(index);
        if (target == null || !target.isActive()) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            return;
        }
        Option option = target.getInteraction().get(optionIndex);
        if (option == null && player.getEquipment().contains(4566) && optionIndex == 1) {
            option = RubberChickenPlugin.RubberChickenEquipPlugin.WHACK;
        } else if (option == null && player.getEquipment().contains(Item.BAN_HAMMER) && optionIndex == 1) {
            option = BanHammerPlugin.BanHammerEquipPlugin.SMASH;
        }
        if (option == null) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            return;
        }
        handleAIPLegion(player, 2, optionIndex, index);
        if (!option.getName().equals("Control")) {
            player = getPlayer(player);
        }
        target.getInteraction().handle(player, option);
    }

    /**
     * Handles the AIPlayer legion.
     *
     * @param player The player.
     * @param args   The arguments.
     */
    private static void handleAIPLegion(Player player, int... args) {
        if (player.isArtificial()) {
            List<AIPlayer> legion = player.getAttribute("aip_legion");
            if (legion != null) {
                for (AIPlayer aip : legion) {
                    if (aip != player) {
                        handlePlayerInteraction(aip, args[0], args[1]);
                    }
                }
            }
        }
    }

    /**
     * Gets the player instance (used for AIP controlling).
     *
     * @param player The player.
     * @return The player instance, or the AIP when the player is controlling an AIP.
     */
    public static Player getPlayer(Player player) {
        if (player == null) {
            return null;
        }
        if (player.getAttribute("aip_select") == null) {
            return player;
        }
        AIPlayer aip = player.getAttribute("aip_select");
        if (aip != null && aip.getLocation().withinDistance(player.getLocation())) {
            if (!player.getAttribute("aip_perm_select", true)) {
                player.removeAttribute("aip_select");
            }
            return aip;
        }
        Player control = player.getAttribute("control_player");
        if (control != null && control.getLocation().withinDistance(player.getLocation())) {
            return control;
        }
        return player;
    }

}
