package plugin.npc;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;

/**
 * Handles the tutorial rat npc.
 *
 * @author 'Vexia
 */
public class RatTutorialNPC extends AbstractNPC {

    /**
     * The NPC ids of NPCs using this plugin.
     */
    private static final int[] ID = { 86 };

    /**
     * Constructs a new {@code AlKharidWarriorPlugin} {@code Object}.
     */
    public RatTutorialNPC() {
        super(0, null);
        this.setAggressive(false);
    }

    /**
     * Constructs a new {@code AlKharidWarriorPlugin} {@code Object}.
     *
     * @param id       The NPC id.
     * @param location The location.
     */
    private RatTutorialNPC(int id, Location location) {
        super(id, location, true);
    }

    @Override
    public void init() {
        super.init();
        setAggressive(false);
        getSkills().setLevel(Skills.HITPOINTS, 5);
        getSkills().setStaticLevel(Skills.HITPOINTS, 5);
        getSkills().setLifepoints(5);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new RatTutorialNPC(id, location);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void finalizeDeath(final Entity killer) {
        super.finalizeDeath(killer);
        final Player p = ((Player) killer);
    }

    @Override
    public int[] getIds() {
        return ID;
    }

}
