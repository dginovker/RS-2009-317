package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.quest.Quest;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the gertrude cat dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class GertrudesCatDialogue extends DialoguePlugin {

    /**
     * Represents the animation of bending down.
     */
    private final Animation BEND_DOWN = Animation.create(827);

    /**
     * Constructs a new {@code GertrudesCatDialogue} {@code Object}.
     */
    public GertrudesCatDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code GertrudesCatDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public GertrudesCatDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GertrudesCatDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendOptions("Select an Option", "Talk-with", "Pick-up", "Stroke");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        final Quest quest = player.getQuestRepository().getQuest("Gertrude's Cat");
        switch (stage) {
            case 545:
                end();
                break;
            case 0:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        if (quest.getStage() < 60) {
                            end();
                            npc.sendChat("Miaoww");
                        } else {
                            interpreter.sendDialogues(player, null, "Oh, it looks like Fluffs has returned.", "I'd better leave her alone.");
                            stage = 99;
                        }
                        break;
                    case THREE_OPTION_TWO:
                        close();
                        player.getPulseManager().run(new Pulse(1, player) {

                            int count = 0;

                            @Override
                            public boolean pulse() {
                                switch (count++) {
                                    case 0:
                                        player.animate(BEND_DOWN);
                                        break;
                                    case 2:
                                        npc.sendChat("Hisss!");
                                        break;
                                    case 3:
                                        player.sendChat("Ouch!");
                                        break;
                                    case 4:
                                        if (quest.getStage() == 40) {
                                            interpreter.sendPlaneMessage("The cat seems afraid to leave.", "In the distance you can hear kittens mewing...");
                                            stage = 545;
                                            return true;
                                        }
                                        if (quest.getStage() == 30) {
                                            interpreter.sendPlaneMessage("Maybe the cat is hungry?");
                                            stage = 545;
                                            return true;
                                        }
                                        if (quest.getStage() == 50) {
                                            end();
                                            return true;
                                        }
                                        end();
                                        interpreter.sendPlaneMessage("Maybe the cat is thirsty?");
                                        stage = 545;
                                        break;
                                }
                                return count == 5;
                            }
                        });
                        break;
                    case THREE_OPTION_THREE:
                        close();
                        player.getPulseManager().run(new Pulse(1, player) {

                            int count = 0;

                            @Override
                            public boolean pulse() {
                                switch (count++) {
                                    case 0:
                                        player.animate(BEND_DOWN);
                                        break;
                                    case 2:
                                        npc.sendChat("Hisss!");
                                        break;
                                    case 3:
                                        player.sendChat("Ouch!");
                                        break;
                                    case 4:
                                        if (quest.getStage() == 40) {
                                            return true;
                                        }
                                        interpreter.sendPlaneMessage("Perhaps the cat want's something?");
                                        stage = 545;
                                        break;
                                }
                                return count == 5;
                            }

                        });
                        if (quest.getStage() == 40) {
                            return true;
                        }
                        World.submit(new Pulse(7, player) {

                            @Override
                            public boolean pulse() {
                                end();
                                return true;
                            }

                        });
                        break;
                }
                break;
            case 99:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2997 };
    }
}
