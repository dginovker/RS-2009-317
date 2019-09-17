package org.gielinor.game.content.skill.member.hunter;

import org.gielinor.game.content.skill.member.hunter.bnet.ImplingNode;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.path.Pathfinder;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.parser.npc.NPCConfiguration;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles an impling npc.
 *
 * @author Vexia
 */
public final class ImplingNPC extends AbstractNPC {

    /**
     * The impling node.
     */
    private final ImplingNode impling;

    /**
     * Constructs a new {@code ImplingNPC} {@code Object}.
     */
    public ImplingNPC() {
        super(0, null);
        this.impling = null;
        this.setWalks(true);
        this.setWalkRadius(20);
    }

    /**
     * Constructs a new {@code ImplingNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     */
    public ImplingNPC(int id, Location location, ImplingNode impling) {
        super(id, location);
        this.impling = impling;
        if (impling != null) {
            this.getDefinition().getConfigurations().put(NPCConfiguration.RESPAWN_DELAY, impling.getRespawnTime());
        }
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new ImplingNPC(id, location, null);
    }

    @Override
    public void handleTickActions() {
        if (!getLocks().isMovementLocked()) {
            if (isWalks() && !getPulseManager().hasPulseRunning() && getNextWalk() < World.getTicks()) {
                setNextWalk();
                Location l = getLocation().transform(-5 + RandomUtil.random(getWalkRadius()), -5 + RandomUtil.random(getWalkRadius()), 0);
                if (canMove(l)) {
                    Pathfinder.find(this, l, true, Pathfinder.PROJECTILE).walk(this);
                }
            }
        }
        if (RandomUtil.random(100) < 4) {
            sendChat("Tee hee!");
        }
        int nextTeleport = getAttribute("nextTeleport", -1);
        if (nextTeleport > -1 && World.getTicks() > nextTeleport) {
            setAttribute("nextTeleport", World.getTicks() + 600);
            graphics(new Graphics(590));
            World.submit(new Pulse(1) {

                @Override
                public boolean pulse() {
                    setTeleportTarget(ImpetuousImpulses.LOCATIONS[RandomUtil.random(ImpetuousImpulses.LOCATIONS.length)]);
                    return true;
                }
            });
        }
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style) {
        return style == CombatStyle.MAGIC && (entity.getProperties().getSpell().getSpellId() == 12 || entity.getProperties().getSpell().getSpellId() == 30 || entity.getProperties().getSpell().getSpellId() == 56 || super.isAttackable(entity, style));
    }

    @Override
    public void handleDrops(Player p, Entity killer) {
        getProperties().setTeleportLocation(getProperties().getSpawnLocation());
    }

    @Override
    public void checkImpact(BattleState state) {
    }

    @Override
    public int[] getIds() {
        return new int[]{};
    }

    /**
     * Gets the impling.
     *
     * @return The impling.
     */
    public ImplingNode getImpling() {
        return impling;
    }

}
