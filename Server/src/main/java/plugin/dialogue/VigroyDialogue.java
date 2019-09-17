package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles the dialogue plugin for Vigroy.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public final class VigroyDialogue extends DialoguePlugin {

    private final Item COINS = new Item(Item.COINS, 200);

    public VigroyDialogue() {
    }

    public VigroyDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new VigroyDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (args.length > 1) { // "Pay-fare" on Vigroy
            stage = 4;
            handle(0, null);
            return true;
        }
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello! Would you like to take a trip back to Brimhaven?", "It will cost you 200 coins.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes please.", "200 coins? No way!");
                stage = 2;
                break;
            case 2:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        player("Yes please.");
                        stage = 3;
                        break;
                    case 2:
                        player("200 coins? No way!");
                        stage = END;
                        break;
                }
                break;
            case 3:
                if (!player.getInventory().containsItem(COINS)) {
                    interpreter.sendDialogues(player, null, "Sorry, I don't seem to have enough coins.");
                    stage = END;
                    return true;
                }
                player.getInventory().remove(COINS);
                end();
                player.getActionSender().sendMessage("You pay Vigroy 200 coins to travel back to Brimhaven.");
                travel();
                break;
            case 4:
                if (!player.getInventory().containsItem(COINS)) {
                    interpreter.sendDialogues(player, null, "Sorry, I don't seem to have enough coins.");
                    stage = END;
                    return true;
                }
                player.getInventory().remove(COINS);
                end();
                player.getActionSender().sendMessage("You quickly pay Vigroy 200 coins to travel back to Brimhaven.");
                travel();
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    public void travel() {
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
                        player.getActionSender().sendMinimapState(2);
                        break;
                    case 3:
                        player.setTeleportTarget(Location.create(2779, 3212, 0));
                        break;
                    case 4:
                        player.getInterfaceState().close();
                        player.getInterfaceState().openDefaultTabs();
                        player.getActionSender().sendMinimapState(0);
                        return true;
                }
                return false;
            }

        });
    }

    @Override
    public int[] getIds() {
        return new int[]{ 511 };
    }
}
