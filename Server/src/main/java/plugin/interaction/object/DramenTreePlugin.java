package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.free.gather.SkillingTool;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the Dramen tree plugin.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DramenTreePlugin extends OptionHandler {

    /**
     * Handles the interaction option.
     *
     * @param player The player who used the option.
     * @param node   The node the player selected an option on.
     * @param option The option selected.
     * @return <code>True</code> if successful.
     */
    @Override
    public boolean handle(Player player, Node node, String option) {
        SkillingTool tool = SkillingTool.getHatchet(player);
        if (tool == null) {
            player.getActionSender().sendMessage("You don't have an axe to cut this tree.");
            return true;
        }

        if (player.getSavedData().getQuestData().hasKilledTreeSpirit()) {
            handleCutting(player, tool);
            return true;
        }
        for (NPC npc : RegionManager.getLocalNpcs(player)) {
            if (npc.getId() == 655 && npc.getAttribute("PLAYER_SPAWNED") != null) {
                if (npc.getAttribute("PLAYER_SPAWNED") == player) {
                    npc.sendChat("You must defeat me before touching the tree!");
                    npc.attack(player);
                    return true;
                }
            }
        }
        spawnTreeSpirit(player);
        return true;
    }

    /**
     * Handles spawning the tree spirit.
     *
     * @param player The player to spawn the spirit for.
     */
    public void spawnTreeSpirit(final Player player) {
        Location spawn = Location.create(2860, 9738);
        final NPC npc = NPC.create(655, spawn);
        npc.setWalks(true);
        npc.setWalkRadius(10);
        npc.setAttribute("PLAYER_SPAWNED", player);
        npc.init();
        npc.sendChat("You must defeat me before touching the tree!");
        npc.attack(player);
        // If the player isn't near the tree spirit, remove the spirit.
        World.submit(new Pulse(10, player, npc) {

            @Override
            public boolean pulse() {
                if (!npc.isActive() || DeathTask.isDead(npc)) {
                    return true;
                }
                if (player == null || !player.isActive()) {
                    this.stop();
                    npc.clear();
                    return true;
                }
                if (player.getLocation().getDistance(npc.getLocation()) > 20) {
                    this.stop();
                    npc.clear();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Handles cutting the Dramen tree.
     *
     * @param player The player.
     */
    public void handleCutting(final Player player, SkillingTool tool) {
        player.playAnimation(tool.getAnimation());
        final Item branch = new Item(771);
        player.getPulseManager().run(new Pulse(1, player) {

            @Override
            public boolean pulse() {
                if (!player.getInventory().hasRoomFor(branch)) {
                    player.getActionSender().sendMessage("Not enough space in inventory.");
                    return true;
                }
                player.getInventory().add(branch);
                player.getActionSender().sendMessage("You cut a branch from the Dramen tree.");
                player.playAnimation(Animation.create(-1));
                return true;
            }
        });
    }

    /**
     * Creates a new instance.
     *
     * @param arg The argument.
     * @return The plugin instance created.
     */
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(1292).getConfigurations().put("option:chop down", this);
        return this;
    }
}
