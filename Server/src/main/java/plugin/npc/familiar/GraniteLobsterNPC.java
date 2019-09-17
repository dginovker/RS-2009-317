package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the Granite Lobster familiar.
 * @author Aero
 */
public class GraniteLobsterNPC extends Familiar {

    /**
     * Constructs a new {@code GraniteLobsterNPC} {@code Object}.
     */
    public GraniteLobsterNPC() {
        this(null, 6849);
    }

    /**
     * Constructs a new {@code GraniteLobsterNPC} {@code Object}.
     * @param owner The owner.
     * @param id The id.
     */
    public GraniteLobsterNPC(Player owner, int id) {
        super(owner, id, 4700, 12069, 6);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new GraniteLobsterNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6849, 6850 };
    }

}
