package plugin.skill.slayer;

import org.gielinor.game.content.skill.member.slayer.Tasks;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the abyssal npc.
 *
 * @author Vexia
 */
public class AbyssalDemonNPCPlugin extends AbstractNPC {

    /**
     * The melee swing handler.
     */
    private static final MeleeSwingHandler SWING_HANDLER = new MeleeSwingHandler() {

        @Override
        public int swing(Entity entity, Entity victim, BattleState state) {
            if (victim instanceof Player && RandomUtil.random(8) <= 2) {
                boolean npc = RandomUtil.random(100) <= 50;
                Entity source = npc ? victim : entity;
                Entity teleported = npc ? entity : victim;
                Location loc = source.getLocation().transform(Direction.values()[RandomUtil.random(Direction.values().length)], 1);
                if (loc != null && !loc.equals(source.getLocation()) &&
                    RegionManager.isTeleportPermitted(loc) && RegionManager.getObject(loc) == null && loc.getY() <= 3576) {
                    teleported.graphics(Graphics.create(409));
                    teleported.teleport(loc, 1);
                }
            }
            return super.swing(entity, victim, state);
        }
    };

    /**
     * Constructs a new {@Code AbyssalNPC} {@Code Object}
     */
    public AbyssalDemonNPCPlugin() {
        super(-1, null);
    }

    /**
     * Constructs a new {@Code AbyssalNPC} {@Code Object}
     *
     * @param id       the id.
     * @param location the location.
     */
    public AbyssalDemonNPCPlugin(int id, Location location) {
        super(id, location);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return SWING_HANDLER;
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new AbyssalDemonNPCPlugin(id, location);
    }

    @Override
    public int[] getIds() {
        return Tasks.ABYSSAL_DEMONS.getTask().getNpcs();
    }

}
