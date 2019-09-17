package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Handles the Bandit Camp Bartender dialogue
 *
 * @author Aero
 * @version 1.0
 */
public class BanditCampBartenderDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code BanditCampBartender} {@code Object}.
     */
    public BanditCampBartenderDialogue() {
        /**
         * Empty to stop instantiation.
         */
    }

    /**
     * Constructs a new {@code BanditCampBartender} {@code Object}.
     *
     * @param player The player to construct the class for.
     */
    public BanditCampBartenderDialogue(final Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BanditCampBartenderDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        if (player.getAttribute("dt-beer_bought", Boolean.FALSE)) {
            npc("You've had your drink, now get out of here.", "I have nothing to say to the");
            stage = 7;
        } else if (player.getAttribute("dt-diamonds_unlock", Boolean.FALSE)) {
            options("I heard about treasure...", "I heard about four diamonds...", "I heard about a fortress...", "I've heard of the Diamonds of Azzandra.");
            stage = 11;
        } else {
            npc("If you're not buying anything, I have nothing to say to", "you.");
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case -1:
                end();
                break;
            case 0:
                player("I would like to buy a drink.");
                stage = 4;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("As I understand it there's some kind of hidden treasure", "in these parts...");
                        stage = 2;
                        break;
                    case TWO_OPTION_TWO:
                        npc("What's that?", "You wanna buy a beer?", "It'll cost ya 650 coins.");
                        stage = 5;
                        break;
                }
                break;
            case 2:
                npc("Look around " + (player.getAppearance().isMale() ? "Sir." : "Lady."), "Does it look like there's any treasure near here?");
                stage = 3;
                break;
            case 3:
                npc("If I were you I'd get lost before someone takes a", "dislike to your face and removes it for you.");
                stage = -1;
                break;
            case 4:
                npc("What's that?", "You wanna buy a beer?", "It'll cost ya 650 coins.");
                stage = 5;
                break;
            case 5:
                options("Buy a beer", "Don't buy anything");
                stage = 6;
                break;
            case 6:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if (player.getInventory().contains(Item.COINS, 650)) {
                            player.getInventory().remove(new Item(Item.COINS, 650));
                            player.getInventory().add(new Item(3803, 1));
                            npc("There you go.", "Now get out, we don't like your kind around here.");
                            stage = -1;
                        } else {
                            npc("You don't have enough money.", "Get out of here! Do you think this is a charity?");
                            stage = -1;
                        }
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
            case 7:
                player("No, Wait!", "Look, I'm here on an archaeological expedition for the", "Museum of Varrock.");
                stage = 8;
                break;
            case 8:
                player("I'm only here looking for artefacts...");
                stage = 9;
                break;
            case 9:
                npc("Oh really?", "Our inheritance is only sand and death.", "What do you expect to find out here in this desert", "forsaken by the gods?");
                stage = 10;
                break;
            case 10:
                if (player.getAttribute("dt-diamonds_unlock", Boolean.FALSE)) {
                    options("I heard about treasure...", "I heard about four diamonds...", "I heard about a fortress...", "I've heard of the Diamonds of Azzandra.");
                } else {
                    options("I heard about treasure...", "I heard about four diamonds...", "I heard about a fortress...");
                }
                stage = 11;
                break;
            case 11:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        //TODO: From RS.
                        break;
                    case FOUR_OPTION_TWO:
                        player("I heard a rumour about four diamonds or crystals...");
                        stage = 12;
                        break;
                    case FOUR_OPTION_THREE:
                        player("Certain things have led me to believe there may be", "some kind of ruined fortress around here...");
                        stage = 19;
                        break;
                    case FOUR_OPTION_FOUR:
                        npc("The four diamonds of Azzanadra???", "How came you to know of this?");
                        stage = 13;
                        break;
                }
                break;
            case 12:
                npc("The four diamonds of Azzanadra???", "How came you to know of this?");
                stage = 13;
                break;
            case 13:
                player("You've heard of them then?");
                stage = 14;
                break;
            case 14:
                npc("It's just a fairy tale for children.", "Maybe one of the village elders might know more, but", "it's not really something I care about.");
                stage = 15;
                break;
            case 15:
                npc("Now get out of here, your sort isn't welcome in my", "bat.");
                player.setAttribute("dt-diamonds_unlock", Boolean.TRUE);
                stage = -1;
                break;
            case 16:
                player("Tell me all you know about the Diamonds of", "Azzandra.");
                stage = 17;
                break;
            case 17:
                npc("Not that I think it's any of your business, but when I", "was a child I remember hearing the legend.");
                stage = 18;
                break;
            case 18:
                npc("If you really want to hear more about it you'd be best", "off finding someone who cares about the past, and the", "history of this area, and stop bothering me.");
                stage = -1;
                break;
            case 19:
                npc("Doubt it", "What in the world would someone need to guard", "against in the middle of this dessert?");
                stage = 20;
                break;
            case 20:
                npc("A bad track of sand?", "I think you're on the wrong track, " + (player.getAppearance().isMale() ? "mr" : "miss") + " so called", "archaeologist.");
                stage = -1;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 1921 };
    }

}
