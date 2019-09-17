package plugin.interaction.item.withobject;

import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * @author 'Vexia
 */
public class PriestInPerilUsePlugin extends UseWithHandler {

    /**
     * Constructs a new {@code PriestInPerilUsePlugin.java} {@code Object}.
     */
    public PriestInPerilUsePlugin() {
        super(2944, 1925, 2945, 2954);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.gielinor.rs2.plugin.Plugin#newInstance(java.lang.Object)
     */
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(3499, OBJECT_TYPE, this);
        addHandler(3485, OBJECT_TYPE, this);
        addHandler(3463, OBJECT_TYPE, this);
        addHandler(30728, OBJECT_TYPE, this);
        return this;
    }

    /**
     * (non-Javadoc)
     *
     * @see org.gielinor.game.interaction.UseWithHandler#handle(org.gielinor.game.interaction.NodeUsageEvent)
     */
    @Override
    public boolean handle(NodeUsageEvent event) {
        Player player = event.getPlayer();
        if (event.getUsedWith().getId() == 3499) {
            if (!event.getPlayer().getGameAttributes().getAttributes().containsKey("priest_in_peril:key") && event.getPlayer().getInventory().remove(new Item(2944))) {
                event.getPlayer().getInventory().add(new Item(2945));
                event.getPlayer().getActionSender().sendMessage("You swap the Golden key for the Iron key.");
                event.getPlayer().getGameAttributes().saveAttribute("priest_in_peril:key", true);
            } else {
                return true;
            }
        }
        if (event.getUsedWith().getId() == 3485) {
            if (event.getPlayer().getInventory().remove(new Item(1925))) {
                event.getPlayer().getInventory().add(new Item(2953));
                event.getPlayer().getActionSender().sendMessage("You fill the bucket from the well.");
            }
        }
        if (event.getUsedWith().getId() == 3463) {
            if (player.getInventory().remove(new Item(2945))) {
                player.getActionSender().sendMessage("You have unlocked the cell door.");
                NPC npc = NPC.create(1047, player.getLocation());
                npc.setName("Dezel");
                player.getDialogueInterpreter().sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Oh! Thank you! You have found the key!");
            }
        }
        if (event.getUsedWith().getId() == 30728) {
            if (player.getInventory().remove(new Item(2954))) {
                player.getInventory().add(new Item(1925));
                player.getActionSender().sendMessage("You pour the blessed water over the coffin...");
            }
        }
        return true;
    }

}
