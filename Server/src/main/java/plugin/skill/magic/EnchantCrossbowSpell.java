package plugin.skill.magic;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InterfaceColourContext;
import org.gielinor.net.packet.out.InterfaceColour;
import org.gielinor.rs2.plugin.Plugin;

import plugin.interaction.inter.BoltEnchantingInterface;
import plugin.interaction.inter.BoltEnchantingInterface.Bolts;

/**
 * Represents the enchant crossbow spell.
 *
 * @author 'Vexia.
 * @version 1.0
 */
public final class EnchantCrossbowSpell extends MagicSpell {

    /**
     * Constructs a new {@code EnchantCrossbowSpell} {@code Object}.
     */
    public EnchantCrossbowSpell() {
        super(SpellBook.MODERN, 4, 0, null, null, null, null);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.MODERN.register(19207, this);
        return null;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        final Player player = ((Player) entity);
        int childId = 47238;
        int runeTextId = 47202;
        for (Bolts bolts : Bolts.values()) {
            player.getActionSender().sendHideComponent(bolts.getButton(), false);
            if (player.getSkills().getLevel(Skills.MAGIC) >= bolts.getLevel()) {
                PacketRepository.send(InterfaceColour.class, new InterfaceColourContext(player, childId, 0x3366));
            } else {
                PacketRepository.send(InterfaceColour.class, new InterfaceColourContext(player, childId, 0x6000));
                player.getActionSender().sendHideComponent(bolts.getButton(), true);
            }
            if (!player.getInventory().contains(bolts.getBolt()) || !player.getInventory().containsItems(bolts.getRunes())) {
                player.getActionSender().sendHideComponent(bolts.getButton(), true);
            }
            // Rune cases
            for (Item rune : bolts.getRunes()) {
                boolean usingStaff = BoltEnchantingInterface.usingStaff(player, rune.getId()) || player.getInventory().getCount(rune) > 65535;
                boolean hasRune = player.getInventory().containsItems(rune) || usingStaff;
                PacketRepository.send(InterfaceColour.class, new InterfaceColourContext(player, runeTextId, hasRune ? 0x3366 : 0x6000));
                int count = hasRune ? rune.getCount() : player.getInventory().getCount(rune);
                player.getActionSender().sendString((usingStaff ? "*" : count) + "/" + rune.getCount(), runeTextId);
                runeTextId++;
            }
            childId++;
        }
        player.getInterfaceState().open(new Component(47200));
        return true;
    }

}
