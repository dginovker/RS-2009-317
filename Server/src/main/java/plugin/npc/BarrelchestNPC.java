package plugin.npc;

import java.security.SecureRandom;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.InteractionType;
import org.gielinor.game.node.entity.combat.equipment.ArmourSet;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The Barrelchest {@link org.gielinor.game.node.entity.npc.AbstractNPC}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BarrelchestNPC extends AbstractNPC {

    /**
     * The combat swing handler.
     */
    private CombatSwingHandler combatHandler = new BarrelchestSwingHandler();

    /**
     * Constructs a new {@code BarrelchestNPC} {@code Object}.
     */
    public BarrelchestNPC() {
        this(5666, new Location(2766, 9192, 0));
    }

    /**
     * Constructs a new {@code BarrelchestNPC} {@code Object}.
     */
    public BarrelchestNPC(int id, Location spawn) {
        super(id, spawn);
        super.setAggressive(true);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return combatHandler;
    }

    @Override
    public boolean hasProtectionPrayer(CombatStyle style) {
        return style == CombatStyle.MELEE;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void init() {
        super.init();
        setRespawn(true);
        getAggressiveHandler().setRadius(64);
        setWalkRadius(64);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            killer.asPlayer().getSavedData().getBossKillLog().increaseBarrelchestKills(1);
        }
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new BarrelchestNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5666 };
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        init();
        return super.newInstance(arg);
    }


    /**
     * The Barrelchest swinghandler.
     *
     * @author <a href="http://Gielinor.org/">Gielinor</a>
     */
    static class BarrelchestSwingHandler extends CombatSwingHandler {

        /**
         * The {@link java.security.SecureRandom} instance.
         */
        private final SecureRandom secureRandom = new SecureRandom();

        /**
         * Constructs a new {@code BarrelchestNPC} {@code Object}.
         */
        public BarrelchestSwingHandler() {
            super(CombatStyle.MELEE);
        }

        @Override
        public InteractionType canSwing(Entity entity, Entity victim) {
            return getType().getSwingHandler().canSwing(entity, victim);
        }

        @Override
        public int swing(Entity entity, Entity victim, BattleState state) {
            boolean disabledPrayer = false;
            if (victim instanceof Player && secureRandom.nextInt(3) == 1) {
                Player player = (Player) victim;
                if (player.getPrayer().get(PrayerType.PROTECT_FROM_MELEE)) {
                    player.getPrayer().toggle(PrayerType.PROTECT_FROM_MELEE);
                    disabledPrayer = true;
                }
                if (player.getPrayer().get(PrayerType.DEFLECT_MELEE)) {
                    player.getPrayer().toggle(PrayerType.DEFLECT_MELEE);
                    disabledPrayer = true;
                }
                if (disabledPrayer) {
                    player.getActionSender().sendMessage("The swing of the anchor disables your prayer!");
                }
            }
            if (secureRandom.nextInt(15) == 1 && entity instanceof NPC) {
                entity.getSkills().heal(10);
            }
            return getType().getSwingHandler().swing(entity, victim, state);
        }

        @Override
        public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
            state.getStyle().getSwingHandler().visualizeImpact(entity, victim, state);
        }


        @Override
        public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
            state.getStyle().getSwingHandler().adjustBattleState(entity, victim, state);
        }

        @Override
        public int calculateAccuracy(Entity entity) {
            return getType().getSwingHandler().calculateAccuracy(entity);
        }

        @Override
        public int calculateDefence(Entity entity, Entity attacker) {
            return getType().getSwingHandler().calculateDefence(entity, attacker);
        }

        @Override
        public int calculateHit(Entity entity, Entity victim, double modifier) {
            return getType().getSwingHandler().calculateHit(entity, victim, modifier);
        }

        @Override
        public void impact(Entity entity, Entity victim, BattleState battleState) {
            CombatStyle combatStyle = battleState.getStyle();
            handleExtraEffects(victim, battleState);
            combatStyle.getSwingHandler().impact(entity, victim, battleState);
        }


        @Override
        public ArmourSet getArmourSet(Entity e) {
            return getType().getSwingHandler().getArmourSet(e);
        }

        @Override
        public double getSetMultiplier(Entity e, int skillId) {
            return getType().getSwingHandler().getSetMultiplier(e, skillId);
        }
    }
}
