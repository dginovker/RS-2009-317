package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Created by Stan van der Bend on 09/01/2018.
 * project: GielinorGS
 * package: plugin.interaction.npc
 */
public class PilesNoteItemOptionPlugin extends OptionHandler {

    private final static int PILES_NPC_ID = 13;


    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getDialogueInterpreter().open(4250, node);
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(PILES_NPC_ID).getConfigurations().put("option:talk-to", this);
        return this;
    }

}
