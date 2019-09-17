package plugin.npc;

import org.gielinor.game.content.global.quest.impl.CurseOfTheUndead;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.rs2.pulse.Pulse;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CurseZombieNPC extends AbstractNPC {

    /**
     * If this {@link plugin.npc.CurseZombieNPC} is the primary.
     */
    private boolean primary = false;
    /**
     * If the hit is successful.
     */
    private boolean hitSuccess = false;

    /**
     * Constructs a new {@code CurseZombiePlugin} {@code Object}.
     */
    public CurseZombieNPC() {
        this(5317, new Location(2407, 9604, 0));
    }

    /**
     * Constructs a new {@code CurseZombiePlugin} {@code Object}.
     */
    public CurseZombieNPC(int id, Location spawn) {
        super(id, spawn);
        super.setAggressive(true);
        super.walkRadius = 0;
        super.setWalks(false);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        if (killer instanceof Player && primary) {
            Player player = ((Player) killer);
            player.getQuestRepository().getQuest(CurseOfTheUndead.NAME).setStage(10);
            player.getProperties().setTeleportLocation(player.getAttribute("SUROK_ORIGINAL", Location.create(3564, 3288, 0)));
            player.getPulseManager().run(new Pulse() {

                @Override
                public boolean pulse() {
                    player.getDialogueInterpreter().open(2024);
                    return true;
                }
            });
        }
        getImpactHandler().getImpactQueue().clear();
        this.clear();
        this.setRespawn(false);
    }


    @Override
    public void checkImpact(BattleState state) {
        super.checkImpact(state);
        Entity entity = state.getAttacker();
        if (entity == null || !(entity instanceof Player)) {
            return;
        }
        Player player = (Player) entity;
        if (state.getStyle() != CombatStyle.MAGIC) {
            player.getActionSender().sendMessage("The zombies won't be affected by that!");
            state.setEstimatedHit(0);
            state.neutralizeHits();
            return;
        }
        if (state.getSpell() == null) {
            player.getActionSender().sendMessage("The zombies don't seem to be affected!");
            state.setEstimatedHit(0);
            state.neutralizeHits();
            return;
        }
        if (state.getSpell().getSpellId() != 12891) {
            player.getActionSender().sendMessage("The zombies don't seem to be affected!");
            state.setEstimatedHit(0);
            state.neutralizeHits();
            return;
        }
        hitSuccess = true;
    }

    @Override
    public void onImpact(Entity entity, BattleState state) {
        if (!hitSuccess) {
            return;
        }
        state.setEstimatedHit(0);
        state.neutralizeHits();
        for (NPC npc : RegionManager.getLocalNpcs(entity)) {
            if (npc == null || !(npc instanceof CurseZombieNPC) ||
                npc.getLocation().getZ() != entity.getLocation().getZ()) {
                continue;
            }
            npc.getImpactHandler().manualHit(entity, 1000, ImpactHandler.HitsplatType.NORMAL);
        }
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new CurseZombieNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5317 };
    }

    /**
     * Sets this {@link plugin.npc.CurseZombieNPC} as the primary.
     */
    public void setPrimary() {
        this.primary = true;
    }
}
