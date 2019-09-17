package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.utilities.misc.RandomUtil;

import java.util.Arrays;

/**
 * Handles a morph item.
 *
 * @author Vexia
 * @author Corey
 */
public class MorphItemPlugin implements Plugin {

    /**
     * The easter egg ids.
     */
    protected static final int[] EASTER_EGG_IDS = new int[]{ 3689, 3690, 3691, 3692, 3693, 3694 };

    /**
     * The morph component.
     */
    private static final Component COMPONENT = new Component(6014).setCloseEvent(new CloseEvent() {

        @Override
        public void close(Player player, Component c) {
            unmorph(player);
        }

        @Override
        public boolean canClose(Player player, Component component) {
            return true;
        }

    });

    /**
     * Handles the morph interface plugin.
     *
     * @author Vexia
     */
    public class MorphInterfacePlugin extends ComponentPlugin {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ComponentDefinition.put(COMPONENT.getId(), this);
            return this;
        }


        @Override
        public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
            if (component.getId() != COMPONENT.getId()) {
                return false;
            }
            unmorph(player);
            return true;
        }

    }

    @Override
    public Plugin newInstance(Object arg) throws Throwable {
        // automatically configure all item ids
        Arrays.stream(Morph.values()).forEach(morph -> ItemDefinition.forId(morph.itemId).getConfigurations().put("equipment", this));
        PluginManager.definePlugin(new MorphInterfacePlugin());
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        final Player player = (Player) args[0];
        final Item item = (Item) args[1];
        switch (identifier) {
            case "equip":
                morph(player, item);
                return false;
        }
        return true;
    }

    /**
     * Morphs the player.
     *
     * @param player the player.
     * @param item   the item.
     */
    private void morph(Player player, Item item) {
        Morph morph = Morph.forId(item);
        int morphId = morph.getRandomMorph();

        player.getInterfaceState().close();

        switch (morph.type) {
            case NPC:
                player.getAppearance().transformNPC(morphId);
                break;
            case ITEM:
                player.getAppearance().transformItem(morphId);
                break;
            case OBJECT:
                player.getAppearance().transformObject(morphId);
                break;
        }

        player.getInterfaceState().removeTabs(0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
        player.getInterfaceState().openTab(3, COMPONENT);
        player.getLocks().lockMovement(World.getTicks() + 900000000);
        player.getLocks().lockInteractions(World.getTicks() + 90000000);
        player.getLocks().lockTeleport(World.getTicks() + 900000000);
        player.getAppearance().sync();
    }

    /**
     * Unmorphs the player.
     *
     * @param player the player.
     */
    private static void unmorph(Player player) {
        player.getInterfaceState().closeSingleTab();
        player.getAppearance().transformNPC(-1);
        player.getAppearance().transformItem(-1);
        player.getAppearance().transformObject(-1);
        player.unlock();
        player.getInterfaceState().openDefaultTabs();
    }

    public enum Morph {
        EASTER_RING(7927, MorphType.NPC, EASTER_EGG_IDS),
        RING_OF_STONE(6583, MorphType.NPC, 2626),
        RING_OF_COINS(40017, MorphType.ITEM, 1004),
        RING_OF_NATURE(40005, MorphType.OBJECT, 1124);

        private int itemId;
        private MorphType type;
        private int[] morphId;

        Morph(int itemId, MorphType type, int... morphId) {
            this.itemId = itemId;
            this.type = type;
            this.morphId = morphId;
        }

        public int getRandomMorph() {
            return morphId[RandomUtil.random(morphId.length)];
        }

        public static Morph forId(Item item) {
            return forId(item.getId());
        }

        public static Morph forId(int itemId) {
            return Arrays.stream(values()).filter(morph -> morph.itemId == itemId).findFirst().get();
        }

        enum MorphType {
            NPC,
            OBJECT,
            ITEM
        }
    }
}
