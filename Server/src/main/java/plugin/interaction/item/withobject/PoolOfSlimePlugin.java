package plugin.interaction.item.withobject;

import org.gielinor.game.content.skill.free.prayer.Ectofuntus;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles using an empty bucket on a pool of slime, and a hopper.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class PoolOfSlimePlugin extends UseWithHandler {

    /**
     * Constructs a new {@code PoolOfSlimePlugin} {@code Object}.
     */
    public PoolOfSlimePlugin() {
        super(526, 3187, 2859, 528, 3179, 3181, 3183, 3185, 530, 532, 3125, 3127, 3128, 3129, 3130, 3131, 3132, 3133, 4812, 3123, 534, 6812, 536, 4830, 4832, 6729, 4834, 1925, 526, 3187, 2859, 528, 3179, 3181, 3183, 3185, 530, 532, 3125, 3127, 3128, 3129, 3130, 3131, 3132, 3133, 4812, 3123, 534, 6812, 536, 4830, 4832, 6729, 4834, 1925);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(Ectofuntus.HOPPER, OBJECT_TYPE, this);
        addHandler(17116, OBJECT_TYPE, this);
        addHandler(17117, OBJECT_TYPE, this);
        addHandler(17118, OBJECT_TYPE, this);
        addHandler(17119, OBJECT_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        event.getPlayer().faceLocation(event.getUsedWith().getLocation());
        Ectofuntus.handleItemOnObject(event.getPlayer(), event.getUsed().getId(), event.getUsedWith().getId());
        return true;
    }

}
