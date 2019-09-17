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
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the Dragon, Infernal and 3rd age pickaxe special attack.
 *
 * TODO: find the correct sound(s)
 * TODO: (optional) find the correct anim(s)
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Dragon_pickaxe
 *  - http://oldschoolrunescape.wikia.com/wiki/Infernal_pickaxe
 *  - http://oldschoolrunescape.wikia.com/wiki/3rd_age_pickaxe
 *
 * @author Stan van der Bend
 */
public class RockKnockerSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 100;
    private static final Animation ANIMATION = new Animation(10222, Animator.Priority.HIGH);
    private static final Audio SOUND = new Audio(1);

    @Override public Object fireEvent(String identifier, Object... args) { return null; }

    @Override public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.MELEE.getSwingHandler().register(11920, this); // dragon
        CombatStyle.MELEE.getSwingHandler().register(13243, this); // infernal
        CombatStyle.MELEE.getSwingHandler().register(20014, this); // 3rd age
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {
        Player p = (Player) entity;
        if (!p.getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        p.sendChat("Smashing!");

        p.animate(ANIMATION);
        p.animate(Animation.RESET, 2);

        p.getSkills().updateLevel(Skills.MINING, 3);

        p.getAudioManager().send(SOUND, true);
        return -1;
    }

}
