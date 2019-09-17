package plugin.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.string.TextUtils;
import plugin.skill.herblore.PotionDecantingPlugin;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BobBarterNPC extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(6524).getConfigurations().put("option:decant", this);
        PluginManager.definePlugin(new BobBarterDialogue());
        return this;
    }

    @Override
    public boolean handle(final Player player, final Node node, String option) {
        int decant = PotionDecantingPlugin.decantAll(player);
        player.lock(decant > 1 ? 2 : 1);
        World.submit(new Pulse(decant > 1 ? 2 : 1) {

            @Override
            public boolean pulse() {
                player.getDialogueInterpreter().open(6524, node, decant);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    /**
     * Represents the bob barter npc dialogue plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static class BobBarterDialogue extends DialoguePlugin {

        /**
         * Constructs a new {@code BobBarterDialogue} {@code Object}.
         */
        public BobBarterDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code BobBarterDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public BobBarterDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new BobBarterDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            if (args.length == 2) {
                int decanted = (int) args[1];
                if (decanted < 1) {
                    npc("There's no potions for me to decant, " + TextUtils.formatDisplayName(player.getName()));
                    stage = END;
                    return true;
                }
                npc("There you go, " + TextUtils.formatDisplayName(player.getName()) + "!");
                stage = END;
                return true;
            }
            if (player.getDonorManager().getDonorStatus().getCanDecant()) {
                player("Hi.");
                stage = 0;
            } else {
                player.getDialogueInterpreter().sendPlaneMessage("You must be an Emerald member or higher to decant.");
                stage = END;
            }
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    npc("Hello, chum, fancy buyin' some designer", "jewellery? They've come all the way from", "Ardougne! most pukka!");
                    stage = 1;
                    break;
                case 1:
                    player("Erm, no. I'm all set, thanks.");
                    stage = 2;
                    break;
                case 2:
                    npc("Okay, chum. So what can I do for you? I can", "tell you the very latest herb prices.");
                    stage = 3;
                    break;
                case 3:
                    interpreter.sendOptions("Select an Option", "Who are you?", "Can you help me out with the prices for herbs?", "Sorry, I've got to split.");
                    stage = 4;
                    break;
                case 4:
                    switch (optionSelect) {
                        case THREE_OPTION_ONE:
                            player("Who are you?");
                            stage = 10;
                            break;
                        case THREE_OPTION_TWO:
                            player("Can you help me out with the prices for herbs?");
                            stage = 20;
                            break;
                        case THREE_OPTION_THREE:
                            player("Sorry, I've got to split.");
                            stage = 30;
                            break;
                    }
                    break;
                case 30:
                    end();
                    break;
                case 10:
                    npc("Why, I'm Bob! Your friendly seller of goods!");
                    stage = 11;
                    break;
                case 11:
                    player("So what do you have to sell?");
                    stage = 12;
                    break;
                case 12:
                    npc("Oh, not much at the moment. Cuz, ya know", "Business being so well and cushie.");
                    stage = 13;
                    break;
                case 13:
                    player("You don't really look like you're being so", "successful.");
                    stage = 14;
                    break;
                case 14:
                    npc("You plonka! It's all a show, innit! If I let people", "knows I'm in good business they'll want a", "share of the moolah!");
                    stage = 15;
                    break;
                case 15:
                    end();
                    break;
                case 20:
                    end();
                    // GEGuidePrice.open(player, GuideType.HERBS);
                    break;
                case END:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 6524 };
        }
    }
}
