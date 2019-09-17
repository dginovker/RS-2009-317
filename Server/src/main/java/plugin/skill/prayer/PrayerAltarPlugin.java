package plugin.skill.prayer;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the <b>Plugin</b> to handle recharging prayer points.
 *
 * @author 'Vexia
 * @since 06/10/2013
 */
public class PrayerAltarPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.setOptionHandler("pray-at", this);
        ObjectDefinition.setOptionHandler("pray", this);
        ObjectDefinition.forId(61).getConfigurations().put("option:check", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (option.equalsIgnoreCase("check")) {
            player.getActionSender().sendMessage("An altar of the evil god Zamorak.");
            return true;
        }
        if (player.getSkills().getPrayerPoints() == player.getSkills().getStaticLevel(Skills.PRAYER)) {
            player.getActionSender().sendMessage("You already have full prayer points.");
            return true;
        }
        player.animate(new Animation(645));
        // player.getAudioManager().send(2674);
        player.lock(2);
        if (player.getSkills().getPrayerPoints() == 0) {
            AchievementDiary.finalize(player, AchievementTask.TO_THE_GODS);
        }
        if (player.getPrayer().get(PrayerType.MYSTIC_MIGHT) &&
            node.getLocation().getX() == 3243 && node.getLocation().getY() == 3207) {
            AchievementDiary.finalize(player, AchievementTask.GODS_GIVE_ME_STRENGTH);
        }
        player.getSkills().rechargePrayerPoints();
        player.getActionSender().sendMessage("You recharge your Prayer points.");
        return true;
    }

}
