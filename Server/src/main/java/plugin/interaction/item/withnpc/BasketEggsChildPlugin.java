package plugin.interaction.item.withnpc;

import java.util.List;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.rs2.plugin.Plugin;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BasketEggsChildPlugin extends UseWithHandler {

    /**
     * Constructs a new {@code NeedleCowPlugin} {@code Object}.
     */
    public BasketEggsChildPlugin() {
        super(4565);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(2872, NPC_TYPE, this);
        addHandler(2873, NPC_TYPE, this);
        addHandler(2874, NPC_TYPE, this);
        addHandler(2875, NPC_TYPE, this);
        addHandler(2876, NPC_TYPE, this);
        addHandler(2877, NPC_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        List<Integer> childrenList = event.getPlayer().getSavedData().getGlobalData().getEggsDelivered();
        if (childrenList.contains(event.getUsedWith().getId())) {
            event.getPlayer().getDialogueInterpreter().sendPlaneMessage(false, "You have already given them an Easter egg.");
            return true;
        }
        event.getPlayer().getDialogueInterpreter().sendPlaneMessage(false, "You give the child an Easter egg!");
        event.getPlayer().getSavedData().getGlobalData().getEggsDelivered().add(event.getUsedWith().getId());
        int left = 6 - childrenList.size();
        if (left > 0) {
            event.getPlayer().getActionSender().sendMessage("<col=F298F4><shad=1>Only " + left + " more child" + (left == 1 ? "" : "ren") + " left!");
            return true;
        }
        event.getPlayer().getActionSender().sendMessage("<col=F298F4><shad=1>You have delivered all of the Easter eggs! Talk to the Easter bunny at home!");
        return true;
    }

}
