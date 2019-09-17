package plugin.npc.osrs.zulrah;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;

/**
 * Handles interactions related to Zulrah.
 *
 * @author Vexia
 */
public class ZulrahPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(10068).getConfigurations().put("option:board", this);
        PluginManager.definePlugin(new ZulrahCutscene(), new ZulrahNPC());
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (node.asObject().getId() == 10068) {
            player.getDialogueInterpreter().sendOptions("Return to Zulrah's shrine?", "Yes, skip cutscene", "Yes", "No");
            player.getDialogueInterpreter().addAction((player1, optionSelect) -> {
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        ActivityManager.start(player1, "zulrah", false, false);
                        break;
                    case THREE_OPTION_TWO:
                        ActivityManager.start(player1, "zulrah", false, true);
                        break;
                    case THREE_OPTION_THREE:
                        player1.getDialogueInterpreter().close();
                        break;
                }
            });
            return true;
        }
        return true;
    }

}
