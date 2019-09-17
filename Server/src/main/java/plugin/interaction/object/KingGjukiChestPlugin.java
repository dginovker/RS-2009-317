package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.quest.impl.TheLostKingdom;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

import plugin.npc.GjukiChestGuardNPC;

/**
 * Represents the chest on Jatizso, behind King Gjuki's throne.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class KingGjukiChestPlugin extends OptionHandler {

    /**
     * The Varrock Palace deed.
     */
    public static final Item VARROCK_PALACE_DEED = new Item(11953);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(21299).getConfigurations().put("option:open", this);
        return this;
    }


    @Override
    public boolean handle(final Player player, Node node, final String option) {
        if (player.getQuestRepository().getQuest(TheLostKingdom.NAME).getStage() != 30) {
            return false;
        }
        if (player.hasItem(VARROCK_PALACE_DEED)) {
            player.getActionSender().sendMessage("You already have the deed!");
            return true;
        }
        if (player.getQuestRepository().getQuest(TheLostKingdom.NAME).getStage() == 30 &&
            player.getAttribute("gjuki-guard-dead", false)) {
            if (player.getInventory().freeSlots() == 0) {
                player.getActionSender().sendMessage("You don't have enough room in your inventory for that!");
                return true;
            }
            player.getActionSender().sendMessage("You take the Varrock Palace deed!");
            player.getInventory().add(VARROCK_PALACE_DEED);
            return true;
        }
        if (player.getAttribute("gjuki-guard") != null || player.hasItem(VARROCK_PALACE_DEED)) {
            return false;
        }
        GjukiChestGuardNPC gjukiChestGuardNPC = new GjukiChestGuardNPC(5517, new Location(player.getLocation().getX(), 3801, 0));
        gjukiChestGuardNPC.setAttribute("player", player);
        player.setAttribute("gjuki-guard", true);
        gjukiChestGuardNPC.init();
        gjukiChestGuardNPC.sendChat("HALT! Keep your hands off of that chest!");
        gjukiChestGuardNPC.attack(player);
        World.submit(new Pulse(20) {

            @Override
            public boolean pulse() {
                if (player == null || !player.isActive() || gjukiChestGuardNPC == null || !gjukiChestGuardNPC.isActive()) {
                    gjukiChestGuardNPC.clear();
                    return true;
                }
                if (player.getLocation().getDistance(gjukiChestGuardNPC.getLocation()) > 20) {
                    gjukiChestGuardNPC.clear();
                    return true;
                }
                return false;
            }
        });
        return true;
    }

}
