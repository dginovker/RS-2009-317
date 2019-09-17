package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.misc.RunScript;
import org.gielinor.utilities.security.BCrypt;

/**
 * The dialogue plugin for claiming experience from an XP Token.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ChangePassword extends DialoguePlugin {

    private String currentPassword = "";
    private String newPassword = "";

    public ChangePassword() {
    }

    /**
     * Constructs a new {@link ChangePassword} {@link DialoguePlugin}.
     *
     * @param player
     *            The player.
     */
    public ChangePassword(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new ChangePassword(player);
    }

    @Override
    public boolean open(Object... args) {
        stage = 0;
        interpreter.sendPlaneMessage(false, "Enter your current password to proceed:");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                promptCurrentPassword();
                stage = 1;
                break;
            case 1:
                if (checkPassword(currentPassword)) {
                    interpreter.sendPlaneMessage("The entered password is correct,", "you may now change your password.");
                    stage = 2;
                } else {
                    interpreter.sendPlaneMessage("Incorrect password.");
                    stage = END;
                }
                break;
            case 2:
                interpreter.sendOptions("Are you sure?", "Yes, change my password", "No, I've changed my mind");
                stage = 3;
                break;
            case 3:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        promptNewPassword();
                        stage = 4;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
            case 4:
                interpreter.sendPlaneMessage("Is this correct?");
                stage = 5;
                break;
            case 5:
                interpreter.sendOptions("Is this correct?", "Yep, change my password now", "No, let me enter it again");
                stage = 6;
                break;
            case 6:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        String newSalt = BCrypt.gensalt();
                        String newHashedPassword = BCrypt.hashpw(newPassword, "$2a$13$" + newSalt);
                        player.getDetails().setPassword(newHashedPassword);
                        player.getDetails().setSalt(newSalt);
                        if (player.getDetails().savePassword()) {
                            interpreter.sendPlaneMessage("Password changed.");
                            player.getActionSender().sendMessage("Password successfully changed.");
                        } else {
                            interpreter.sendPlaneMessage("Failed to change password!");
                        }
                        stage = END;
                        break;
                    case TWO_OPTION_TWO:
                        promptNewPassword();
                        stage = 4;
                        break;
                }
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    private void promptNewPassword() {
        player.setAttribute("runscript", new RunScript() {

            @Override
            public boolean handle() {
                newPassword = (String) value;
                interpreter.sendPlaneMessage("You have entered:", "'" + newPassword.replace("_", " ") + "'");
                return true;
            }
        });
        interpreter.sendPWInput(true, "Enter new password:");
    }

    private void promptCurrentPassword() {
        player.setAttribute("runscript", new RunScript() {

            @Override
            public boolean handle() {
                currentPassword = (String) value;
                interpreter.sendPlaneMessage("You entered:", "'" + currentPassword.replace("_", " ") + "'");
                return true;
            }
        });
        interpreter.sendPWInput(true, "Enter current password:");
    }

    private boolean checkPassword(String toCheck) {
        return BCrypt.hashpw(toCheck, "$2a$13$" + player.getDetails().getSalt()).equals(player.getDetails().getPassword());
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("ChangePassword") };
    }

}
