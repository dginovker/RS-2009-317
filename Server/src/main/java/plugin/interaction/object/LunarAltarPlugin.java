package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the Lunar Altar for spellbook switching.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class LunarAltarPlugin extends OptionHandler {

    /**
     * Handles the interaction option.
     *
     * @param player The player who used the option.
     * @param node   The node the player selected an option on.
     * @param option The option selected.
     * @return <code>True</code> if successful.
     */
    @Override
    public boolean handle(Player player, Node node, String option) {
        player.animate(new Animation(645));
        player.getAudioManager().send(2674);
        player.lock(2);
        player.getActionSender().sendMessage("You pray at the altar.");
        SpellBook spellBook = player.getSpellBookManager().getSpellBook() == SpellBook.LUNAR.getInterfaceId() ? SpellBook.MODERN : SpellBook.LUNAR;
        player.getSpellBookManager().setSpellBook(spellBook);
        player.getInterfaceState().openTab(6, new Component(spellBook.getInterfaceId()));
        if (player.getSkills().getPrayerPoints() == player.getSkills().getStaticLevel(Skills.PRAYER)) {
            return true;
        }
        if (player.getSkills().getPrayerPoints() == 0) {
            AchievementDiary.finalize(player, AchievementTask.TO_THE_GODS);
        }
        player.getSkills().rechargePrayerPoints();
        player.getActionSender().sendMessage("You recharge your Prayer points.");
        return true;
    }

    /**
     * Creates a new instance.
     *
     * @param arg The argument.
     * @return The plugin instance created.
     */
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(17010).getConfigurations().put("option:pray", this);
        return this;
    }
}
