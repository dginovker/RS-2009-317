package org.gielinor.net.packet.in;

import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.GameConstants;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.PlayerContext;
import org.gielinor.net.packet.out.ClearMinimapFlag;
import org.gielinor.rs2.pulse.impl.MovementPulse;

/**
 * Handles the Interface "Use" on packets.
 *
 * @author Stacx
 */
public class InterfaceUseOnPacketHandler implements IncomingPacket {

    @SuppressWarnings("unused")
    @Override
    public void decode(final Player player, int opcode, PacketBuilder packet) {
        int payload;
        int interfaceId;
        int componentId;
        int itemId;
        int x;
        int y;
        switch (packet.opcode()) {
            case 55: // Interface On GroundItem
                payload = packet.getIntB();
                componentId = payload & 0xFF;
                interfaceId = payload >> 16;
                x = packet.getLEShort();
                y = packet.getShortA();
                final int spell = packet.getLEShort();
                itemId = packet.getShort();
                final Item groundItem = GroundItemManager.get(itemId, Location.create(x, y, player.getLocation().getZ()), player);
                if (groundItem == null || !player.getLocation().withinDistance(groundItem.getLocation())) {
                    break;
                }
                if (player.getAttribute("magic:delay", -1) > World.getTicks()) {
                    break;
                }
                if (player.getZoneMonitor().clickButton(interfaceId, componentId, spell, itemId, opcode)) {
                    break;
                }
                if (CombatSwingHandler.isProjectileClipped(player, groundItem, false)) {
                    MagicSpell.castSpell(player, SpellBook.MODERN, spell, groundItem);
                } else {
                    player.getPulseManager().run(new MovementPulse(player, groundItem) {

                        @Override
                        public boolean update() {
                            if (CombatSwingHandler.isProjectileClipped(player, groundItem, false)) {
                                super.destination = player.getLocation();
                            }
                            boolean finished = super.update();
                            if (finished) {
                                player.getWalkingQueue().reset();
                            }
                            return finished;

                        }

                        @Override
                        public boolean pulse() {
                            MagicSpell.castSpell(player, SpellBook.MODERN, spell, groundItem);
                            return true;
                        }
                    }, "movement");
                }
                break;
            case 139: // Interface On Player
                int targetIndex = packet.getShort();
                payload = packet.getIntA();
                interfaceId = payload >> 16;
                componentId = payload & 0xFFFF;
                //			Logger.log("Interface:" + interfaceId+ " Component:" + componentId + " Target Index:"+ targetIndex);
                final Player target = Repository.getPlayers().get(targetIndex);
                if (target == null || !player.getLocation().withinDistance(target.getLocation())) {
                    PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
                    break;
                }
                switch (interfaceId) {
                    case 192:
                        MagicSpell.castSpell(player, SpellBook.MODERN, componentId, target);
                        break;
                    case 193:
                        MagicSpell.castSpell(player, SpellBook.ANCIENT, componentId, target);
                        break;
                    case 430:
                        MagicSpell.castSpell(player, SpellBook.LUNAR, componentId, target);
                        break;
                    case 662:
                        switch (componentId) {
                            case 67:
                            case 69:
                            case 119:
                            case 121:
                            default:
                                if (!player.getFamiliarManager().hasFamiliar()) {
                                    player.getActionSender().sendMessage("You don't have a familiar.");
                                } else {
                                    player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(target));
                                }
                                break;
                        }
                        break;
                    default:
                        player.debug("Option usage [inter=" + interfaceId + ", child=" + componentId + ", target=" + target + "].");
                }
                break;
            case 35: // Interface On Object
                x = packet.getLEShort();
                componentId = packet.getShortA();
                y = packet.getShortA();
                int objectId = packet.getLEShort();
                interfaceId = player.getSpellBookManager().getSpellBook();
                if (componentId >= 30000 && componentId <= 30350) {
                    interfaceId = 29999;
                }
                player.getActionSender().sendDebugPacket(packet.opcode(), "InterfaceOnItem", "X: " + x,
                    "Y: " + y,
                    "Object ID: " + objectId,
                    "Interface ID: " + interfaceId,
                    "Spell ID: " + componentId);
                GameObject object = RegionManager.getObject(player.getLocation().getZ(), x, y);
                if (object == null) {
                    object = RegionManager.getObject(Location.create(x, y, 0));
                }
                if (object != null) {
                    object = object.getChild(player);
                }
                if (object == null || (object.getId() != objectId && object.getWrapper().getId() != objectId) || !player.getLocation().withinDistance(object.getLocation())) {
                    PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
                    break;
                }
                if (componentId == 25942) {
                    player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(object));
                    break;
                }
                switch (interfaceId) {
                    case 29999:
                        MagicSpell.castSpell(player, SpellBook.LUNAR, componentId, object);
                        break;
                    case 1151:
                        MagicSpell.castSpell(player, SpellBook.MODERN, componentId, object);
                        break;
                }
                player.debug("Option usage [child=" + componentId + ", target=" + objectId + "].");
                break;
            case 248: // Interface On NPC
                int index = packet.getLEShortA();
                if (index < 1 || index > GameConstants.MAX_NPCS) {
                    PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
                    break;
                }
                int i_54_ = packet.getShortA();
                payload = packet.getIntA();
                componentId = payload & 0xFFFF;
                interfaceId = (payload >> 16) & 0xFFFF;
                NPC npc = Repository.getNpcs().get(index);
                if (npc == null || !player.getLocation().withinDistance(npc.getLocation())) {
                    PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
                    break;
                }
                switch (interfaceId) {
                    case 430:
                        MagicSpell.castSpell(player, SpellBook.LUNAR, componentId, npc);
                        break;
                    case 192:
                        MagicSpell.castSpell(player, SpellBook.MODERN, componentId, npc);
                        break;
                    case 193:
                        MagicSpell.castSpell(player, SpellBook.ANCIENT, componentId, npc);
                        break;
                    case 662:
                        switch (componentId) {
                            case 67:
                            case 69:
                            case 177:
                            case 121:
                            case 119:
                            default:
                                if (!player.getFamiliarManager().hasFamiliar()) {
                                    player.getActionSender().sendMessage("You don't have a familiar.");
                                } else {
                                    player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(npc));
                                }
                                break;
                        }
                        break;
                    default:
                        player.debug("Option usage [inter=" + interfaceId + ", child=" + componentId + ", target=" + npc + "].");
                }
                break;
            case 251: // Interface On Item
                int itemSlot = packet.getLEShortA();
                payload = packet.getInt();
                interfaceId = payload >> 16;
                componentId = payload & 0xFFFF;
                itemId = packet.getLEShortA();
                int unknown = packet.getShortA();
                if (itemSlot < 0 || itemSlot > 27) {
                    break;
                }
                Item item = player.getInventory().get(itemSlot);
                if (item == null) {
                    break;
                }
                switch (interfaceId) {
                    case 430:
                        if (player.getAttribute("magic:delay", -1) > World.getTicks()) {
                            break;
                        }
                        MagicSpell.castSpell(player, SpellBook.LUNAR, componentId, item);
                        break;
                    case 192:
                        if (player.getAttribute("magic:delay", -1) > World.getTicks()) {
                            break;
                        }
                        MagicSpell.castSpell(player, SpellBook.MODERN, componentId, item);
                        break;
                    case 662:
                        if (player.getFamiliarManager().hasFamiliar()) {
                            player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(item, interfaceId, componentId, item));
                        } else {
                            player.getActionSender().sendMessage("You don't have a follower.");
                        }
                        break;
                    default:
                        player.debug("Option usage [inter=" + interfaceId + ", child=" + componentId + ", target=" + item + "].");
                }
                break;
        }
    }
}
