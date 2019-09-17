package plugin.skill.slayer;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the dezert lizard plugin.
 *
 * @author 'Vexia
 */
public final class DesertLizardPlugin implements Plugin<Object> {

    /**
     * Represents the ids to use.
     */
    private static final int[] IDS = new int[]{ 458, 459, 460, 461, 462, 463 };

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        new IcecoolerPlugin().newInstance(arg);
        new DezertLizardNPC().newInstance(arg);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Represents the ice cooler plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class IcecoolerPlugin extends UseWithHandler {

        /**
         * Represents the ice cooler item.
         */
        private static final Item ICE_COOLER = new Item(6696);

        /**
         * Constructs a new {@code IcecoolerPlugin} {@code Object}.
         */
        public IcecoolerPlugin() {
            super(ICE_COOLER.getId());
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            for (int id : IDS) {
                addHandler(id, NPC_TYPE, this);
            }
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            final Player player = event.getPlayer();
            if (player.getSkills().getLevel(Skills.SLAYER) < 22) {
                player.getActionSender().sendMessage("You need a Slayer level of at least 22 to do this.");
                return true;
            }
            final NPC npc = (NPC) event.getUsedWith();
            if (npc.getSkills().getLifepoints() > 2) {
                player.getActionSender().sendMessage("The lizard isn't weak enough to be affected by the icy water.");
                return true;
            }
            if (player.getInventory().remove(ICE_COOLER)) {
                npc.getImpactHandler().manualHit(player, npc.getSkills().getLifepoints(), HitsplatType.NORMAL);
                player.getActionSender().sendMessage("The lizard shudders and collapses from the freezing water.");
            }
            return true;
        }

    }

    /**
     * Represents the desert lizard npc.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class DezertLizardNPC extends AbstractNPC {

        /**
         * Constructs a new {@code DezertLizardNPC} {@code Object}.
         *
         * @param id       the id.
         * @param location the location.
         */
        public DezertLizardNPC(int id, Location location) {
            super(id, location);
        }

        /**
         * Constructs a new {@code DezertLizardNPC} {@code Object}.
         */
        public DezertLizardNPC() {
            super(0, null);
        }

        @Override
        public AbstractNPC construct(int id, Location location, Object... objects) {
            return new DezertLizardNPC(id, location);
        }

        @Override
        public void checkImpact(BattleState state) {
            int lifepoints = getSkills().getLifepoints();
            if (state.getEstimatedHit() > -1) {
                lifepoints -= state.getEstimatedHit();
                if (lifepoints < 1) {
                    state.setEstimatedHit(lifepoints - 1);
                }
            }
            if (state.getSecondaryHit() > -1) {
                lifepoints -= state.getSecondaryHit();
                if (lifepoints < 1) {
                    state.setSecondaryHit(lifepoints - 1);
                }
            }
        }

        @Override
        public int[] getIds() {
            return IDS;
        }

    }
}
