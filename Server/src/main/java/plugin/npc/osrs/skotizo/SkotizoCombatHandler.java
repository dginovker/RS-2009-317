package plugin.npc.osrs.skotizo;

import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.equipment.SwitchAttack;
import org.gielinor.game.node.entity.combat.handlers.MultiSwingHandler;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;

/**
 * Handles the combat behaviour of {@link SkotizoNPC}.
 *
 * Created by Stan van der Bend on 04/01/2018.
 *
 * project: GielinorGS
 * package: plugin.npc.osrs.skotizo
 */
public class SkotizoCombatHandler extends MultiSwingHandler{

    private final static Animation
        MELEE_ATTACK_ANIMATION = new Animation(4680),
        MAGIC_ATTACK_ANIMATION = new Animation(69);

    private final static Graphics
        BOULDER_START_GRAPHICS = new Graphics(857);

    private final static Projectile
        BUILDER_PROJECTILE = Projectile.create(null, null, 856, 88, 31, 30, 2);

    public SkotizoCombatHandler() {
        super(AVAILABLE_ATTACKS);
    }

    private final static SwitchAttack[] AVAILABLE_ATTACKS = new SwitchAttack[]{
        new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), MELEE_ATTACK_ANIMATION),
        new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), MAGIC_ATTACK_ANIMATION, BOULDER_START_GRAPHICS, BUILDER_PROJECTILE)
    };

}
