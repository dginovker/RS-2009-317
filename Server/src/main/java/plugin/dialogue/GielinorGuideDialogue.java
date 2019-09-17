package plugin.dialogue;

import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.config.Constants;

/**
 * The dialogue plugin for the Gielinor guide.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class GielinorGuideDialogue extends DialoguePlugin {

    /**
     * The referral ticket {@link org.gielinor.game.node.item.Item}.
     */
    private static final Item REFERRAL_TICKET = new Item(14752);

    /**
     * Constructs a new {@link GielinorGuideDialogue} {@link DialoguePlugin}.
     */
    public GielinorGuideDialogue() {
    }

    /**
     * Constructs a new {@link GielinorGuideDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public GielinorGuideDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GielinorGuideDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello, " + player.getUsername() + ", I'm the " + Constants.SERVER_NAME + " Guide.", "I can help you with most things for getting", "started.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                options("Can I see the starter cutscene?", "How do I begin making money?", "Where do I begin training?", "What is there to do?", "Can I have a referral ticket?");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case FIVE_OPTION_ONE:
                        player("Can I see the starter cutscene?");
                        stage = 19;
                        break;
                    case FIVE_OPTION_TWO:
                        player("How do I begin make money?");
                        stage = 2;
                        break;
                    case FIVE_OPTION_THREE:
                        player("Where do I begin training?");
                        stage = 12;
                        break;
                    case FIVE_OPTION_FOUR:
                        player("What is there to do?");
                        stage = 14;
                        break;
                    case FIVE_OPTION_FIVE:
                        player("Can I have a referral ticket?");
                        stage = 21;
                        break;
                }
                break;
            case 2:
                npc("You can begin making money by simply taking part", "in one of the many features " + Constants.SERVER_NAME + " has to offer", "you can kill monsters which will drop coins and", "sometimes expensive items.");
                stage = 3;
                break;
            case 3:
                npc("You can find easier NPCs at training teleports, which", "can be accessed by clicking the 'Training teleport' spell", "in your magic book.");
                stage = 4;
                break;
            case 4:
                npc("At higher levels, you can try your luck bossing,", "the spell 'Boss teleport' will give you access", "to many bosses to slay for items.");
                stage = 5;
                break;
            case 5:
                npc("If you wish to go with a less violent method,",
                    "you could always train your thieving",
                    "on the men around here, just don't tell them",
                    "I told you this, simply click 'Pickpocket' and");
                stage = 6;
                break;
            case 6:
                npc("hope for the best in receiving some coins!", "Once you're a higher level in thieving,", "you can teleport around cities by the", "'Cities teleport' in your magic book, and");
                stage = 8;
                break;
            case 8:
                npc("thieve from stalls for items and coins, which", "can be sold to shops or players for money.");
                stage = 9;
                break;
            case 9:
                npc("Of course, you can also eventually use your skills", "to make supplies, such as bows, weapons, potions", "and sell these to players.");
                stage = 10;
                break;
            case 10:
                npc("Other players may also post guides on slaying bosses,", "skilling to make money, and many other helpful tips", "on the forums.");
                stage = 11;
                break;
            case 11:
                npc("I'm also sure if you ask around, others will be happy", "to help in most cases.");
                stage = 0;
                break;
            case 12:
                npc("You can begin training by going to your magic spellbook", "and clicking 'Training teleport'. From there,", "there will be options on where to train.", "For beginners, I would highly recommend");
                stage = 13;
                break;
            case 13:
                npc("The low level training before you consider", "higher levels.");
                stage = 0;
                break;
            case 14:
                npc("There's plenty to do on " + Constants.SERVER_NAME + "!", "From training your combat to become powerful", "to training your skills to become resourceful.");
                stage = 15;
                break;
            case 15:
                npc("Every skill on " + Constants.SERVER_NAME + " has a use, and can be", "very helpful in your journey.", "You may also find fun things to do", "like minigames such as Duel Arena, Pest Control");
                stage = 16;
                break;
            case 16:
                npc("and many more.", "Or try your luck with friends and slay mighty", "bosses for great drops.");
                stage = 18;
                break;
            case 18:
                npc("You can find minigame teleports by clicking", "the world icon on your minimap.", "Your spellbook will also have teleports", "to various cities to explore.");
                stage = 0;
                break;
            case 19:
                npc("Of course!");
                stage = 20;
                break;
            case 20:
                end();
                player.lock();
                ActivityManager.start(player, "starter cutscene", false);
                break;
            case 21:
                if (player.getInventory().contains(REFERRAL_TICKET) || player.getBank().contains(REFERRAL_TICKET)) {
                    stage = END;
                    npc("You already have a referral ticket in your " + (player.getBank().contains(REFERRAL_TICKET) ? "bank" : "inventory") + ".");
                    return true;
                }
                if (player.getInventory().hasRoomFor(REFERRAL_TICKET)) {
                    npc("Of course! Here you go.");
                    player.getInventory().add(REFERRAL_TICKET);
                    stage = END;
                    return true;
                }
                npc("Of course! I've placed it in your bank.");
                player.getBank().add(REFERRAL_TICKET);
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
        return new int[]{ 945 };
    }
}
