package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.travel.carpet.MagicCarpet;
import org.gielinor.game.content.global.travel.carpet.MagicCarpet.Destination;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.model.container.impl.Equipment;

/**
 * The dialogue plugin used for the rug merchant.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class RugMerchantDialogue extends DialoguePlugin {

    /**
     * The ids of the rug merchants.
     */
    public static final int[] IDS = new int[]{ 2291, 2292, 2293, 2294, 2296, 2298, 3020 };
    /**
     * The destination to travel to.
     */
    private Destination destination;

    /**
     * Constructs a new {@code RugMerchantDialogue} {@code Object}.
     */
    public RugMerchantDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code RugMerchantDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public RugMerchantDialogue(final Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new RugMerchantDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch (npc.getId()) {
            case 2291://Shantay pass. (hub)
            case 3020://Pollnivneach. (hub)
                player("Hello.");
                break;
            default://regulars.
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        int buttonId = optionSelect == null ? -1 : optionSelect.getButtonId();
        switch (npc.getId()) {
            case 2291://Shantay pass.
                switch (stage) {
                    default:
                        handleDefault(buttonId);
                        break;
                }
                break;
            case 3020://Pollnivneach.
                switch (stage) {
                    default:
                        handleDefault(buttonId);
                        break;
                }
                break;
            default://regulars.
                switch (stage) {
                    default:
                        handleDefault(buttonId);
                        break;
                }
                break;
        }
        return true;
    }

    /**
     * Handles the defeault dialogue.
     *
     * @param buttonId the buttonId.
     */
    private void handleDefault(final int buttonId) {
        switch (stage) {
            case 0:
                npc("Greetings, desert traveller. Do you require the services", "of Ali Morrisane's flying carpet fleet?");
                stage++;
                break;
            case 1:
                options("Tell me about Ali Morrisane.", "Tell me about this magic carpet fleet.", "Yes.", "No, but I had some questions.");
                stage++;
                break;
            case 2:
                switch (buttonId) {
                    case 1:
                        player("Tell me about Ali Morrisane.");
                        stage = 10;
                        break;
                    case 2:
                        player("Tell me about this magic carpet fleet.");
                        stage = 20;
                        break;
                    case 3:
                        switch (npc.getId()) {
                            case 2291://Shantay
                                npc("From here you can travel to Uzer, te Bedabin Camp or to", "north Pollnivneach.");
                                stage = 50;
                                break;
                            case 3020://Pollnivneach.
                                break;
                            default://Regular
                                npc("From here you can travel to Shantay Pass.");
                                break;
                        }
                        break;
                    case 4:
                        player("I have some other questions.");
                        stage = 40;
                        break;
                }
                break;
            case 10:
                npc("What? You haven't heard of Ali Morrisane? Possibly the", "greatest salesman in all the Kharidian Desert, if not all", "RuneScape?");
                stage++;
                break;
            case 11:
                player("Ah, yes, I remember him now. I went on a wild goose", "chse looking for his nephew.");
                stage++;
                break;
            case 12:
                end();
                break;
            case 20:
                npc("The latest idea from the great Ali Morrisane. Desert", "travel will never be the same again.");
                stage++;
                break;
            case 21:
                player("So how does it work?");
                stage++;
                break;
            case 22:
                npc("I'm not too sure; it's just an enchanted rug really,", "made out of special Ughtanki hair. It flies to whatever", "destination its owner commands.");
                stage++;
                break;
            case 23:
                end();
                break;
            case 40:
                npc("I'll help you as much as I can.");
                stage++;
                break;
            case 41:
                player("Where did you get that hat?");
                stage++;
                break;
            case 42:
                npc("My fez? I got it form Ali Morrisane; it's a uniform of", "sorts. Apparently it makes us more visible, but i'm not", "too sure about it.");
                stage++;
                break;
            case 43:
                player("Well, it is quite distinctive.");
                stage++;
                break;
            case 44:
                end();
                break;
            case 50:
                npc("The second major carpet hub station is in south Pollnivneach", "is in easy walking distance from there.");
                stage++;
                break;
            case 51:
                switch (npc.getId()) {
                    case 2291://Shantay
                        options("I want to travel to Uzer.", "I want to travel to Bedabin Camp.", "I want to travel to Pollnivneach.");
                        stage = 52;
                        break;
                    case 3020://Pollnivneach.
                        break;
                    default://Regular
                        options("Take me to Shantay Pass.", "Actually, I've changed my mind.");
                        stage = 60;
                        break;
                }
                break;
            case 52:
                switch (buttonId) {
                    case 1:
                        destination = Destination.UZER;
                        player("I would like to travel to Uzer.");
                        stage = 53;
                        break;
                    case 2:
                        destination = Destination.BEDABIN_CAMP;
                        player("I would like to travel to the Bedabin camp.");
                        stage = 53;
                        break;
                    case 3:
                        destination = Destination.NORTH_OF_POLLNIVNEACH;
                        player("I would like to travel to north of Pollnivneach.");
                        stage = 53;
                        break;
                }
                break;
            case 53:
                if (destination == null) {
                    end();
                    break;
                }
                npc("Okay! The cost will be " + destination.getCost() + " gold coins.");
                stage = 54;
                break;
            case 54:
                if (destination == null) {
                    end();
                    break;
                }
                if (player.getInventory().contains(new Item(Item.COINS, destination.getCost()))) {
                    options("Okay, here you go.", "No thank you.");
                    stage = 55;
                    break;
                }
                player("I'm afraid I don't have that on me.");
                stage = 1000;
                break;
            case 55:
                if (destination == null) {
                    end();
                    break;
                }
                if (buttonId == 1) {
                    if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
                        npc("I'm sorry, you need to have your right hand free.", "You could hit a nasty flock of birds.");
                        stage = 1000;
                        break;
                    }
                    if (player.getFamiliarManager().hasFamiliar() || player.getFamiliarManager().hasPet()) {
                        npc("I'm sorry, familiars can't fly with you.");
                        stage = 1000;
                        break;
                    }
                    player.getInventory().remove(new Item(Item.COINS, destination.getCost()));
                    npc("Thank you, please keep in mind that you may experience some", "turbulence on this ride. You may also hit a nasty", "flock of birds.");
                    stage = 56;
                    break;
                }
                end();
                break;
            case 56:
                end();
                if (destination == null) {
                    break;
                }
                MagicCarpet.prepare(player, destination);
                break;
            case 60:
                break;
            case 1000:
                end();
                break;
        }
    }

    @Override
    public int[] getIds() {
        return IDS;
    }

    /**
     * A destination for a rug.
     *
     * @author 'Vexia
     */
    public enum RugDestination {

        SHANTAY_PASS(2291, null),
        BEDABIN_CAMP(2292, null);

        /**
         * The npc id.
         */
        private final int npc;

        /**
         * The destination to go to.
         */
        private final Location destination;

        /**
         * Constructs a new {@code RugDestination} {@code Object}.
         *
         * @param npc         the npc.
         * @param destination the destination.
         */
        private RugDestination(int npc, Location destination) {
            this.npc = npc;
            this.destination = destination;
        }

        /**
         * Checks if the player has the requirements.
         *
         * @param player the player.
         * @return {@code True} if so.
         */
        public boolean hasRequirements(final Player player) {
            return true;
        }

        /**
         * Gets the npc.
         *
         * @return The npc.
         */
        public int getNpc() {
            return npc;
        }

        /**
         * Gets the destination.
         *
         * @return The destination.
         */
        public Location getDestination() {
            return destination;
        }
    }

}
