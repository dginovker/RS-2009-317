package plugin.skill.magic.lunar;

import java.util.Iterator;
import java.util.List;

import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the vengeance (other) spell.
 *
 * @author Emperor
 */
public final class VengeanceSpell extends MagicSpell {

    /**
     * The vengeance other spell button id.
     */
    private static final int VENGEANCE_OTHER = 30298;
    /**
     * The vengeance spell button id.
     */
    private static final int VENGEANCE = 30306;
    /**
     * The vengeance group spell button id.
     */
    private static final int VENGEANCE_GROUP = 30330;

    /**
     * Constructs a new {@code VengeanceSpell} {@code Object}.
     */
    public VengeanceSpell() {

    }

    /**
     * Constructs a new {@code VengeanceSpell} {@code Object}.
     *
     * @param level      The level required.
     * @param experience The experience gained from casting this spell.
     * @param anim       The cast animation.
     * @param graphic    The graphics.
     * @param runes      The runes required.
     */
    public VengeanceSpell(int level, double experience, Animation anim, Graphics graphic, Item... runes) {
        super(SpellBook.LUNAR, level, experience, anim, graphic, null, runes);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.LUNAR.register(VENGEANCE_OTHER, new VengeanceSpell(93, 78, Animation.create(4411), new Graphics(725, 96), Runes.ASTRAL_RUNE.getItem(3), Runes.DEATH_RUNE.getItem(2), Runes.EARTH_RUNE.getItem(10)));
        SpellBook.LUNAR.register(VENGEANCE, new VengeanceSpell(94, 112, Animation.create(4410), new Graphics(726, 96), Runes.ASTRAL_RUNE.getItem(4), Runes.DEATH_RUNE.getItem(2), Runes.EARTH_RUNE.getItem(10)));
        SpellBook.LUNAR.register(VENGEANCE_GROUP, new VengeanceSpell(93, 78, Animation.create(4411), new Graphics(725, 96), Runes.ASTRAL_RUNE.getItem(3), Runes.DEATH_RUNE.getItem(2), Runes.EARTH_RUNE.getItem(10)));

        return this;
    }

    @Override
    public void visualize(Entity entity, Node target) {
        entity.animate(animation);
        ((Entity) target).graphics(graphic);
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        int ticks = World.getTicks();
        boolean vengOther = spellId == VENGEANCE_OTHER;
        boolean vengGroup = spellId == VENGEANCE_GROUP;
        if (!((Player) entity).specialDetails()) {
            if (entity.getAttribute("vengeance_delay", -1) > ticks) {
                ((Player) entity).getActionSender().sendMessage("You can only cast vengeance spells once every 30 seconds.");
                return false;
            }
        }
        if (vengGroup) {
            Player player = (Player) entity;
            List<Player> players = RegionManager.getLocalPlayers(player, 4);
            int index = 0;
            for (Iterator<Player> it = players.iterator(); it.hasNext(); ) {
                Player p = it.next();
                boolean acceptAid = p.getSettings().isAcceptAid();
                if (!((Player) entity).specialDetails()) {
                    acceptAid = true;
                }
                if (p.isActive() && acceptAid) {
                    index++;
                    continue;
                }
                if (index == 50) {
                    break;
                }
                it.remove();
            }
            if (!super.meetsRequirements(player, true, true)) {
                return false;
            }
            player.playAnimation(animation);
            player.playGraphics(new Graphics(726, 96));
            player.getAudioManager().send(2907, true);
            for (Player p : players) {
                if (p != player) {
                    p.graphics(graphic);
                }
                entity.setAttribute("vengeance_delay", ticks + 50);
                p.setAttribute("vengeance", true);
            }
            return true;
        }
        if (vengOther && (target == null || !(target instanceof Player))) {
            return false;
        }
        Player p = (Player) (vengOther ? target : entity);
        if (vengOther) {
            if (!p.getSettings().isAcceptAid()) {
                if (!((Player) entity).specialDetails()) {
                    ((Player) entity).getActionSender().sendMessage("The player is not accepting any aid.");
                    return false;
                }
            }
        }
        if (!meetsRequirements(entity, true, true)) {
            return false;
        }
        visualize(entity, p);
        entity.setAttribute("vengeance_delay", ticks + 50);
        p.setAttribute("vengeance", true);
        p.getAudioManager().send(vengOther ? 2907 : 2908);
        return true;
    }

}