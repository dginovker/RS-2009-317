package plugin.npc;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatSpell;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.InteractionType;
import org.gielinor.game.node.entity.combat.equipment.ArmourSet;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.link.SpellBookManager;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the catablepon npc type.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CatableponNPC extends AbstractNPC {

    /**
     * Represents the NPC ids of NPCs using this plugin.
     */
    private static final int[] ID = { 4397, 4398, 4399 };

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(4272);

    /**
     * Represents the swing handler.
     */
    private final CatableponSwingHandler combatHandler = new CatableponSwingHandler();

    /**
     * Constructs a new {@code CatableponNPC} {@code Object}.
     */
    public CatableponNPC() {
        super(0, null);
    }

    /**
     * Constructs a new {@code CatableponNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     */
    private CatableponNPC(int id, Location location) {
        super(id, location);
        this.setAggressive(true);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new CatableponNPC(id, location);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return combatHandler;
    }

    @Override
    public int[] getIds() {
        return ID;
    }

    /**
     * Represents the combat swing handler.
     *
     * @author 'Vexia
     */
    public class CatableponSwingHandler extends CombatSwingHandler {

        /**
         * Represents the style to use.
         */
        private CombatStyle style = CombatStyle.MAGIC;

        /**
         * Constructs a new {@code CatableponNPC} {@code Object}.
         */
        public CatableponSwingHandler() {
            super(CombatStyle.MAGIC);
        }

        @Override
        public InteractionType canSwing(Entity entity, Entity victim) {
            return style.getSwingHandler().canSwing(entity, victim);
        }

        @Override
        public int swing(Entity entity, Entity victim, BattleState state) {
            if (RandomUtil.random(7) == 3) {
                style = CombatStyle.MAGIC;
            } else {
                style = CombatStyle.MELEE;
            }
            return 2;
        }

        @Override
        public void visualize(Entity entity, Entity victim, BattleState state) {
            if (style == CombatStyle.MAGIC) {
                state.setSpell(getCombatSpell());
                entity.animate(ANIMATION);
            }
            style.getSwingHandler().visualize(entity, victim, state);
        }

        @Override
        public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
            style.getSwingHandler().visualizeImpact(entity, victim, state);
        }


        @Override
        public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
            style.getSwingHandler().adjustBattleState(entity, victim, state);
        }

        @Override
        public int calculateAccuracy(Entity entity) {
            return style.getSwingHandler().calculateAccuracy(entity);
        }

        @Override
        public int calculateDefence(Entity entity, Entity attacker) {
            return style.getSwingHandler().calculateDefence(entity, attacker);
        }

        @Override
        public int calculateHit(Entity entity, Entity victim, double modifier) {
            return style.getSwingHandler().calculateHit(entity, victim, modifier);
        }

        @Override
        public void impact(Entity entity, Entity victim, BattleState battleState) {
            handleExtraEffects(victim, battleState);
            style.getSwingHandler().impact(entity, victim, battleState);
        }

        @Override
        public ArmourSet getArmourSet(Entity e) {
            return style.getSwingHandler().getArmourSet(e);
        }

        @Override
        public double getSetMultiplier(Entity e, int skillId) {
            return style.getSwingHandler().getSetMultiplier(e, skillId);
        }

        /**
         * Method used to get a combat spell.
         *
         * @return the spell.
         */
        public CombatSpell getCombatSpell() {
            return ((CombatSpell) SpellBookManager.SpellBook.MODERN.getSpell(7));
        }
    }
}
