package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.quest.Quest;
import org.gielinor.game.content.global.quest.impl.TheLostKingdom;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.pulse.Pulse;

import plugin.interaction.inter.EmoteTabInterface;

/**
 * 5478
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class KingGjukiSorvottIV extends DialoguePlugin {

    /**
     * The {@link org.gielinor.game.content.global.quest.impl.TheLostKingdom} quest.
     */
    private Quest quest;

    /**
     * Constructs a new {@code KingGjukiSorvottIV} {@code Object}.
     */
    public KingGjukiSorvottIV() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code KingGjukiSorvottIV} {@code Object}.
     *
     * @param player the player.
     */
    public KingGjukiSorvottIV(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new KingGjukiSorvottIV(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        quest = player.getQuestRepository().getQuest(TheLostKingdom.NAME);
        if (quest.getStage() == 21 && TheLostKingdom.isWearingJester(player)) {
            npc("Ah, the entertainment!", "What say ye, Jester?");
            stage = 1;
            return true;
        }
        if (quest.getStage() != 15) {
            player.getActionSender().sendMessage("He doesn't seem interested in talking to you.");
            end();
            return true;
        }
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                quest.setStage(20);
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Away from me, peasant!");
                stage = END;
                break;
            case 1:
                player("I am here for your benefit!", "What's on your mind, my King?");
                stage = 2;
                break;
            case 2:
                interpreter.sendPlaneMessage(true, "You dance for King Gjuki Sorvott.");
                player.lock(7);
                EmoteTabInterface.Emote.DANCE.play(player);
                player.getPulseManager().run(new Pulse(7) {

                    @Override
                    public boolean pulse() {
                        interpreter.sendPlaneMessage(false, "You dance for King Gjuki Sorvott.");
                        return true;
                    }
                });
                stage = 3;
                break;
            case 3:
                npc("Well... I'm just so tired, being as great", "as I am is hard practice.", "For example, this worthless King Roald...");
                stage = 4;
                break;
            case 4:
                npc("He has no idea of my soon attack!", "Its in fact, really great.", "My attack will be at night, and he", "will suffer a great loss!");
                stage = 5;
                break;
            case 5:
                interpreter.sendPlaneMessage(true, "You jig for King Gjuki Sorvott.");
                player.lock(6);
                EmoteTabInterface.Emote.JIG.play(player);
                player.getPulseManager().run(new Pulse(6) {

                    @Override
                    public boolean pulse() {
                        interpreter.sendPlaneMessage(false, "You jig for King Gjuki Sorvott.");
                        return true;
                    }
                });
                stage = 6;
                break;
            case 6:
                npc("Ah... Yes, with my ultimate force, he", "will not be able to stop me.");
                stage = 7;
                break;
            case 7:
                player("Would you attack through the front doors?", "Being the almighty king you are, I'm", "positive you have no doubt in your", "plans!");
                stage = 8;
                break;
            case 8:
                interpreter.sendPlaneMessage(true, "You twirl for King Gjuki Sorvott.");
                player.lock(2);
                EmoteTabInterface.Emote.SPIN.play(player);
                player.getPulseManager().run(new Pulse(2) {

                    @Override
                    public boolean pulse() {
                        interpreter.sendPlaneMessage(false, "You twirl for King Gjuki Sorvott.");
                        return true;
                    }
                });
                stage = 9;
                break;
            case 9:
                npc("I am almighty! I will attack through the front", "doors without anyone noticing, its so", "diabolical and simple, I don't see anyone", "figuring it out!");
                stage = 10;
                break;
            case 10:
                quest.setStage(25);
                npc("That's enough for now you Jester,", "I must rest to prepare for the attack!");
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
        return new int[]{ 5478 };
    }
}
