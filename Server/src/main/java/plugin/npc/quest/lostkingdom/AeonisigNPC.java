package plugin.npc.quest.lostkingdom;

import org.gielinor.game.content.global.quest.impl.TheLostKingdom;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;

/**
 * Represents the plugin used for the Aeonisig NPC.
 */
public final class AeonisigNPC extends AbstractNPC {

    /**
     * Constructs a new {@code AeonisigNPC} {@code Object}.
     */
    public AeonisigNPC() {
        super(0, null);
    }

    /**
     * Constructs a new {@code AeonisigNPC} {@code Object}.
     *
     * @param id       The NPC id.
     * @param location The location.
     */
    private AeonisigNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new AeonisigNPC(id, location);
    }

    @Override
    public boolean isHidden(final Player player) {
        return player.getQuestRepository().getQuest(TheLostKingdom.NAME).getStage() >= 30;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 4710 };
    }

}
