package plugin.interaction.item.withnpc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles using a rotten potato on an NPC.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class RottenPotatoNPCPlugin extends UseWithHandler {

    /**
     * Constructs a new {@code RottenPotatoNPCPlugin}.
     */
    public RottenPotatoNPCPlugin() {
        super(5733);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (NPCDefinition npcDefinition : NPCDefinition.getDefinitions().values()) {
            addHandler(npcDefinition.getId(), NPC_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        NPC npc = (NPC) event.getUsedWith();
        Player player = event.getPlayer();
        player.setAttribute("REMOVE_NPC", npc);
        player.getDialogueInterpreter().open("RottenPotato");
        return true;
    }

    @Override
    public boolean isWalks() {
        return false;
    }
}
