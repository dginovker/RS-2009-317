package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the Stranger Plant familiar.
 * @author Aero
 */
public class StrangerPlantNPC extends Familiar {

    /**
     * Constructs a new {@code StrangerPlantNPC} {@code Object}.
     */
    public StrangerPlantNPC() {
        this(null, 6827);
    }

    /**
     * Constructs a new {@code StrangerPlantNPC} {@code Object}.
     * @param owner The owner.
     * @param id The id.
     */
    public StrangerPlantNPC(Player owner, int id) {
        super(owner, id, 4900, 12045, 6);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new StrangerPlantNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6827, 6828 };
    }

}
