package plugin.interaction.inter;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.crafting.SilverProduct;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.net.packet.OperationCode;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the silver crafting making interface.
 *
 * @author 'Vexia
 */
public final class SilverInterface extends ComponentPlugin {

    /**
     * Represents the silver bar item.
     */
    private static final Item SILVER_BAR = new Item(2355);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(13782, this);
        return this;
    }

    @Override
    public boolean handle(final Player p, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != 13782) {
            return false;
        }
        final SilverProduct silver = SilverProduct.forId(itemId);
        if (silver == null) {
            return true;
        }
        if (!p.getInventory().contains(silver.getNeeded(), 1)) {
            p.getActionSender().sendMessage("You need a " + ItemDefinition.forId(silver.getNeeded()).getName().toLowerCase() + " to make this item.");
            return true;
        }
        if (p.getSkills().getLevel(Skills.CRAFTING) < silver.getLevel()) {
            p.getActionSender().sendMessage("You need a crafting level of " + silver.getLevel() + " to make this.");
        }
        int amt = -1;
        switch (opcode) {
            case OperationCode.OPTION_OFFER_ONE:
                amt = 1;
                break;
            case OperationCode.OPTION_OFFER_FIVE:
                amt = 5;
                break;
            case OperationCode.OPTION_OFFER_TEN:
                amt = 10;
                break;
        }
        make(p, silver, amt);
        return true;
    }

    private void make(final Player player, final SilverProduct def, final int ammount) {
        if (!player.getInventory().contains(2355, 1)) {
            return;
        }
        player.getInterfaceState().close();
        World.submit(new Pulse(2, player) {

            int amt = ammount;

            @Override
            public boolean pulse() {
                if (amt < 1) {
                    return true;
                }
                if (!player.getInventory().contains(def.getNeeded(), 1)) {
                    return true;
                }
                if (player.getInventory().containsItem(SILVER_BAR) && player.getInventory().contains(def.getNeeded(), 1)) {
                    player.animate(new Animation(899));
                    if (player.getInventory().remove(/*new Item(def.getNeeded(), 1), */SILVER_BAR)) {
                        player.getInventory().add(new Item(def.getProduct(), 1));
                        player.getSkills().addExperience(Skills.CRAFTING, def.getExp());
                    }
                } else {
                    return true;
                }
                amt--;
                return false;
            }

            @Override
            public void stop() {
                player.animate(new Animation(-1));
            }
        });
    }
}
