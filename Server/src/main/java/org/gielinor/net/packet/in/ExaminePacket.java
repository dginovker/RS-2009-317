package org.gielinor.net.packet.in;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.free.prayer.ecto.Bonemeal;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.utilities.string.TextUtils;

/**
 * Handles an examine packet.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *         TODO Current cache IDs
 */
public final class ExaminePacket implements IncomingPacket {

    /**
     * The object examine opcode.
     */
    private static final int OBJECT_EXAMINE = 211;
    /**
     * The item examine opcode.
     */
    private static final int ITEM_EXAMINE = 235;
    /**
     * The NPC examine opcode.
     */
    private static final int NPC_EXAMINE = 213;

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        switch (packet.opcode()) {
            case OBJECT_EXAMINE:
                int id = packet.getShort();
                player.getActionSender().sendDebugPacket(opcode, "ObjectExamine", "ID: " + id);
                if (id < 0 || id > ObjectDefinition.getHighestKey()) {
                    break;
                }
                ObjectDefinition objectDefinition = ObjectDefinition.forId(id);
                if (objectDefinition.getExamine() != null && !objectDefinition.getExamine().equals("null")) {
                    player.getActionSender().sendMessage(objectDefinition.getExamine());
                    break;
                }
                String name = objectDefinition.getName();
                player.getActionSender().sendMessage("It's a" + (TextUtils.isPlusN(name) ? "n " : " ") + name + ".");
                break;
            case ITEM_EXAMINE:
                id = packet.getShort();
                if (id < 0) {
                    id += 65536;
                }
                player.getActionSender().sendDebugPacket(opcode, "ItemExamine", "ID: " + id);
                if (id < 0 || ItemDefinition.forId(id) == null) {
                    break;
                }
                player.getActionSender().sendMessage(getItemExamine(player, id));
                break;
            case NPC_EXAMINE:
                id = packet.getShort();
                player.getActionSender().sendDebugPacket(opcode, "NPCExamine", "ID: " + id);
                if (id < 0 || NPCDefinition.forId(id) == null) {
                    break;
                }
                player.debug("NPC id: " + id + ".");
                NPCDefinition npcDefinition = NPCDefinition.forId(id);
                if (npcDefinition == null) {
                    break;
                }
                player.getActionSender().sendMessage(npcDefinition.getExamine());
                break;
        }
    }

    /**
     * Gets the item examine.
     *
     * @param id the id.
     * @return the name.
     */
    public static String getItemExamine(Player player, int id) {
        String examine = ItemDefinition.forId(id).getExamine();
        Item item = player.getInventory().getById(id);

        /* TODO - improve this massively when we switch to the
        deob using proper packets to determine the source of the click */

        if (item == null) {
            item = player.getBank().getById(id);
        }

        if (item != null && item.getCount() >= 100_000) {
            return TextUtils.getFormattedNumber(item.getCount()) + " x " + item.getName() + ".";
        }

        if (examine.startsWith("A pot of crushed [")) {
            Bonemeal bonemeal = Bonemeal.forMealId(id);
            examine = "A pot of crushed " + ItemDefinition.forId(bonemeal.getBoneId()).getName().toLowerCase() + ".";
        }

        return examine;

    }
}
