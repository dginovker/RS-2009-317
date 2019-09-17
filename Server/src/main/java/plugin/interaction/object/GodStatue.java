package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin for praying at God statues.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GodStatue extends OptionHandler {

    /**
     * Checks if the player has a god cape in any of their containers.
     *
     * @return The container the cape is in.
     */
    public String hasCape(Player player) {
        if (player.getInventory().contains(2412) || player.getInventory().contains(2413) || player.getInventory().contains(2414)) {
            return "inventory";
        }
        if (player.getBank().contains(2412) || player.getBank().contains(2413) || player.getBank().contains(2414)) {
            return "bank";
        }
        if (player.getEquipment().contains(2412) || player.getEquipment().contains(2413) || player.getEquipment().contains(2414)) {
            return "equipment";
        }
        return null;
    }

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
        if (player.getSkills().getStaticLevel(Skills.MAGIC) < 60) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a level of at least 60 Magic to do this.");
            return true;
        }
        if (player.getAttribute("RECENT_GOD_CAPE", 0) > 0) {
            player.getDialogueInterpreter().sendPlaneMessage("You've recently prayed to a god.");
            return true;
        }
        String container = hasCape(player);
        if (container != null) {
            player.getDialogueInterpreter().sendPlaneMessage("You already have a cape in your " + container + "!");
            return true;
        }
        return player.getDialogueInterpreter().open("GodStatue", (GameObject) node);
    }

    /**
     * Creates a new instance.
     *
     * @param arg The argument.
     * @return The plugin instance created.
     */
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2873).getConfigurations().put("option:pray at", this);
        ObjectDefinition.forId(2874).getConfigurations().put("option:pray at", this);
        ObjectDefinition.forId(2875).getConfigurations().put("option:pray at", this);
        return this;
    }
}

