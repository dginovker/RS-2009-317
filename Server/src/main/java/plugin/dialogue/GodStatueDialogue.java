package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles the dialogues for praying at God statues.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public class GodStatueDialogue extends DialoguePlugin {

    /**
     * The game object.
     */
    private GameObject gameObject;

    public GodStatueDialogue() {

    }

    public GodStatueDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GodStatueDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        this.gameObject = (GameObject) args[0];
        String godName = "Saradomin";
        switch (gameObject.getId()) {
            /**
             * Saradomin.
             */
            case 2873:
                godName = "Saradomin";
                break;
            /**
             * Guthix.
             */
            case 2875:
                godName = "Guthix";
                break;
            /**
             * Zamorak.
             */
            case 2874:
                godName = "Zamorak";
                break;

        }

        interpreter.sendPlaneMessage("You kneel and begin to chant to " + godName + "...");
        player.getWalkingQueue().reset();
        player.getWalkingQueue().addPath(player.getLocation().getX(), player.getLocation().getY() - 1);
        World.submit(new Pulse(1, player) {

            @Override
            public boolean pulse() {
                player.faceLocation(player.getLocation().transform(0, 2, 0));
                return true;
            }
        });
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                player.animate(new Animation(645));
                player.lock(3);
                World.submit(new Pulse(1) {

                    int count = 0;

                    @Override
                    public boolean pulse() {
                        switch (count++) {
                            case 0:
                                // TODO GFX
                                break;
                            case 2:
                                int itemId = 2412;
                                switch (gameObject.getId()) {
                                    /**
                                     * Saradomin.
                                     */
                                    case 2873:
                                        itemId = 2412;
                                        break;
                                    /**
                                     * Guthix.
                                     */
                                    case 2875:
                                        itemId = 2413;
                                        break;
                                    /**
                                     * Zamorak.
                                     */
                                    case 2874:
                                        itemId = 2414;
                                        break;

                                }
                                GroundItemManager.create(new Item(itemId, 1), player.getLocation().transform(0, 1, 0), player);
                                player.setAttribute("RECENT_GOD_CAPE", 1);
                                World.submit(new Pulse(100, player) {

                                    @Override
                                    public boolean pulse() {
                                        player.removeAttribute("RECENT_GOD_CAPE");
                                        return true;
                                    }
                                });
                                interpreter.sendPlaneMessage("You feel a rush of energy charge through your veins. Suddenly a", "cape appears before you.");
                                return true;
                        }
                        return false;
                    }
                });
                stage = 20;
                break;
            case 20:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("GodStatue") };
    }
}
