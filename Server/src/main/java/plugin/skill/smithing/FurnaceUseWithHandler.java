package plugin.skill.smithing;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.content.skill.free.smithing.smelting.Bar;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to handle the ore on the furance.
 *
 * @author 'Vexia
 */
public final class FurnaceUseWithHandler extends UseWithHandler {

    /**
     * Represents the ids.
     */
    private static final int[] IDS = new int[]{ 2966, 3044, 3294, 3994, 4304, 6189, 11009, 11010, 11666, 12100, 12806, 12807, 12808, 12809, 14921, 18497, 18525, 18526, 21879, 22721, 26814, 28433, 28434, 30018, 30019, 30020, 30021, 30508, 30509, 30510, 36956, 37651, };

    /**
     * Constructs a new {@code SmeltUseWithHandler} {@code Object}.
     */
    public FurnaceUseWithHandler() {
        super(getIds());
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int i : IDS) {
            addHandler(i, OBJECT_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        FurnaceOptionPlugin.show(event.getPlayer());
        return true;
    }

    /**
     * Gets the ore ids.
     *
     * @return the ids.
     */
    public static int[] getIds() {
        List<Integer> ids = new ArrayList<>();
        for (Bar bar : Bar.values()) {
            for (Item i : bar.getOres()) {
                if (i.getId() == 2355) {
                    continue;
                }
                ids.add(i.getId());
            }
        }
        int[] array = new int[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            array[i] = ids.get(i);
        }
        return array;
    }
}
