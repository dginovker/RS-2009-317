package plugin.combat.spell;

import org.gielinor.cache.def.impl.ItemDefinition;
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
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the god spells.
 *
 * @author Emperor
 * @author 'Vexia
 */
public final class GodSpell extends CombatSpell {

    /**
     * The start graphic for Air strike.
     */
    private static final Graphics STRIKE_START = null;

    /**
     * The projectile for Air strike.
     */
    private static final Projectile STRIKE_PROJECTILE = null;

    /**
     * The end graphic for Air strike.
     */
    private static final Graphics STRIKE_END = new Graphics(76, 0);

    /**
     * The start graphic for Air strike.
     */
    private static final Graphics GUTHIX_START = null;

    /**
     * The projectile for Air strike.
     */
    private static final Projectile GUTHIX_PROJECTILE = null;

    /**
     * The end graphic for Air strike.
     */
    private static final Graphics GUTHIX_END = new Graphics(77, 0);

    /**
     * The start graphic for Air strike.
     */
    private static final Graphics ZAM_START = null;

    /**
     * The projectile for Air strike.
     */
    private static final Projectile ZAM_PROJECTILE = null;

    /**
     * The end graphic for Air strike.
     */
    private static final Graphics ZAM_END = new Graphics(78, 0);

    /**
     * The cast animation.
     */
    private static final Animation ANIMATION = new Animation(811, Priority.HIGH);

    /**
     * Constructs a new {@code AirSpell} {@Code Object}
     */
    public GodSpell() {
        /*
         * empty.
         */
    }

    /**
     * Constructs a new {@code AirSpell} {@Code Object}
     *
     * @param type  The spell type.
     * @param start The start graphics.
     * @param end   The end graphics.
     */
    private GodSpell(SpellType type, int sound, Graphics start, Projectile projectile, Graphics end, Item... runes) {
        super(type, SpellBook.MODERN, 60, 35.0, -1, -1, ANIMATION, start, projectile, end, runes);
    }

    @Override
    public boolean meetsRequirements(Entity caster, boolean message, boolean remove) {
        if (caster instanceof Player) {
            int staffId = ((Player) caster).getEquipment().getNew(Equipment.SLOT_WEAPON).getId();
            int required = -1;
            switch (runes[1].getCount()) {
                case 2: //Saradomin strike
                    required = 2415;
                    break;
                case 1: //Guthix claws
                    required = 2416;
                    break;
                case 4: //Flames of Zamorak
                    required = 2417;
                    break;
            }
            if (staffId != required) {
                if (message) {
                    ((Player) caster).getActionSender().sendMessage("You need to wear a " + ItemDefinition.forId(required).getName() + " to cast this spell.");
                }
                return false;
            }
        }
        return super.meetsRequirements(caster, message, remove);
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return getType().getImpactAmount(entity, victim, 0);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
        SpellBook.MODERN.register(1190, new GodSpell(SpellType.GOD_STRIKE, -1, STRIKE_START, STRIKE_PROJECTILE, STRIKE_END, Runes.BLOOD_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(2), Runes.AIR_RUNE.getItem(4)));
        SpellBook.MODERN.register(1191, new GodSpell(SpellType.GOD_STRIKE, -1, GUTHIX_START, GUTHIX_PROJECTILE, GUTHIX_END, Runes.BLOOD_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(1), Runes.AIR_RUNE.getItem(4)));
        SpellBook.MODERN.register(1192, new GodSpell(SpellType.GOD_STRIKE, -1, ZAM_START, ZAM_PROJECTILE, ZAM_END, Runes.BLOOD_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(4), Runes.AIR_RUNE.getItem(1)));
        return this;
    }

}
