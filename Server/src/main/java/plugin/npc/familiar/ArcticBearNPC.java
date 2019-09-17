package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the Arctic Bear familiar.
 *
 * @author Aero
 */
public class ArcticBearNPC extends Familiar {

    /**
     * Constructs a new {@code ArcticBearNPC} {@code Object}.
     */
    public ArcticBearNPC() {
        this(null, 6839);
    }

    /**
     * Constructs a new {@code ArcticBearNPC} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The id.
     */
    public ArcticBearNPC(Player owner, int id) {
        super(owner, id, 2800, 12057, 6);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new ArcticBearNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6839, 6840 };
    }

}
