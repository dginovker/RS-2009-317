package plugin.combat.special;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.impl.Animator;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the Dragon and Infernal harpoon special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Dragon_harpoon
 *  - http://oldschoolrunescape.wikia.com/wiki/Infernal_harpoon
 *
 * @author Stan van der Bend
 */
public class FishstabberSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 100;
    private static final Animation ANIMATION = new Animation(1056, Animator.Priority.HIGH);
    private static final Graphics GRAPHICS = new Graphics(246);
    private static final Audio SOUND = new Audio(386);

    @Override public Object fireEvent(String identifier, Object... args) { return null; }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.MELEE.getSwingHandler().register(21028, this); // dragon
        CombatStyle.MELEE.getSwingHandler().register(21031, this); // infernal
        return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        Player p = (Player) entity;
        if (!p.getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        p.sendChat("Here fishy fishies!");

        p.visualize(ANIMATION, GRAPHICS);

        p.getSkills().updateLevel(Skills.FISHING, 3);

        p.getAudioManager().send(SOUND, true);
        return -1;
    }

}
