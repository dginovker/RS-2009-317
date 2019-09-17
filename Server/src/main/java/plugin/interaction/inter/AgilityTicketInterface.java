package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the plugin used to handle the agility ticket interface.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class AgilityTicketInterface extends ComponentPlugin {

    /**
     * Represents the pirate hook item.
     */
    private static final Item PIRATE_HOOK = new Item(2997);

    /**
     * Represents the toadflax item.
     */
    private static final Item TOADFLAX = new Item(2998);

    /**
     * Represents the snapdragon item.
     */
    private static final Item SNAPDRAGON = new Item(3000);

    /**
     * Represents the aerna ticket.
     */
    private static final int ARENA_TICKET = 2996;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.forId(8292).setPlugin(this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        Item reward = null;
        double experience = 0;
        int tickets = 0;
        switch (button) {
            /**
             * 1 Experience.
             */
            case 8387:
            case 8388:
                experience = 1;
                tickets = 1;
                break;
            /**
             * 10 Experience.
             */
            case 8389:
            case 8395:
                experience = 10;
                tickets = 10;
                break;
            /**
             * 25 Experience.
             */
            case 8390:
            case 8396:
                experience = 25;
                tickets = 25;
                break;
            /**
             * 100 Experience.
             */
            case 8391:
            case 8397:
                experience = 100;
                tickets = 100;
                break;
            /**
             * 1000 Experience.
             */
            case 8392:
            case 8398:
                experience = 1000;
                tickets = 1000;
                break;
            /**
             * Toadflax.
             */
            case 8382:
            case 8384:
                reward = TOADFLAX;
                tickets = 3;
                break;
            /**
             * Snapdragon.
             */
            case 8393:
            case 8394:
                reward = SNAPDRAGON;
                tickets = 10;
                break;
            /**
             * Pirate's hook.
             */
            case 8381:
            case 8385:
                reward = PIRATE_HOOK;
                tickets = 800;
                break;
        }
        if (experience > 0 && !player.getInventory().contains(ARENA_TICKET, tickets)) {
            player.getActionSender().sendMessage("This Agility experience costs " + tickets + " tickets.");
            return true;
        }
        if (reward != null && !player.getInventory().contains(ARENA_TICKET, tickets)) {
            player.getActionSender().sendMessage(TextUtils.formatDisplayName(reward.getName().replace("Clean", "").trim()) + " costs " + tickets + " tickets.");
            return true;
        }
        if (!player.getInventory().contains(ARENA_TICKET, tickets)) {
            return false;
        }
        if (experience > 0) {
            if (!player.getInventory().containsItem(new Item(ARENA_TICKET, tickets))) {
                return false;
            }
            if (player.getInventory().remove(new Item(ARENA_TICKET, tickets))) {
                player.getSkills().addExperience(Skills.AGILITY, experience);
                player.getActionSender().sendMessage("You have been granted some Agility experience!");
            }
            return true;
        }
        if (reward != null) {
            if (player.getInventory().remove(new Item(ARENA_TICKET, tickets))) {
                player.getInventory().add(reward);
                player.getActionSender().sendMessage("You have been granted a " + TextUtils.formatDisplayName(reward.getName().replace("Clean", "").trim()) + ".");
            }
            return true;
        }
        return false;
    }

}
