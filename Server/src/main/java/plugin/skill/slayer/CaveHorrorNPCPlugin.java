package plugin.skill.slayer;

import org.gielinor.game.content.skill.member.slayer.Equipment;
import org.gielinor.game.content.skill.member.slayer.Tasks;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;

/**
 * Represents the plugin for the cave horror NPC.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class CaveHorrorNPCPlugin extends AbstractNPC {

    /**
     * The combat handler to use.
     */
    private static final MeleeSwingHandler COMBAT_HANDLER = new MeleeSwingHandler() {

        @Override
        public void impact(final Entity entity, final Entity victim, BattleState battleState) {
            if (victim instanceof Player) {
                Player player = (Player) victim;
                if (!Equipment.WITCHWOOD_ICON.hasEquipment(player)) {
                    int nextHit = (player.getSkills().getLifepoints() * 10) / 100;
                    battleState.setEstimatedHit(nextHit);
                }
            }
            super.impact(entity, victim, battleState);
        }
    };

    /**
     * Constructs a new <code>CaveHorrorNPCPlugin</code>.
     *
     * @param id       the id.
     * @param location the location.
     */
    public CaveHorrorNPCPlugin(int id, Location location) {
        super(id, location);
        //getProperties().getCombatPulse().setStyle(CombatStyle.MAGIC);
    }

    /**
     * Constructs a new <code>CaveHorrorNPCPlugin</code>.
     */
    public CaveHorrorNPCPlugin() {
        super(0, null);
    }

    @Override
    public void checkImpact(BattleState state) {
        super.checkImpact(state);
        if (state.getAttacker() instanceof Player) {
            final Player player = (Player) state.getAttacker();
            if (!Equipment.WITCHWOOD_ICON.hasEquipment(player)) {
                state.neutralizeHits();
            }
        }
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return COMBAT_HANDLER;
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new CaveHorrorNPCPlugin(id, location);
    }

    @Override
    public int[] getIds() {
        return Tasks.CAVE_HORRORS.getTask().getNpcs();
    }

}
