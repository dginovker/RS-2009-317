package plugin.skill.magic.lunar;

import java.util.List;
import java.util.Optional;

import org.gielinor.game.content.global.consumable.Consumable;
import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.global.consumable.Drink;
import org.gielinor.game.content.global.consumable.Potion;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.ai.AIPlayer;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The stat restore spell.
 *
 * @author 'Vexia
 */
public class StatRestoreSpell extends MagicSpell {

    /**
     * Represents the animation of this spell.
     */
    private static final Animation ANIMATION = new Animation(4413);

    /**
     * Represents the graphics.
     */
    private static final Graphics GRAPHICS = new Graphics(733, 130);

    /**
     * The ids of the spell.
     */
    private static final int[] IDS = new int[]{ 2430, 127, 129, 131, 3024, 3026, 3028, 3030 };

    /**
     * Constructs a new {@code StatRestoreSpell} {@code Object}.
     */
    public StatRestoreSpell() {
        super(SpellBook.LUNAR, 81, 84, null, null, null, new Item[]{ new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.EARTH_RUNE.getId(), 10), new Item(Runes.WATER_RUNE.getId(), 10) });
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.LUNAR.register(30194, this);
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {

        final Player player = entity.asPlayer();
        final Item item = target.asItem();
        final Optional<Consumable> optional = Consumables.findDrink(item).filter(Consumable::isDrink);

        player.getInterfaceState().setViewedTab(6);

        if (!optional.isPresent()) {
            player.getActionSender().sendMessage("For use of this spell use only one a potion.");
            return false;
        }
        if (!item.getDefinition().isTradeable() || !isRestore(item.getId())) {
            player.getActionSender().sendMessage("You can't cast this spell on that item.");
            return false;
        }

        final Potion potion = optional.get().asDrink().asPotion();

        final List<Player> localPlayers = RegionManager.getLocalPlayers(player, 1);

        final int
            plSize = localPlayers.size() - 1,
            doses = potion.getDose(item);

        if (plSize > doses) {
            player.getActionSender().sendMessage("You don't have enough doses.");
            return false;
        }

        if (localPlayers.size() == 0)
            return false;

        if (!super.meetsRequirements(player, true, false))
            return false;

        int size = 1;

        for (Player otherPlayer : localPlayers) {

            if (!otherPlayer.isActive() || otherPlayer.getLocks().isInteractionLocked() || otherPlayer == player)
                continue;

            if (!otherPlayer.getSettings().isAcceptAid() && !(otherPlayer instanceof AIPlayer))
                if (!((Player) entity).specialDetails())
                    continue;

            otherPlayer.graphics(GRAPHICS);
            potion.effect(otherPlayer, item);
            size++;
        }
        if (size == 1) {
            player.getActionSender().sendMessage("There is nobody around to share the potion with you.");
            return false;
        }
        super.meetsRequirements(player, true, true);

        potion.effect(player, item);
        potion.message(player, item, player.getSkills().getLifepoints());

        player.animate(ANIMATION);
        player.graphics(GRAPHICS);

        potion.remove(player, item, size - 1, true);
        return true;
    }

    /**
     * Checks if it is a restore item.
     *
     * @param id the id.
     * @return <code>True</code> if so.
     */
    private boolean isRestore(int id) {
        for (int i : IDS) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }

}
