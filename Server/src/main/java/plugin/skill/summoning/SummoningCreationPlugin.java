package plugin.skill.summoning;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.member.summoning.SummoningCreator;
import org.gielinor.game.content.skill.member.summoning.SummoningPouch;
import org.gielinor.game.content.skill.member.summoning.SummoningScroll;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents a component plugin used to handle the summoning creation of a
 * node.
 *
 * @author 'Vexia
 */
public final class SummoningCreationPlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(25728, this);
        ComponentDefinition.put(25821, this);
        ComponentDefinition.put(25810, this);
        ComponentDefinition.put(25717, this);
        PluginManager.definePlugin(new ObeliskHandler());
        return this;
    }

    @Override
    public boolean handle(Player player, final Component component, int opcode, int button, final int slot, int itemId) {
        if (component.getId() == 25810 || component.getId() == 25717) {
            switch (button) {
                case 25725:
                case 25818:
                    SummoningCreator.configure(player, button == 25818);
                    return true;
            }

        }
        if (component.getId() != 25728 && component.getId() != 25821 && component.getId() != 25810) {
            return false;
        }
        switch (component.getId()) {
            case 25728:
            case 25821:
            case 25810:
                switch (opcode) {
                    case 145:
                    case 117:
                    case 43:
                    case 129:
                        SummoningCreator.create(player, getItemAmount(opcode), component.getId() == 25810 ? SummoningScroll.forId(slot) : SummoningPouch.forSlot(slot));
                        break;
                    case 135:
                        if (component.getId() == 25810) {
                            player.setAttribute("runscript", new RunScript() {

                                @Override
                                public boolean handle() {
                                    SummoningCreator.create(player, (int) getValue(), SummoningScroll.forId(slot));
                                    return true;
                                }
                            });
                            player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
                            return true;
                        }
                        SummoningCreator.list(player, SummoningPouch.forSlot(slot));
                        break;
                }
                break;
        }
        return true;
    }

    /**
     * Method used to get the item amount based on id.
     *
     * @param opcode the opcode.
     * @return the amount to make.
     */
    private int getItemAmount(final int opcode) {
        return opcode == 145 ? 1 : opcode == 117 ? 5 : opcode == 43 ? 10 : opcode == 129 ? 28 : -1;
    }

    /**
     * Represents the use with handler for an obelisk.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class ObeliskHandler extends UseWithHandler {

        /**
         * Represents the ids of the obelisks.
         */
        private static final int[] IDS = new int[]{ 28716, 28719, 28722, 28725, 28278, 28731, 28734 };

        /**
         * Constructs a new {@code ObeliskHandler} {@code Object}.
         */
        public ObeliskHandler() {
            super(12047, 12043, 12059, 12019, 12009, 12778, 12049, 12055, 12808, 2067, 12064, 12091, 12800, 12053, 12065, 12021, 12818, 12781, 12798, 12814, 12073, 12075, 12077, 12079, 12081, 12083, 12087, 12071, 12051, 12095, 12097, 12099, 12101, 12103, 12105, 12107, 12816, 12041, 12061, 12007, 12035, 12027, 12531, 12812, 12784, 12710, 12023, 12085, 12037, 12015, 12045, 12123, 12031, 12029, 12033, 12820, 12057, 12792, 12069, 12011, 12782, 12794, 12013, 12025, 12017, 12039, 12089, 12093, 12802, 12804, 12806, 12788, 12776, 12786, 12796, 12822, 12790);
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            for (int id : IDS) {
                addHandler(id, OBJECT_TYPE, this);
            }
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            final Player player = event.getPlayer();
            SummoningCreator.open(player, false);
            return true;
        }

    }
}
