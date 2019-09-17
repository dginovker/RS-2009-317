package plugin.zone.rellekka;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.task.impl.LocationLogoutTask;
import org.gielinor.rs2.task.impl.LogoutTask;

/**
 * Handles the dialogue for Jarvald.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class JarvaldDialogue extends DialoguePlugin {


    /**
     * Constructs a new {@code JarvaldDialogue} {@link DialoguePlugin}.
     */
    public JarvaldDialogue() {

    }

    /**
     * Constructs a new {@code JarvaldDialogue} {@link DialoguePlugin}.
     *
     * @param player the player.
     */
    public JarvaldDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new JarvaldDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (args.length > 1) {
            interpreter.sendOptions("Travel back to Rellekka?", "Yes", "No");
            stage = 10;
            return true;
        }
        npc("Outerlander! What do you want?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                options("Travel to Waterbirth island.", "Nothing.");
                stage++;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("I would like to travel to Waterbirth island.");
                        stage = 2;
                        break;
                    case TWO_OPTION_TWO:
                        player("Nothing.");
                        stage = END;
                        break;
                }
                break;
            case 2:
                npc("Hm... I guess I could. For a fee!", "I'll sail you to Waterbirth island for 1000 coins.");
                stage++;
                break;
            case 3:
                options("Pay 1000 coins", "No thank you.");
                stage++;
                break;
            case 4:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if (!player.getInventory().contains(Item.COINS, 1000)) {
                            npc("Typical outerlander! I said 1000 coins, you", "have no such thing!");
                            stage = END;
                            break;
                        }
                        if (player.getInventory().remove(new Item(Item.COINS, 1000))) {
                            sail(player, true);
                        }
                        end();
                        break;
                    case TWO_OPTION_TWO:
                        player("No thank you.");
                        stage = 5;
                        break;
                }
                break;
            case 5:
                npc("Fine, outerlander!");
                stage = END;
                break;
            case 10:
                end();
                sail(player, false);
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    /**
     * Sails the player to and from waterbirth.
     *
     * @param player the player.
     */
    public void sail(final Player player, final boolean to) {
        player.lock();
        player.getInterfaceState().open(new Component(224));
        player.addExtension(LogoutTask.class, new LocationLogoutTask(5, to ? Location.create(2544, 3759, 0) : Location.create(2620, 3685, 0)));
        World.submit(new Pulse(1, player) {

            int count;

            @Override
            public boolean pulse() {
                switch (++count) {
                    case 5:
                        player.unlock();
                        player.getInterfaceState().close();
                        player.getProperties().setTeleportLocation(to ? Location.create(2544, 3759, 0) : Location.create(2620, 3685, 0));
                        player.getDialogueInterpreter().close();
                        player.getDialogueInterpreter().sendDialogue("The ship arrives at " + (to ? "Waterbirth Island" : "Rellekka") + ".");
                        return true;
                }
                return false;
            }

        });
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2436, 2437, 2438 };
    }
}
