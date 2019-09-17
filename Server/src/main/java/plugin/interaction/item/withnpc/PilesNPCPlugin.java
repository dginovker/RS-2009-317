package plugin.interaction.item.withnpc;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.rs2.plugin.Plugin;
import plugin.zone.wilderness.ResourceArea;

/**
 * Created by Stan van der Bend on 09/01/2018.
 * <p>
 * todo: add obtainable items from resource area.
 * <p>
 * project: GielinorGS
 * package: plugin.interaction.item.withnpc
 */
public class PilesNPCPlugin extends UseWithHandler {
    
    public PilesNPCPlugin() {
        super(ResourceArea.OBTAINABLE_ITEM_IDS);
    }
    
    @Override
    public boolean handle(NodeUsageEvent event) {
        //todo: open item use with dialogue
        return true;
    }
    
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(ResourceArea.PILES_NPC_ID, NPC_TYPE, this);
        return this;
    }
}
