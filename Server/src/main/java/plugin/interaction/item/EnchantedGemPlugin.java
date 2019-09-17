package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.skill.member.slayer.Equipment;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} for the Enchanted Gem.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class EnchantedGemPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(Equipment.ENCHANTED_GEM.getItem().getId()).getConfigurations().put("option:activate", this);
        ItemDefinition.forId(Equipment.ENCHANTED_GEM.getItem().getId()).getConfigurations().put("option:check", this);
        ItemDefinition.forId(Equipment.ENCHANTED_GEM.getItem().getId()).getConfigurations().put("option:log", this);
        ItemDefinition.forId(Equipment.ENCHANTED_GEM.getItem().getId()).getConfigurations().put("option:partner", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (!player.getSlayer().hasStarted()) {
            player.getActionSender().sendMessage("You try to activate the gem...");
            return true;
        }
        switch (option.toLowerCase()) {
            case "partner":
                player.getActionSender().sendMessage("Coming soon");
                return true;
            case "activate":
                player.getDialogueInterpreter().open(77777);
                return true;
            case "log":
                player.getSavedData().getBossKillLog().sendInterface(player);
                return true;
            case "check":
                player.getSlayer().informTaskProgress();
                return true;
        }
        player.getActionSender().sendMessage("Unknown option \"" + option + "\"! Please report on forums.");
        return false;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}
