package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the Barker Toad familiar.
 *
 * @author Aero
 */
public class BarkerToadNPC extends Familiar {

    /**
     * Constructs a new {@code BarkerToadNPC} {@code Object}.
     */
    public BarkerToadNPC() {
        this(null, 6889);
    }

    /**
     * Constructs a new {@code BarkerToadNPC} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The id.
     */
    public BarkerToadNPC(Player owner, int id) {
        super(owner, id, 800, 12123, 6);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new BarkerToadNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6889, 6890 };
    }

}
