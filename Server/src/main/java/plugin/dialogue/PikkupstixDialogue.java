package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Handles the PikkupstixDialogue dialogue.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class PikkupstixDialogue extends DialoguePlugin {

    /**
     * Represents the summoning mastering items.
     */
    private static final Item[] ITEMS = new Item[]{ new Item(12169), new Item(12170), new Item(12171) };

    /**
     * Represents the coins item.
     */
    private static final Item COINS = new Item(Item.COINS, 99000);

    /**
     * Represents the amount of bulk shards to be bought.
     */
    private int amount;

    /**
     * Represents the total price of the bulk shards.
     */
    private int price;

    /**
     * Constructs a new {@code PikkupstixDialogue} {@code Object}.
     */
    public PikkupstixDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code PikkupstixDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public PikkupstixDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new PikkupstixDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc("Hello! How can I help you?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                if (player.getSkills().getStaticLevel(Skills.SUMMONING) == 99) {
                    options("So, what's Summmoning all about, then?", "Can I buy some Summoning supplies?", "Can I buy a Summoning skillcape?", "I'd like to buy shards in bulk.");
                    stage = 600;
                } else {
                    options("So, what's Summmoning all about, then?", "Can I buy some Summoning supplies?", "Please tell me about skillcapes.", "I'd like to buy shards in bulk.");
                    stage = 1;
                }
                break;
            case 1:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        player("So, what's summoning all about, then?");
                        stage = 10;
                        break;
                    case FOUR_OPTION_TWO:
                        player("Can I buy some summoning supplies, please?");
                        stage = 34;
                        break;
                    case FOUR_OPTION_THREE:
                        player("Please tell me about skillcapes.");
                        stage = 400;
                        break;
                    case FOUR_OPTION_FOUR:
                        player("I'd like to buy some of your spirit shards in bulk.");
                        stage = 9000;
                        break;
                }
                break;
            case 10:
                npc("In general? Or did you have a specific topic in mind?");
                stage = 11;
                break;
            case 11:
                options("In general.", "Tell me about summoning familiars.", "Tell me about special moves.", "Tell me about pets.");
                stage = 12;
                break;
            case 12:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        npc("Well, you already know about Summoning in general;", "otherwise, we would not be having this conversation!");
                        stage = 100;
                        break;
                    case FOUR_OPTION_TWO:
                        npc("Summoned familiars are at the very core of Summoning.", "Each familiar is different, and the more powerful the", "summoner, the more powerful the familiar they can", "summon.");
                        stage = 20001;
                        break;
                    case FOUR_OPTION_THREE:
                        npc("Well, if a Summoning pouch is split apart at an obelisk,", "then the energy it contained will reconstitute itself -", "transform - into a scroll. This scroll can then be used to", "make your familiar perform its special move.");
                        stage = 2001;
                        break;
                    case FOUR_OPTION_FOUR:
                        npc("Well, these are not really an element of the skill, as such,", "but more like a side-effect of training.");
                        stage = 2001;
                        break;

                }
                break;
            case 100:
                npc("Effectively, the skill can be broken into two main parts:", "summoned familiars, and pets.");
                stage = 101;
                break;
            case 101:
                npc("Summoned familiars are spiritual animals that can be", "called to you from the spirit plane, to serve you for a", "period of time.");
                stage = 102;
                break;
            case 102:
                npc("These animals can also perform a special move, which is", "specific to the species. For example, a spirit wolf can", "perform the Howl special move if you are holding the", "correct Howl scroll.");
                stage = 103;
                break;
            case 103:
                npc("The last part of Summoning: the pets. The more", "you practice the skill, the more you will comprehend the", "natural world around you.");
                stage = 104;
                break;
            case 104:
                npc("This is reflected in your increased ability to raise animals", "as pets. It takes a skilled summoner to be able to raise", "some of Gielinor's more exotic animals, such as the lizards", "of Karamja, or even dragons!");
                stage = 105;
                break;
            case 105:
                npc("Now that I've given you this overview, do you want to", "know about anything specific?");
                stage = 106;
                break;
            case 106:
                options("Tell me about summoning familiars.", "Tell me about special moves.", "Tell me about pets.");
                stage = 107;
                break;
            case 107:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        npc("Summoned familiars are at the very core of Summoning.", "Each familiar is different, and the more powerful the", "summoner, the more powerful the familiar they can", "summon.");
                        stage = 20001;
                        break;
                    case THREE_OPTION_TWO:
                        npc("Well, if a Summoning pouch is split apart at an obelisk,", "then the energy it contained will reconstitute itself -", "transform - into a scroll. This scroll can then be used to", "make your familiar perform its special move.");
                        stage = 2001;
                        break;
                    case THREE_OPTION_THREE:
                        npc("Well, these are not really an element of the skill, as such,", "but more like a side-effect of training.");
                        stage = 2001;
                        break;
                }
                break;
            case 2001:
                end();
                break;
            case 2000:
                npc("Summoned familiars are at the very core of Summoning.", "Each familiar is different, and the more powerful the", "summoner, the more powerful the familiar they can", "summon.");
                stage = 20001;
                break;
            case 20001:
                end();
                break;
            case 34:
                npc("If you like! It's good to see you training.");
                stage = 35;
                break;
            case 35:
                end();
                Shops.SUMMONING_SHOP.open(player);
                break;
            case 400:
                npc("Of course. Skillcapes are a symbol of achievement. Only", "people who have mastered a skill and reached level 99 can", "get their hands on them and gain the benefits they carry.", "Is there something else I can help you with perhaps?");
                stage = 401;
                break;
            case 401:
                interpreter.sendOptions("Choose an option:", "So, what's Summmoning all about, then?", "Can I buy some Summoning supplies?", "Please tell me about skillcapes.", "I'd like to buy some shards in bulk.");
                stage = 1;
                break;
            case 600:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        player("So, what's summoning all about, then?");
                        stage = 10;
                        break;
                    case FOUR_OPTION_TWO:
                        player("Can I buy some summoning supplies, please?");
                        stage = 34;
                        break;
                    case FOUR_OPTION_THREE:
                        player("Can I buy a Skillcape of Summoning?");
                        stage = 599;
                        break;
                    case FOUR_OPTION_FOUR:
                        player("I'd like to buy some of your spirit shards in bulk.");
                        stage = 9000;
                        break;
                }
                break;
            case 599:
                npc("Why yes you can! I must warn you that they cost", "a total of 99000 coins. Do you wish to still", "buy a skillcape of Summoning?");
                stage = 601;
                break;
            case 601:
                options("Yes.", "No.");
                stage = 602;
                break;
            case 602:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Yes, please.");
                        stage = 603;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
            case 603:
                if (player.getInventory().freeSlots() < 2) {
                    player("Sorry, I don't seem to have enough inventory space.");
                    stage = 604;
                    return true;
                }
                if (!player.getInventory().containsItem(COINS)) {
                    end();
                    return true;
                }
                if (!player.getInventory().remove(COINS)) {
                    player("Sorry, I don't seem to have enough coins", "with me at this time.");
                    stage = 604;
                    return true;
                }
                if (player.getInventory().add(ITEMS[player.getSkills().getMasteredSkills() > 1 ? 1 : 0], ITEMS[2])) {
                    player("There you go, enjoy!");
                    stage = 604;
                }
                break;
            case 604:
                end();
                break;
            case 9000:
                npc("Ah, very well then, young Summoner. I'd be happy to", "sell you spirit shards in a bulk package.", "How many did you have in mind?");
                stage = 9001;
                break;
            case 9001:
                interpreter.sendOptions("How many shards?", "5,000", "10,000", "25,000");
                stage = 9002;
                break;
            case 9002:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        player("I'd like to buy 5,000 of your shards.");
                        amount = 5000;
                        stage = 9003;
                        break;
                    case THREE_OPTION_TWO:
                        player("I'd like to buy 10,000 of your shards.");
                        amount = 10000;
                        stage = 9003;
                        break;
                    case THREE_OPTION_THREE:
                        player("I'd like to buy 25,000 of your shards.");
                        amount = 25000;
                        stage = 9003;
                        break;
                }
                break;
            case 9003:
                price = amount * 30;
                npc("Very well, I suppose I could sell you " + amount + " shards.", "Each shard costs a solid thirty gold pieces each.", "Your total cost is going to be " + price + " gold pieces.");
                stage = 9004;
                break;
            case 9004:
                if (player.getInventory().freeSlots() < 1) {
                    player("Actually, I don't have enough spaces in my inventory.");
                    stage = 604;
                    return true;
                }
                if (player.getInventory().getCount(Item.COINS) < price) {
                    player("I don't have enough coins with me.", "I'll be back when I do.");
                    stage = 604;
                    return true;
                }
                if (player.getInventory().getCount(Item.COINS) >= price && player.getInventory().freeSlots() >= 1) {
                    player("Alright, here's " + price + " gold pieces.");
                    stage = 9005;
                }
                break;
            case 9005:
                if (player.getInventory().getCount(Item.COINS) >= price && player.getInventory().freeSlots() >= 1) {
                    interpreter.sendItemMessage(12183, "You purchase " + amount + " shards for " + price + " gold.");
                    if (player.getInventory().remove(new Item(Item.COINS, price))) {
                        player.getInventory().add(new Item(12183, amount));
                    }
                    stage = 604;
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6970 };
    }

}
