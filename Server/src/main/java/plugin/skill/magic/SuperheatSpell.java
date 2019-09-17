package plugin.skill.magic;

import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.content.skill.free.smithing.smelting.SmeltingPulse;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

import plugin.interaction.inter.SmeltingInterface.Smeltable;

/**
 * Represents the super heat spell.
 *
 * @author 'vexia
 * @version 1.0
 */
public final class SuperheatSpell extends MagicSpell {

    /**
     * Constructs a new {@code SuperheatSpell} {@code Object}.
     */
    public SuperheatSpell() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code SuperheatSpell} {@code Object}.
     *
     * @param level the level.
     * @param runes the runes.
     */
    public SuperheatSpell(int level, Item... runes) {
        super(SpellBook.MODERN, level, 53.0, null, null, null, runes);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.MODERN.register(1173, new SuperheatSpell(43, new Item(Runes.FIRE_RUNE.getId(), 4), new Item(Runes.NATURE_RUNE.getId(), 1)));
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        final Player player = ((Player) entity);
        final Item item = ((Item) target);
        player.getInterfaceState().setViewedTab(6);
        if (player.inCombat()) {
            player.getActionSender().sendMessage("You can't do that during combat.");
            return false;
        }
        if (!item.getName().contains("ore")) {
            player.getActionSender().sendMessage("You need to cast superheat item on ore.");
            return false;
        }
        Smeltable smeltable = Smeltable.forItemId(item.getId());
        if (smeltable == null) {
            player.getActionSender().sendMessage("Something went wrong, please report this on the forums.");
            return false;
        }
        // Steel bar check
        if (smeltable == Smeltable.IRON) {
            if (player.getInventory().contains(453, 2)) {
                smeltable = Smeltable.STEEL;
            }
        }
        if (!player.getInventory().contains(smeltable.getPrimary())) {
            player.getActionSender().sendMessage("You do not have the required ores to make this bar.");
            return false;
        }

        if (smeltable.getSecondary() != null && !player.getInventory().contains(smeltable.getSecondary())) {
            player.getActionSender().sendMessage("You do not have the required ores to make this bar.");
            return false;
        }
        if (meetsRequirements(entity, true, true)) {
            player.getAudioManager().send(117);
            player.getPulseManager().run(new SmeltingPulse(player, item, smeltable, 1, true));
        }
        return true;
    }

}