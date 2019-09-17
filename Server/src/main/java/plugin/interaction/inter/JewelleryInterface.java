package plugin.interaction.inter;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.crafting.jewellery.JewelleryCrafting;
import org.gielinor.game.content.skill.free.crafting.jewellery.JewelleryCrafting.JewelleryItem;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the interface plugin used for jewellery crafting.
 *
 * @author 'Vexia
 */
public final class JewelleryInterface extends ComponentPlugin {

    /**
     * Represents constants of useful items.
     */
    public static final int RING_MOULD = 1592, AMULET_MOULD = 1595,
        NECKLACE_MOULD = 1597, BRACELET_MOULD = 11065, GOLD_BAR = 2357,
        SAPPHIRE = 1607, EMERALD = 1605, RUBY = 1603, DIAMOND = 1601, DRAGONSTONE = 1615, ONYX = 6573;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(23335, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != 23335) {
            return false;
        }
        int amount = 0;
        JewelleryItem jewelleryItem = JewelleryItem.forProduct(itemId);
        if (jewelleryItem == null) {
            player.getActionSender().sendMessage("You don't have the required items to make this.");
            return true;
        }
        if (player.getSkills().getLevel(Skills.CRAFTING) < jewelleryItem.getLevel()) {
            String an = TextUtils.isPlusN(ItemDefinition.forId(jewelleryItem.getSendItem()).getName().toLowerCase()) ? "an" : "a";
            player.getActionSender().sendMessage("You need a crafting level of " + jewelleryItem.getLevel() + " to craft " + an + " " + ItemDefinition.forId(jewelleryItem.getSendItem()).getName().toLowerCase() + ".");
            return true;
        }
        String name = ItemDefinition.forId(jewelleryItem.getSendItem()).getName().toLowerCase();
        boolean flag = false;
        if (name.contains("ring") && !player.getInventory().contains(RING_MOULD, 1)) {
            flag = true;
        }
        if (name.contains("necklace") && !player.getInventory().contains(NECKLACE_MOULD, 1)) {
            flag = true;
        }
        if (name.contains("amulet") && !player.getInventory().contains(AMULET_MOULD, 1)) {
            flag = true;
        }
        if (name.contains("bracelet") && !player.getInventory().contains(BRACELET_MOULD, 1)) {
            flag = true;
        }
        if (flag) {
            player.getActionSender().sendMessage("You don't have the required mould to make this.");
            return flag;
        }
        switch (opcode) {
            case 145:
                amount = 1;
                break;
            case 117:
                amount = 5;
                break;
            case 43:
                if (jewelleryItem.name().contains("GOLD")) {
                    amount = player.getInventory().getCount(new Item(GOLD_BAR));
                } else {
                    int first = player.getInventory().getCount(new Item(jewelleryItem.getItems()[0]));
                    int second = player.getInventory().getCount(new Item(jewelleryItem.getItems()[1]));
                    if (first == second) {
                        amount = first;
                    } else if (first > second) {
                        amount = second;
                    } else {
                        amount = first;
                    }
                }
                break;
            case 129:
                final JewelleryItem d = jewelleryItem;
                player.setAttribute("runscript", new RunScript() {

                    @Override
                    public boolean handle() {
                        JewelleryCrafting.make(player, d, (int) getValue());
                        return true;
                    }
                });
                player.getDialogueInterpreter().sendInput(false, "Enter amount:");
                return true;
        }
        JewelleryCrafting.make(player, jewelleryItem, amount);
        return true;
    }

}
