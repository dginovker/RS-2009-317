package plugin.interaction.inter;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.free.crafting.Hide;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents the plugin for the {@link org.gielinor.game.content.skill.free.crafting.Hide} tanning interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class TanningInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(14670, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int childId, int slot, int itemId) {
        Hide hide = Hide.forId(childId);
        if (hide == null) {
            return false;
        }
        int amount = Hide.getAmount(childId);
        if (amount == 0) {
            return false;
        }
        if (amount == -1) {
            player.setAttribute("runscript", new RunScript() {

                @Override
                public boolean handle() {
                    int amount = (int) getValue();
                    tan(player, amount, hide);
                    return true;
                }
            });
            player.getDialogueInterpreter().sendInput(false, "Enter amount:");
            return true;
        }
        tan(player, amount, hide);
        return true;
    }

    /**
     * Thans the given {@link Hide}.
     *
     * @param player The player.
     * @param amount The amount to tan.
     * @param hide   The hide.
     */
    public static void tan(final Player player, int amount, Hide hide) {
        if (amount > player.getInventory().getCount(hide.getRaw())) {
            amount = player.getInventory().getCount(hide.getRaw());
        }
        int coins = hide.getCost();
        if (!player.getInventory().contains(hide.getRaw().getId(), amount)) {
            player.getActionSender().sendMessage("You don't have any " + hide.getRaw().getName().toLowerCase() + " to tan.");
            return;
        }
        if (amount == 0) {
            return;
        }
        player.getInterfaceState().close();
        if (!player.getInventory().contains(Item.COINS, coins * amount)) {
            player.getActionSender().sendMessage("You don't have enough coins to tan that many.");
            return;
        }
        if (player.getInventory().remove(new Item(Item.COINS, coins * amount)) && player.getInventory().remove(new Item(hide.getRaw().getId(), amount))) {
            player.getInventory().add(new Item(hide.getProduct().getId(), amount));
            if (amount > 1) {
                player.getActionSender().sendMessage("The tanner tans " + amount + " " + ItemDefinition.forId(hide.getRaw().getId()).getName().toLowerCase() + "s for you.");
            } else {
                player.getActionSender().sendMessage("The tanner tans your " + ItemDefinition.forId(hide.getRaw().getId()).getName().toLowerCase() + ".");
            }
        } else {
            player.getActionSender().sendMessage("You don't have enough coins to tan that many.");
        }
    }

    /**
     * Opens the interface for tanning a {@link org.gielinor.game.content.skill.free.crafting.Hide}.
     *
     * @param player The player.
     */
    public static void openInterface(Player player) {
        int itemSlot = 14769;
        int nameId = 14777;
        int costId = 14785;
        for (Hide h : Hide.values()) {
            String nameColor = player.getInventory().contains(h.getRaw()) ? "<col=00FF00>" : "<col=FFB000>";
            String costColor = player.getInventory().contains(new Item(Item.COINS, h.getCost())) ? "<col=00FF00>" : "<col=FFB000>";
            player.getActionSender().sendString(nameColor + h.getName(), nameId);
            player.getActionSender().sendString(costColor + h.getCost() + " Coins", costId);
            player.getActionSender().sendItemZoomOnInterface(h.getRaw().getId(), 200, itemSlot);
            itemSlot++;
            nameId++;
            costId++;
        }
        player.getInterfaceState().open(new Component(14670));
    }

}
