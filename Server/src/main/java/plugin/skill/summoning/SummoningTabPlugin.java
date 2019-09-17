package plugin.skill.summoning;


import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.content.skill.member.summoning.pet.Pet;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the component plugin handler for the summoning tab.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class SummoningTabPlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(25904, this);
        ComponentDefinition.put(25951, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != 25904 && component.getId() != 25951) {
            return false;
        }
        if (component.getId() == 25951) {
            if (!player.getFamiliarManager().hasFamiliar()) {
                player.getActionSender().sendMessage("You do not have a follower.");
                return true;
            }
            Familiar familiar = player.getFamiliarManager().getFamiliar();
            boolean pet = familiar instanceof Pet;
            boolean beastOfBurden = familiar.isBurdenBeast();
            boolean combatFamiliar = familiar.isCombatFamiliar();
            switch (button) {
                case 25954:
                case 25955:
                    player.setAttribute("summ_left_click", "Follower details");
                    player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 1, false);
                    break;

                case 25956:
                case 25957:
                    if (pet) {
                        player.getActionSender().sendMessage("Your follower is a pet.");
                        return true;
                    }
                    player.setAttribute("summ_left_click", "Special attack");
                    player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 2, false);
                    break;

                case 25958:
                case 25959:
                    if (pet) {
                        player.getActionSender().sendMessage("Your follower is a pet.");
                        return true;
                    }
                    if (!combatFamiliar) {
                        player.getActionSender().sendMessage("Your follower is not a combat familiar.");
                        return true;
                    }
                    player.setAttribute("summ_left_click", "Attack");
                    player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 3, false);
                    break;

                case 25960:
                case 25961:
                    player.setAttribute("summ_left_click", "Call follower");
                    player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 4, false);
                    break;

                case 25962:
                case 25963:
                    player.setAttribute("summ_left_click", "Dismiss follower");
                    player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 5, false);
                    break;

                case 25964:
                case 25965:
                    if (pet) {
                        player.getActionSender().sendMessage("Your follower is a pet.");
                        return true;
                    }
                    if (!beastOfBurden) {
                        player.getActionSender().sendMessage("Your follower is not a beast of burden.");
                        return true;
                    }
                    player.setAttribute("summ_left_click", "Take BoB");
                    player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 6, false);
                    break;

                case 25966:
                case 25967:
                    if (pet) {
                        player.getActionSender().sendMessage("Your follower is a pet.");
                        return true;
                    }
                    if (true) {
                        player.getActionSender().sendMessage("Coming soon...");
                        return true;
                    }
                    player.setAttribute("summ_left_click", "Renew familiar");
                    player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 7, false);
                    break;

                case 25968: // Confirm
                    player.getInterfaceState().closeOverlay();
                    player.getInterfaceState().openDefaultTabs();
                    player.getSavedData().getGlobalData().setLeftClickOption(player.getAttribute("summ_left_click", player.getSavedData().getGlobalData().getLeftClickOption()));
                    player.getFamiliarManager().sendLeftClickOptions();
                    break;
            }
            return true;
        }
        switch (button) {
            case 25928:
                if (player.getFamiliarManager().hasFamiliar()) {
                    player.getFamiliarManager().getFamiliar().call();
                } else {
                    player.getActionSender().sendMessage("You don't have a follower.");
                }
                break;
            case 25931:
                if (player.getFamiliarManager().hasFamiliar()) {
                    if (!player.getFamiliarManager().getFamiliar().isBurdenBeast()) {
                        player.getActionSender().sendMessage("Your familiar is not a beast of burden.");
                        break;
                    }
                    BurdenBeast burdenBeast = (BurdenBeast) player.getFamiliarManager().getFamiliar();
                    if (burdenBeast.getContainer().isEmpty()) {
                        player.getActionSender().sendMessage("Your familiar is not carrying any items.");
                        break;
                    }
                    burdenBeast.withdrawAll();
                    break;
                }
                player.getActionSender().sendMessage("You don't have a follower.");
                break;
            case 25934:
                if (player.getFamiliarManager().hasFamiliar()) {
                    player.getDialogueInterpreter().open("dismiss_dial");
                } else {
                    player.getActionSender().sendMessage("You don't have a follower.");
                }
                break;
            case 25942:
                if (player.getFamiliarManager().hasFamiliar()) {
                    player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(player));
                } else {
                    player.getActionSender().sendMessage("You don't have a follower.");
                }
                break;
        }
        return true;
    }

}