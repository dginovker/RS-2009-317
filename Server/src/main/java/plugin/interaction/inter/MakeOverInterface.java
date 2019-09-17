package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.appearance.Gender;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.game.world.update.flag.player.AppearanceFlag;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the interface used to handle the buttons related to the make over mage interface.
 *
 * @author 'Vexia
 * @date 26/12/2013
 */
public class MakeOverInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(5454, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        handle(player, button);
        return true;
    }

    /**
     * Method used to confirm the order.
     *
     * @param player the player.
     */
    public static void confirm(Player player) {
        boolean male = player.getAttribute("design:male", player.getAppearance().isMale());
        int skin = player.getAttribute("design:skin", player.getAppearance().getSkin().getColour());
        if (!player.getInventory().containsItem(new Item(Item.COINS, 3000))) {
            return;
        }
        if (skin != -1 && player.getInventory().remove(new Item(Item.COINS, 3000))) {
            Gender gender = male ? Gender.MALE : Gender.FEMALE;
            if (gender != player.getAppearance().getGender()) {
                player.getAppearance().changeGender(gender);
            }
            player.getAppearance().getSkin().setColour(skin);
            player.getUpdateMasks().register(new AppearanceFlag(player));
            player.getInterfaceState().close();
            player.removeAttribute("design:male");
            player.removeAttribute("design:skin");
            player.getDialogueInterpreter().open(2676, Repository.findNPC(2676), 1);
        }
    }

    /**
     * Method used to handle the buttons.
     *
     * @param player the player.
     * @param button the button.
     */
    public static void handle(Player player, int button) {
        int skin = 0;
        switch (button) {
            case 5559:
                player.setAttribute("design:male", false);
                break;
            case 5557:
                player.setAttribute("design:male", true);
                break;
            case 5556:
                skin = 7;
                break;
            case 5549:
                skin = 1;
                break;
            case 5550:
                skin = 2;
                break;
            case 5551:
                break;
            case 5552:
                skin = 3;
                break;
            case 5553:
                skin = 4;
                break;
            case 5554:
                skin = 5;
                break;
            case 5555:
                skin = 6;
                break;
            case 5544:
                confirm(player);
                break;
        }
        if (button != 5544 && button != 5547 && button != 5549) {
            player.setAttribute("design:skin", skin);
        }
    }
}
