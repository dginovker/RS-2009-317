package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.travel.Teleport;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;

/**
 * The dialogue plugin for the Donor orb.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public final class DonorOrbTeleportDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@link plugin.dialogue.DonorOrbTeleportDialogue} {@link DialoguePlugin}.
     */
    public DonorOrbTeleportDialogue() {
    }

    /**
     * Constructs a new {@link plugin.dialogue.DonorOrbTeleportDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public DonorOrbTeleportDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new DonorOrbTeleportDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        interpreter.sendOptions("Select a location", "Duradel", "Abyssal Demons", "Barrows", "Pest Control", "More...");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                switch (optionSelect) {
                    case FIVE_OPTION_ONE:
                        if (player.getTeleporter().send(Location.create(2869, 2981, 1), Teleport.TeleportType.NORMAL)) {
                            end();
                            return true;
                        }
                        break;
                    case FIVE_OPTION_TWO:
                        if (player.getTeleporter().send(Location.create(3421, 3567, 2), Teleport.TeleportType.NORMAL)) {
                            end();
                            return true;
                        }
                        break;
                    case FIVE_OPTION_THREE:
                        if (player.getTeleporter().send(Location.create(3564, 3288, 0), Teleport.TeleportType.NORMAL)) {
                            if (!player.getInventory().contains(952)) {
                                player.getInventory().add(new Item(952), true);
                            }
                            end();
                            return true;
                        }
                        break;
                    case FIVE_OPTION_FOUR:
                        if (player.getTeleporter().send(Location.create(2662, 2652, 0), Teleport.TeleportType.NORMAL)) {
                            end();
                            return true;
                        }
                        break;
                    case FIVE_OPTION_FIVE:
                        interpreter.sendOptions("Select a location", "TzHaar", "Piscatoris", "Agility Pyramid", "Dagannoth Kings", "More...");
                        stage = 2;
                        break;
                }
                break;
            case 2:
                switch (optionSelect) {
                    case FIVE_OPTION_ONE:
                        if (player.getTeleporter().send(Location.create(2444, 5170, 0), Teleport.TeleportType.NORMAL)) {
                            end();
                            return true;
                        }
                        break;
                    case FIVE_OPTION_TWO:
                        if (player.getTeleporter().send(Location.create(2337, 3694, 0), Teleport.TeleportType.NORMAL)) {
                            end();
                            return true;
                        }
                        break;
                    case FIVE_OPTION_THREE:
                        if (player.getTeleporter().send(Location.create(3354, 2828, 0), Teleport.TeleportType.NORMAL)) {
                            end();
                            return true;
                        }
                        break;
                    case FIVE_OPTION_FOUR:
                        if (player.getTeleporter().send(Location.create(1912, 4367, 0), Teleport.TeleportType.NORMAL)) {
                            end();
                            return true;
                        }
                        break;
                    case FIVE_OPTION_FIVE:
                        interpreter.sendOptions("Select a location", "Duradel", "Abyssal Demons", "Barrows", "Pest Control", "More...");
                        stage = 0;
                        break;
                }
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("donor-orb-teleports") };
    }
}
