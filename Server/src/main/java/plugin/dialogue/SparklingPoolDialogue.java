package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the dialogue for the Sparkling pool.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public class SparklingPoolDialogue extends DialoguePlugin {

    /**
     * The game object.
     */
    private GameObject gameObject;

    public SparklingPoolDialogue() {

    }

    public SparklingPoolDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new SparklingPoolDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        this.gameObject = (GameObject) args[0];
        interpreter.sendPlaneMessage("You step into the pool of sparkling water. You feel energy rush", "through your veins.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                end();
                player.animate(Animation.create(804));
                player.lock(5);
                World.submit(new Pulse(1) {

                    int count = 0;

                    @Override
                    public boolean pulse() {
                        switch (count++) {
                            case 0:
                                player.getInterfaceState().openComponent(8677);
                                break;
                            case 2:
                                PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
                                break;
                            case 3:
                                player.setTeleportTarget(gameObject.getId() == 2878 ? Location.create(2509, 4689, 0) : Location.create(2542, 4718, 0));
                                break;
                            case 4:
                                player.getInterfaceState().close();
                                player.getInterfaceState().openDefaultTabs();
                                PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                                player.playAnimation(Animation.create(803));
                                return true;
                        }
                        return false;
                    }
                });
                break;
            case 20:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("SparklingPool") };
    }
}
