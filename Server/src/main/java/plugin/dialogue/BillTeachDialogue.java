package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for Bill Teach.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class BillTeachDialogue extends DialoguePlugin {

    /**
     * Constructs a new <code>BillTeachDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     */
    public BillTeachDialogue() {
    }

    /**
     * Constructs a new <code>BillTeachDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     *
     * @param player The player.
     */
    public BillTeachDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BillTeachDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npc("Mumblelandlubbermumblemumble...");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                player("Hello!");
                stage = 1;
                break;
            case 1:
                npc("What right have ye to be so happy, landlubber?");
                stage = 2;
                break;
            case 2:
                player("Well it is quite a nice day.");
                stage = 3;
                break;
            case 3:
                npc("Aye, that it is.");
                stage = 4;
                break;
            case 4:
                npc("Will ye forgive an old sailor his temper lad? I'm in dire", "straits indeed.");
                stage = 5;
                break;
            case 5:
                player("What's the problem? I may be able to help.");
                stage = 6;
                break;
            case 6:
                npc("I dunno, lad, I need a pirate to help me with my", "problem, but none will sail with me.");
                stage = 7;
                break;
            case 7:
                npc("Well, ye look like ye might have the right stuff, tell me", "would ye like te be a pirate?");
                stage = 8;
                break;
            case 8:
                player("No thanks, it sounds quite dangerous.");
                stage = 9;
                break;
            case 9:
                npc("Aye, that it be. Come back when yer living ain't so lily-", "like, lad.");
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
        return new int[]{ 3155 };
    }
}
