package plugin.activity.wguild;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;

/**
 * Handles the warrior guild options.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class WarriorsGuild extends OptionHandler {

    /**
     * The token item id.
     */
    public static final int TOKEN = 8851;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(15653).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(1530).getConfigurations().put("option:open", this);
        NPCDefinition.forId(4287).getConfigurations().put("option:claim-shield", this);
        NPCDefinition.setOptionHandler("claim-tokens", this);
        PluginManager.definePlugin(new ClaimTokenDialogue());
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (node.getId()) {
            case 15653:
                if (canEnter(player)) {
                    player.getMusicPlayer().unlock(634);
                    DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
                } else {
                    player.getDialogueInterpreter().sendDialogues(4285, null, "You not pass. You too weedy.");
                }
                break;
            case 1530:
                DoorActionHandler.handleDoor(player, (GameObject) node);
                break;
            default:
                switch (option) {
                    case "claim-shield":
                        player.getDialogueInterpreter().open(4287, node, true);
                        break;
                    case "claim-tokens":
                        player.getDialogueInterpreter().open("wg:claim-tokens", node.getId());
                        break;
                }
                break;
        }
        return true;
    }

    /**
     * Checks if a player can enter the guild.
     *
     * @param player the player.
     * @return {@code True} if so.
     */
    private boolean canEnter(final Player player) {
        return player.getSkills().getStaticLevel(Skills.ATTACK) +
            player.getSkills().getStaticLevel(Skills.STRENGTH) >= 130 ||
            player.getSkills().getStaticLevel(Skills.ATTACK) == 99 ||
            player.getSkills().getStaticLevel(Skills.STRENGTH) == 99;
    }

    /**
     * The dialogue used to handle the claiming of tokens.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class ClaimTokenDialogue extends DialoguePlugin {

        /**
         * The npc id being used.
         */
        private int npcId;

        /**
         * Constructs a new {@code ClaimTokenDialogue} {@code Object}.
         */
        public ClaimTokenDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code ClaimTokenDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public ClaimTokenDialogue(final Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new ClaimTokenDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npcId = (Integer) args[0];
            player("May I claim my tokens please?");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            final int tokens = player.getSavedData().getActivityData().getWarriorGuildTokens();
            switch (stage) {
                case 0:
                    if (tokens < 1) {
                        interpreter.sendDialogues(npcId, null, "I'm afraid you have not earned any tokens yet. Try", "some of the activities around the guild to earn some.");
                        stage = 3;
                    } else {
                        interpreter.sendDialogues(npcId, null, "Of course! Here you go, you've earned " + tokens + " tokens!");
                        stage++;
                    }
                    break;
                case 1:
                    final Item item = new Item(TOKEN, tokens);
                    if (!player.getInventory().hasRoomFor(item)) {
                        player("Sorry, I don't seem to have enough inventory space.");
                        stage++;
                        break;
                    }
                    player.getSavedData().getActivityData().setWarriorGuildTokens(0);
                    player.getInventory().add(item);
                    player("Thanks!");
                    stage++;
                    break;
                case 2:
                    end();
                    break;
                case 3:
                    player("Ok, I'll go see what I can find.");
                    stage--;
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ DialogueInterpreter.getDialogueKey("wg:claim-tokens") };
        }

    }
}
