package plugin.combat.spell;

import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatSpell;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the binding combat spell.
 *
 * @author 'Vexia
 * @author Emperor
 * @version 1.0
 */
public final class BindSpell extends CombatSpell {

    /**
     * The start graphic for Earth strike.
     */
    private static final Graphics BIND_START = new Graphics(177, 96);

    /**
     * The projectile for Earth strike.
     */
    private static final Projectile BIND_PROJECTILE = Projectile.create((Entity) null, null, 178, 40, 36, 52, 75, 15, 11);

    /**
     * The end graphic for Earth strike.
     */
    private static final Graphics BIND_END = new Graphics(181, 96);

    /**
     * The start graphic for Earth strike.
     */
    private static final Graphics SNARE_START = new Graphics(177, 96);

    /**
     * The projectile for Earth strike.
     */
    private static final Projectile SNARE_PROJECTILE = Projectile.create((Entity) null, null, 178, 40, 36, 52, 75, 15, 11);

    /**
     * The end graphic for Earth strike.
     */
    private static final Graphics SNARE_END = new Graphics(180, 96);

    /**
     * The start graphic for Earth strike.
     */
    private static final Graphics ENTANGLE_START = new Graphics(177, 96);

    /**
     * The projectile for Earth strike.
     */
    private static final Projectile ENTANGLE_PROJECTILE = Projectile.create((Entity) null, null, 178, 40, 36, 52, 75, 15, 11);

    /**
     * The end graphic for Earth strike.
     */
    private static final Graphics ENTANGLE_END = new Graphics(179, 96);

    /**
     * The cast animation.
     */
    private static final Animation ANIMATION = new Animation(710, Priority.HIGH);

    /**
     * Constructs a new {@code BindSpell} {@Code Object}
     */
    public BindSpell() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code BindSpell} {@code Object}.
     *
     * @param type       the type.
     * @param level      the level.
     * @param sound      the sound.
     * @param start      the start.
     * @param projectile the projectile.
     * @param end        the end.
     * @param runes      the runes.
     */
    private BindSpell(SpellType type, int level, double baseExperience, int sound, Graphics start, Projectile projectile, Graphics end, Item... runes) {
        super(type, SpellBook.MODERN, level, baseExperience, sound, -1, ANIMATION, start, projectile, end, runes);
    }

    @Override
    public void fireEffect(Entity entity, Entity victim, BattleState state) {
        if (state.getEstimatedHit() == -1) {
            return;
        }

        if (getType() == SpellType.BIND) {
            state.setEstimatedHit(-2);
        }
        int tick = state.getSpell().getSpellId() == 1592 ? 25 : state.getSpell().getSpellId() == 1582 ? 16 : 8;
        if (!victim.getLocks().isMovementLocked() && victim instanceof Player) {
            ((Player) victim).getActionSender().sendMessage("A magical force stops you from moving!");
        }
        victim.getWalkingQueue().reset();
        victim.getLocks().lockMovement(tick);
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return getType() == SpellType.ENTANGLE ? 5 : 3;
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
        SpellBook.MODERN.register(1572, new BindSpell(SpellType.BIND, 20, 30.0, 151, BIND_START, BIND_PROJECTILE, BIND_END, Runes.NATURE_RUNE.getItem(2), Runes.EARTH_RUNE.getItem(3), Runes.WATER_RUNE.getItem(3)));
        SpellBook.MODERN.register(1582, new BindSpell(SpellType.SNARE, 50, 60.0, 152, SNARE_START, SNARE_PROJECTILE, SNARE_END, Runes.NATURE_RUNE.getItem(3), Runes.EARTH_RUNE.getItem(4), Runes.WATER_RUNE.getItem(4)));
        SpellBook.MODERN.register(1592, new BindSpell(SpellType.ENTANGLE, 79, 89.0, 153, ENTANGLE_START, ENTANGLE_PROJECTILE, ENTANGLE_END, Runes.NATURE_RUNE.getItem(4), Runes.EARTH_RUNE.getItem(5), Runes.WATER_RUNE.getItem(5)));
        return this;
    }

}