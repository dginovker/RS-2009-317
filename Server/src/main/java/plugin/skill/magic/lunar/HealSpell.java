package plugin.skill.magic.lunar;

import java.util.Iterator;
import java.util.List;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the healing spell.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class HealSpell extends MagicSpell {

    /**
     * The heal other spell button id.
     */
    private static final int HEAL_OTHER = 30290;
    /**
     * The heal group spell button id.
     */
    private static final int HEAL_GROUP = 30314;

    /**
     * Represents the animation for this spell.
     */
    private static final Animation ANIMATION = new Animation(4411);

    /**
     * Represents the graphics of this spell.
     */
    private static final Graphics GRAPHICS = new Graphics(738, 90);

    /**
     * Represents the animation used for the group heal.
     */
    private static final Animation ANIMATION_G = new Animation(1979);

    /**
     * Represents the graphics used for the group heal.
     */
    private static final Graphics GRAPHICS_G = new Graphics(734, 90);

    /**
     * Constructs a new {@code HealSpell} {@code Object}.
     */
    public HealSpell() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code HealSpell} {@code Object}.
     */
    public HealSpell(int level, int experience, Item[] runes) {
        super(SpellBook.LUNAR, level, experience, null, null, null, runes);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.LUNAR.register(HEAL_OTHER, new HealSpell(92, 101, new Item[]{ new Item(Runes.BLOOD_RUNE.getId(), 1), new Item(Runes.LAW_RUNE.getId(), 3), new Item(Runes.ASTRAL_RUNE.getId(), 3) }));
        SpellBook.LUNAR.register(HEAL_GROUP, new HealSpell(92, 101, new Item[]{ new Item(Runes.BLOOD_RUNE.getId(), 3), new Item(Runes.LAW_RUNE.getId(), 6), new Item(Runes.ASTRAL_RUNE.getId(), 4) }));
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        final Player player = (Player) entity;
        boolean group = getSpellId() == HEAL_GROUP;
        if (!group) {
            if (!(target instanceof Player)) {
                return false;
            }
            final Player o = (Player) target;
            player.face(o);
            if (!o.isActive() || o.getLocks().isInteractionLocked()) {
                player.getActionSender().sendMessage("This player is busy.");
                return false;
            }
            if (!o.getSettings().isAcceptAid()) {
                if (!((Player) entity).specialDetails()) {
                    player.getActionSender().sendMessage("The player is not accepting any aid.");
                    return false;
                }
            }
            if (o.getSkills().getLifepoints() == o.getSkills().getLevel(Skills.HITPOINTS)) {
                player.getActionSender().sendMessage("The player already has full hitpoints.");
                return false;
            }
            if (!super.meetsRequirements(player, true, true)) {
                return false;
            }
            int transfer = (int) (player.getSkills().getLifepoints() * 75) / 100;
            if (transfer + o.getSkills().getLifepoints() > 99) {
                transfer = transfer + o.getSkills().getLifepoints() + 99 - 100;
            }
            player.getImpactHandler().manualHit(player, transfer / 10, null);
            o.getSkills().heal(transfer);
            player.animate(ANIMATION);
            o.graphics(GRAPHICS);
        } else {
            List<Player> players = RegionManager.getLocalPlayers(player, 1);
            int percentage = (int) Math.ceil(player.getSkills().getLifepoints() * 0.75);
            for (Iterator<Player> it = players.iterator(); it.hasNext(); ) {
                Player p = it.next();
                if (p == player || !p.getSettings().isAcceptAid() || !p.isActive() ||
                    p.getSkills().getLifepoints() == p.getSkills().getMaximumLifepoints()) {
                    if (!player.specialDetails()) {
                        it.remove();
                    }
                }
            }
            if (players.isEmpty()) {
                player.getActionSender().sendMessage("There are no players around to replenish.");
                return false;
            }
            if (percentage < 1) {
                player.getActionSender().sendMessage("You don't have enough hitpoints left to cast this spell.");
                return false;
            }
            if (!super.meetsRequirements(player, true, true)) {
                return false;
            }
            int playerPercentage = (int) (percentage / players.size());
            player.getImpactHandler().manualHit(player, percentage, HitsplatType.NORMAL);
            player.animate(ANIMATION_G);
            for (Player p : players) {
                p.graphics(GRAPHICS_G);
                p.getSkills().heal(playerPercentage);
            }
        }
        return true;
    }

}
