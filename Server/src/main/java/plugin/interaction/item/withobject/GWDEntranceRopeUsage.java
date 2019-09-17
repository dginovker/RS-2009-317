package plugin.interaction.item.withobject;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles using a rope on the God wars entrance.
 * @author Emperor
 *
 */
public final class GWDEntranceRopeUsage extends UseWithHandler {

    /**
     * Constructs a new {@code GWDEntranceRopeUsage} {@code Object}.
     */
    public GWDEntranceRopeUsage() {
        super(954);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(26340, OBJECT_TYPE, this);
        return this;
    }


    @Override
    public boolean handle(NodeUsageEvent event) {
        GameObject object = (GameObject) event.getUsedWith();
        return object.getInteraction().get(0).getHandler().handle(event.getPlayer(), object, "tie-rope");
    }

}
