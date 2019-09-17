package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the Ravenous Locust familiar.
 * @author Aero
 */
public class RavenousLocustNPC extends Familiar {

    /**
     * Constructs a new {@code RavenousLocustNPC} {@code Object}.
     */
    public RavenousLocustNPC() {
        this(null, 7372);
    }

    /**
     * Constructs a new {@code RavenousLocustNPC} {@code Object}.
     * @param owner The owner.
     * @param id The id.
     */
    public RavenousLocustNPC(Player owner, int id) {
        super(owner, id, 2400, 12820, 12);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new RavenousLocustNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 7372, 7373 };
    }

}
