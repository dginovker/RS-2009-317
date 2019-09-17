package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for Kazgar.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class KazgarDialogue extends DialoguePlugin {

    /**
     * Constructs a new <code>KazgarDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     */
    public KazgarDialogue() {
    }

    /**
     * Constructs a new <code>KazgarDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     *
     * @param player The player.
     */
    public KazgarDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new KazgarDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (npc.getLocation().getX() >= 3305) {
            if (!player.getSavedData().getGlobalData().hasWalkedToDorgeshKaan()) { // First time
                player.unlock();
                player.getSavedData().getGlobalData().setWalkedToDorgeshKaan(true);
                npc("Welcome to the Dorgeshuun Mines, " + TextUtils.formatDisplayName(player.getName()) + "!");
                stage = 0;
                return true;
            }
            npc("Hello again, " + TextUtils.formatDisplayName(player.getName()) + ". Would you like to go back", "to Lumbridge?");
            stage = 1;
            return true;
        }
        npc("Hello, " + TextUtils.formatDisplayName(player.getName()) + ". Would you like to go to the mines?");
        stage = 4;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                npc("I'll wait in Lumbridge for you to bring you here,", "so you don't have to walk through the", "caves again!");
                stage = END;
                break;
            case 1:
                options("Yes, please.", "No thank you.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Yes, please.");
                        stage = 3;
                        break;
                    case TWO_OPTION_TWO:
                        player("No thank you.");
                        stage = END;
                        break;
                }
                break;
            case 3:
                end();
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
                                player.setTeleportTarget(new Location(3230, 9610, 0));
                                break;
                            case 4:
                                player.getInterfaceState().close();
                                player.getInterfaceState().openDefaultTabs();
                                PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                                return true;
                        }
                        return false;
                    }
                });
                break;
            case 4:
                options("Yes, please.", "No thank you.");
                stage = 5;
                break;
            case 5:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Yes, please.");
                        stage = 6;
                        break;
                    case TWO_OPTION_TWO:
                        player("No thank you.");
                        stage = END;
                        break;
                }
                break;
            case 6:
                end();
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
                                player.setTeleportTarget(new Location(3308, 9612, 0));
                                break;
                            case 4:
                                player.getInterfaceState().close();
                                player.getInterfaceState().openDefaultTabs();
                                PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                                return true;
                        }
                        return false;
                    }
                });
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2086 };
    }
}
