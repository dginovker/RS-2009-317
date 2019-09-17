package plugin.dialogue;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the dying knight dialogue.
 *
 * @author Emperor
 * @version 1.0
 */
public final class GWDKnightDialogue extends DialoguePlugin {

    /**
     * Represents the scroll item.
     */
    private static final Item SCROLL = new Item(11734);

    /**
     * Constructs a new {@code GWDKnightDialogue} {@code Object}.
     */
    public GWDKnightDialogue() {
        NPCDefinition.forId(6201).getConfigurations().put("option:talk-to", new OptionHandler() {

            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                return this;
            }

            @Override
            public boolean handle(Player player, Node node, String option) {
                NPC npc = (NPC) node;
                return player.getDialogueInterpreter().open(6201, npc);
            }

            @Override
            public Location getDestination(Node n, Node node) {
                return node.getLocation().transform(0, -1, 0);
            }
        });
    }

    /**
     * Constructs a new {@code GWDKnightDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public GWDKnightDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GWDKnightDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if ((player.getConfigManager().get(1048) & 16) != 0) {
            player.getActionSender().sendMessage("The knight has already died.");
            return false;
        }
        player("Who are you? What are you doing here in the snow?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                npc("My name is...Sir Gerry. I am...a member of a", "secret...society of knights. My time is short and I", "need...your help.");
                stage = 1;
                break;
            case 1:
                player("A secret society of knights? What a surprise! Is there", "an old charter or decree that says if you're a knight", "you have to belong to a secret order?");
                stage = 2;
                break;
            case 2:
                npc("I'm sorry, my friend... I do not understand your", "meaning. Please, time is short... Take this scroll to Sir", "Tiffy. You will find him in Falador park... You should", "not...read it... It contains information for his eyes only.");
                stage = 3;
                break;
            case 3:
                if (player.getInventory().add(SCROLL)) {
                    interpreter.sendItemMessage(11734, "The knight hands you a scroll.");
                    int value = player.getConfigManager().get(1048) | (1 << 4);
                    player.getConfigManager().set(1048, value, true);
                    stage = 5;
                } else {
                    stage = 4;
                }
                break;
            case 4:
                interpreter.sendPlaneMessage("The knight tries to give you something, but your inventory is full.");
                stage = 5;
                break;
            case 5:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6201 };
    }
}
