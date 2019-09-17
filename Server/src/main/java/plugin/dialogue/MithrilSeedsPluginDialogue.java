package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.player.FaceLocationFlag;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the dialogue plugin used for mithril seeds.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class MithrilSeedsPluginDialogue extends DialoguePlugin {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(827);

    /**
     * Represents the flower object.
     */
    private GameObject flower;

    /**
     * Constructs a new {@code MithrilSeedPluginDialogue}.
     */
    public MithrilSeedsPluginDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code MithrilSeedPluginDialogue}.
     */
    public MithrilSeedsPluginDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new MithrilSeedsPluginDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        flower = (GameObject) args[0];
        player.getDialogueInterpreter().sendOptions("Select an Option", "Pick the flowers.", "Leave the flowers.");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (interfaceId) {
            case 228: // Options chatbox interface id.
                switch (optionSelect.getButtonId()) {
                    case 1: // First option
                        player.lock(2);
                        player.faceLocation(FaceLocationFlag.getFaceLocation(player, flower));
                        player.animate(ANIMATION);
                        World.submit(new Pulse(2, player, flower) {

                            @Override
                            public boolean pulse() {
                                Item reward = new Item(2460 + ((flower.getId() - 2980) << 1));
                                if (reward == null || !player.getInventory().hasRoomFor(reward)) {
                                    player.getActionSender().sendMessage("Not enough space in your inventory!");
                                    return true;
                                }
                                if (ObjectBuilder.remove(flower)) {
                                    player.getInventory().add(reward);
                                    player.getActionSender().sendMessage("You pick the flowers.");
                                }
                                return true;
                            }
                        });
                        break;
                }
                break;
        }
        end();
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 1 << 16 | 1 };
    }

}
