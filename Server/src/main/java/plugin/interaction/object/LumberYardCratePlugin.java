package plugin.interaction.object;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.quest.Quest;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.impl.ForceMovement;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the plugin used for handling a lumber yard crate.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class LumberYardCratePlugin extends OptionHandler {

    /**
     * Represents the kittem item.
     */
    private static final Item KITTEN = new Item(1555);

    @Override
    public boolean handle(Player player, Node node, String option) {
        final Quest quest = player.getQuestRepository().getQuest("Gertrude's Cat");
        switch (option) {
            case "squeeze-under":
                Location dest = null;
                Location start = node.getLocation();
                if (player.getLocation().getX() > node.getLocation().getX()) {
                    start = Location.create(3296, 3498, 0);
                    dest = Location.create(3295, 3498, 0);
                } else {
                    dest = Location.create(3296, 3498, 0);
                }
                ForceMovement.run(player, start, dest, Animation.create(9221));
                break;
            case "search":
                System.out.println(quest.getStage());
                if (quest.getStage() == 50 && !player.getInventory().containsItem(KITTEN) && !player.getBank().containsItem(KITTEN)) {
                    quest.setStage(40);
                }
                if (node instanceof NPC) {
                    player.getActionSender().sendMessage("You search the crate.");
                    player.getActionSender().sendMessage("You find nothing.");
                }
                if (quest.getStage() == 40) {
                    if (player.getAttribute("findkitten", false) && player.getInventory().freeSlots() > 0) {
                        quest.setStage(50);
                        player.getDialogueInterpreter().sendPlaneMessage("You find a kitten! You carefully place it in your backpack.");
                        player.getInventory().add(KITTEN);
                        return true;
                    }
                    player.getActionSender().sendMessage("You search the crate.");
                    player.getActionSender().sendMessage("You find nothing.");
                    if (RandomUtil.random(0, 3) == 1) {
                        player.getActionSender().sendMessage("You can hear kittens mewing close by...");
                        player.setAttribute("findkitten", true);
                    }
                } else {
                    player.getActionSender().sendMessage("You search the crate.");
                    player.getActionSender().sendMessage("You find nothing.");
                }
                break;
        }
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(767).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(2620).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(31149).getConfigurations().put("option:squeeze-under", this);
        return null;
    }

}
