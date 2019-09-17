package plugin.combat.spell;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatSpell;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the magic dart spell.
 *
 * @author Emperor
 */
public class MagicDart extends CombatSpell {

    /**
     * Constructs a new {@code MagicDart} {@code Object}.
     */
    public MagicDart() {
        super(SpellType.MAGIC_DART, SpellBook.MODERN, 50, 61.0, 218, 219, new Animation(1576, Priority.HIGH), null, Projectile.create((Entity) null, null, 330, 40, 36, 52, 75, 15, 11), new Graphics(331, 96), Runes.DEATH_RUNE.getItem(1), Runes.MIND_RUNE.getItem(4));
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        if (entity.getSkills().getLevel(Skills.SLAYER) < 55) {
            ((Player) entity).getActionSender().sendMessage("You need a Slayer level of 55 to cast this spell.");
            return false;
        }
        if (((Player) entity).getEquipment().getNew(Equipment.SLOT_WEAPON).getId() != 4170) {
            ((Player) entity).getActionSender().sendMessage("You need to wear a Slayer's staff to cast this spell.");
            return false;
        }
        return super.cast(entity, target);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.MODERN.register(12037, this);
        return this;
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return type.getImpactAmount(entity, victim, 0);
    }
}