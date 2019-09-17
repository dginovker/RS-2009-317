package plugin.npc;

import org.gielinor.game.content.global.quest.QuestState;
import org.gielinor.game.content.global.quest.impl.TheLostKingdom;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.agg.AggressiveBehavior;
import org.gielinor.game.node.entity.npc.agg.AggressiveHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;

/**
 * Represents a guard for King Gjuki.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *         5517 = Chest Guard
 */
public class GjukiGuardNPC extends AbstractNPC {

    /**
     * Constructs a new {@code GjukiGuardNPC} {@code Object}.
     */
    public GjukiGuardNPC() {
        this(4999, null);
    }

    /**
     * Constructs a new {@code GjukiGuardNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     */
    public GjukiGuardNPC(int id, Location location) {
        super(id, location);
        this.setAggressive(true);
        this.setWalks(true);
        this.setWalkRadius(30);
        this.setRespawn(true);
    }

    @Override
    public void setDefaultBehavior() {
        super.setAggressive(true);
        super.setAggressiveHandler(new AggressiveHandler(this, new AggressiveBehavior() {

            @Override
            public boolean canSelectTarget(Entity entity, Entity target) {
                if (target instanceof Player) {
                    if (((Player) target).getSavedData().getGlobalData().getVisibility() != 0) {
                        return false;
                    }
                    if (((Player) target).getQuestRepository().getQuest(TheLostKingdom.NAME).getStage() < 30) {
                        return false;
                    }
                    if (target.getLocation().getRegionId() != 9531) {
                        return false;
                    }
                }
                return true;
            }
        }));
        getAggressiveHandler().setChanceRatio(8);
        getAggressiveHandler().setRadius(20);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new GjukiGuardNPC(id, location);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player && getLocation().getRegionId() == 9531) {
            Player player = (Player) killer;
            if (player.getQuestRepository().getQuest(TheLostKingdom.NAME).getState() == QuestState.COMPLETED) {
                player.getSavedData().getGlobalData().increaseRoaldPoints();
            }
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{ 4999 };
    }

}
