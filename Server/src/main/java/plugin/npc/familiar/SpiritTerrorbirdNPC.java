package plugin.npc.familiar;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;

/**
 * Represents the Spirit Terrorbird familiar.
 * @author Aero
 * @author Vexia
 */
public class SpiritTerrorbirdNPC extends BurdenBeast {

    /**
     * Constructs a new {@code SpiritTerrorbirdNPC} {@code Object}.
     */
    public SpiritTerrorbirdNPC() {
        this(null, 6794);
    }

    /**
     * Constructs a new {@code SpiritTerrorbirdNPC} {@code Object}.
     * @param owner The owner.
     * @param id The id.
     */
    public SpiritTerrorbirdNPC(Player owner, int id) {
        super(owner, id, 3600, 12007, 8, 12);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new SpiritTerrorbirdNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        visualize(Animation.create(1009), Graphics.create(1521));
        owner.getSkills().updateLevel(Skills.AGILITY, 2);
        owner.getSettings().updateRunEnergy(-owner.getSkills().getStaticLevel(Skills.AGILITY) / 2);
        return true;
    }

    @Override
    public void visualizeSpecialMove() {
        owner.visualize(new Animation(7660), new Graphics(1295));
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6794, 6795 };
    }

}
