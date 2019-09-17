package plugin.combat.special;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the Dragon battleaxe special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Dragon_battleaxe
 *
 * @author Emperor
 * @version 1.0
 */
public final class RampageSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 100;
    private static final Animation ANIMATION = new Animation(1056, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(246);

    @Override public Object fireEvent(String identifier, Object... args) {
        switch (identifier) {
            case "instant_spec":
            case "ncspec":
                return true;
        }
        return null;
    }

    @Override public Plugin<Object> newInstance(Object arg) {
        CombatStyle.MELEE.getSwingHandler().register(1377, this);
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {
        Player p = (Player) entity;
        if (!p.getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        p.sendChat("Raarrrrrgggggghhhhhhh!");
        p.visualize(ANIMATION, GRAPHIC);
        @SuppressWarnings("unused")
        int boost = 0;
        for (int i = 0; i < 6; i++) {
            if (i == 2 || i == 3 || i == 5) {
                continue;
            }
            double drain = p.getSkills().getLevel(i) * 0.1;
            boost += drain;
            p.getSkills().updateLevel(i, (int) -drain, (int) (p.getSkills().getStaticLevel(i) - drain));
        }
        boost *= 0.25;
        p.getSkills().updateLevel(Skills.STRENGTH, (int) (p.getSkills().getStaticLevel(Skills.STRENGTH) * 0.20));
        p.getAudioManager().send(new Audio(386), true);
        return -1;
    }

}
