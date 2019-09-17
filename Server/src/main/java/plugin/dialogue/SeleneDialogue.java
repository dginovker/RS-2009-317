package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles the dialogues for Selene on Lunar Isle.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public class SeleneDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@link SeleneDialogue} {@link DialoguePlugin}.
     */
    public SeleneDialogue() {
    }

    /**
     * Constructs a new {@link SeleneDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public SeleneDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new SeleneDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (!(player.getInventory().contains(9083) || player.getEquipment().contains(9083))) {
            player.getInterfaceState().close();
            player.lock(7);
            World.submit(new Pulse(2) {

                int count = 0;

                @Override
                public boolean pulse() {
                    this.setDelay(1);
                    switch (count++) {
                        case 0:
                            player.getInterfaceState().openComponent(8677);
                            break;
                        case 3:
                            player.getProperties().setTeleportLocation(Location.create(2621, 3688, 0));
                            break;
                        case 5:
                            player.unlock();
                            player.getInterfaceState().close();
                            player.getInterfaceState().openDefaultTabs();
                            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                            player.getDialogueInterpreter().sendPlaneMessage("Selene realizes you are not of the Moonclan, and sends", "you back to Rellekka!");
                            stage = END;
                            return true;
                    }
                    return false;
                }
            });
            return true;
        }
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello there.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                npc("Greetings sweetie. How can I help?");
                stage = END;
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 4517 };
    }
}
