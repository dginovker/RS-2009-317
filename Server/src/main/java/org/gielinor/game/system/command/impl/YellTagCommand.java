package org.gielinor.game.system.command.impl;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.string.TextUtils;

/**
 * Changes the players yell tag
 *
 * @author Corey/Ipsum
 */
public class YellTagCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.SAPPHIRE_MEMBER;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "yelltag" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("yelltag", "Change the tag of your yell", getRights(), "::yelltag <lt>tag>"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!canChange(player)) {
            player.getActionSender().sendMessage("You are not permitted to use this command!");
            return;
        }
        if (args.length > 1) {

            StringBuilder tagBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                tagBuilder.append(args[i] + " ");
            }
            String newTag = tagBuilder.toString().trim();

            if (newTag.length() > Constants.YELL_TAG_MAX_LENGTH) {
                player.getActionSender().sendMessage("Yell tags cannot be more than " + Constants.YELL_TAG_MAX_LENGTH + " characters long!");
                return;
            }
            if (containsDisallowedPhrase(newTag) && player.getRights() != Rights.GIELINOR_MODERATOR && player.getRights() != Rights.DEVELOPER) {
                player.getActionSender().sendMessage("Your yell tag contains one or more disallowed phrases.");
                return;
            }
            int cost = player.getDonorManager().getDonorStatus().getYellTagCost();
            String costFormatted = TextUtils.getFormattedNumber(cost) + " GP";

            player.getDialogueInterpreter().setDialogue(new DialoguePlugin() {

                @Override
                public DialoguePlugin newInstance(Player player) {
                    return this;
                }

                @Override
                public boolean open(Object... args) {
                    interpreter.sendOptions("Are you sure you wish to pay " + costFormatted + "?", "Yes, pay " + costFormatted + ".", "No.");
                    stage = 0;
                    return true;
                }

                @Override
                public boolean handle(int interfaceId, OptionSelect optionSelect) {
                    switch (stage) {
                        case 0:
                            switch (optionSelect) {
                                case TWO_OPTION_ONE:
                                    if (player.getInventory().remove(new Item(Item.COINS, cost)) || player.getBank().remove(new Item(Item.COINS, cost))) {
                                        player.getSavedData().getGlobalData().setYellTag(newTag);
                                        interpreter.sendPlaneMessage("Your new yell tag is: " + newTag);
                                        stage = END;
                                    } else {
                                        interpreter.sendPlaneMessage("You don't have enough money!");
                                        stage = END;
                                    }
                                    break;
                                case TWO_OPTION_TWO:
                                    stage = END;
                                    break;
                            }
                            break;
                        case END:
                            end();
                            break;
                    }
                    return true;
                }

                @Override
                public int[] getIds() {
                    return new int[0];
                }
            });
        } else {
            player.getActionSender().sendMessage("No tag specified!");
            player.getActionSender().sendMessage("Use as ::yelltag <lt>tag>");
        }
    }

    public static boolean canChange(Player player) {
        switch (player.getRights()) {
            case PLAYER_MODERATOR:
            case DEVELOPER:
            case GIELINOR_MODERATOR:
                return true;
        }
        if (player.getDonorManager().hasMembership()) {
            return true;
        }

        return false;
    }

    private boolean containsDisallowedPhrase(String tag) {
        for (String phrase : Constants.DISALLOWED_TAG_PHRASES) {
            if (tag.toLowerCase().contains(phrase.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
