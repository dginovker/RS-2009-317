package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the Ancient Altar for spellbook switching.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AltarOfNaturePlugin extends OptionHandler {

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
        if (node.getId() == 3521) {
            if (player.getDonorManager().isDragonstoneMember() && !player.getLocks().isLocked("altar_of_nature_cooldown") || player.getDonorManager().isZenyteMember()) {
                player.lock(1);
                player.animate(new Animation(645));
                player.fullRestore();
                player.getActionSender().sendMessage("You feel a strange sensation heal you...");
                if (!player.getDonorManager().isZenyteMember()) {
                    player.getLocks().lock("altar_of_nature_cooldown", 100);
                }
                return true;
            } else if (player.getLocks().isLocked("altar_of_nature_cooldown")) {
                player.getActionSender().sendMessage("You need to wait 60 seconds to use this altar again!");
            }
        }
        return true;
    }

    /**
     * Creates a new instance.
     *
     * @param arg The argument.
     * @return The plugin instance created.
     */
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(3521).getConfigurations().put("option:pray-at", this);
        return this;
    }
}
