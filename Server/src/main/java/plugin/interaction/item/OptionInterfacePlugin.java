package plugin.interaction.item;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.portal.punishment.Punishment;
import org.gielinor.game.node.entity.player.info.referral.Referred;
import org.gielinor.game.system.data.DataShelf;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;

import java.util.List;

/**
 * Represents the {@link org.gielinor.game.component.ComponentPlugin} for the custom option interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class OptionInterfacePlugin extends ComponentPlugin {

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (player.getAttribute("OPTION_INTERFACE") == null) {
            return false;
        }
        switch ((String) player.getAttribute("OPTION_INTERFACE")) {
            case "TRANSMOGRIFY":
                player.removeAttribute("OPTION_INTERFACE");
                player.getInterfaceState().closeUnchecked();
                List transmogrifyMap = (List) DataShelf.fetchListMap("transmogrify");
                if (transmogrifyMap == null) {
                    return false;
                }
                int index = 0;
                for (Object transmogrify : transmogrifyMap) {
                    List transmogrifyList = (List) transmogrify;
                    if (transmogrifyList.get(0) == null || transmogrifyList.get(1) == null) {
                        continue;
                    }
                    if (button == (27703 + index)) {
                        switch ((int) transmogrifyList.get(2)) {
                            case 1:
                                player.getAppearance().transformNPC((int) transmogrifyList.get(1));
                                break;
                            case 2:
                                player.getAppearance().transformItem((int) transmogrifyList.get(1));
                                break;
                            case 3:
                                player.getAppearance().transformObject((int) transmogrifyList.get(1));
                                break;
                        }
                        return true;
                    }
                    index++;
                }
                return true;

            case "REFERRALS":
                player.removeAttribute("OPTION_INTERFACE");
                player.getInterfaceState().closeUnchecked();
                index = 0;
                List<Referred> refersList = player.getReferralManager().getReferrals();
                Referred[] referrals = refersList.toArray(new Referred[refersList.size()]);
                for (Referred referred : referrals) {
                    if (!referred.isRequested()) {
                        continue;
                    }
                    if (button == (27706 + index)) {
                        player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("referral-accept"), referred);
                        return true;
                    }
                    index++;
                }
                break;

            case "BAN_HAMMER":
                if (!BanHammerPlugin.canUse(player)) {
                    return true;
                }
                Player target = player.getAttribute("BAN_HAMMER_TARGET");
                boolean remoteControl = player.getAttribute("BAN_HAMMER_REMOTE_CONTROL", false);

                player.removeAttribute("BAN_HAMMER_TARGET");
                player.removeAttribute("OPTION_INTERFACE");
                player.getInterfaceState().closeUnchecked();
                @SuppressWarnings("unchecked")
                List<String> punishmentTypes = (List<String>) DataShelf.fetchListMap("ban_hammer_punishments");
                if (punishmentTypes == null) {
                    return true;
                }

                int i = 0;
                for (String punishment : punishmentTypes) {
                    if (button == (27703 + i)) {
                        switch (punishment.toLowerCase()) {
                            case "freeze player":
                                player.getActionSender().sendMessage("Freezing " + target.getUsername());
                                target.unlock();
                                target.getWalkingQueue().reset();
                                target.getLocks().lockMovement(100000000);
                                break;

                            case "unfreeze player":
                                player.getActionSender().sendMessage("Unfreezing " + target.getUsername());
                                target.getWalkingQueue().reset();
                                target.getLocks().unlockMovement();
                                break;

                            case "mute player":
                                player.setAttribute("runscript", new RunScript() {

                                    @Override
                                    public boolean handle() {
                                        String value = (String) getValue();
                                        long duration;

                                        try {
                                            duration = Punishment.getPunishmentDuration(value);
                                        } catch (NumberFormatException e) {
                                            player.getActionSender().sendMessage("Please enter a valid punishment modifier!");
                                            return true;
                                        } catch (IllegalArgumentException e) {
                                            player.getActionSender().sendMessage("Please enter a valid punishment unit!");
                                            return true;
                                        }

                                        player.setAttribute("runscript", new RunScript() {

                                            @Override
                                            public boolean handle() {
                                                String reason = (String) getValue();
                                                if (reason == null || reason.trim().length() == 0) {
                                                    player.getActionSender().sendMessage("A valid punishment reason is required.");
                                                    return true;
                                                }
                                                if (!target.getDetails().getPortal().getMute().punish(target, player, duration, reason)) {
                                                    player.getActionSender().sendMessage("Could not punish target.");
                                                }
                                                return true;
                                            }
                                        });
                                        player.getDialogueInterpreter().sendInput(true, "Mute reason:");

                                        return true;
                                    }
                                });
                                player.getDialogueInterpreter().sendInput(true, "Enter punishment duration:");
                                break;

                            case "unmute player":
                                if (!target.getDetails().getPortal().getMute().isPunished()) {
                                    player.getActionSender().sendMessage("Target is not muted!");
                                    break;
                                }
                                target.getDetails().getPortal().getMute().reverse();
                                player.getActionSender().sendMessage("Unmuted " + target.getUsername());
                                target.getActionSender().sendMessage("You have been unmuted.");
                                Repository.getStaffOnline().stream().forEach(staff -> staff.getActionSender().sendMessage("<img=0>[<col=800000>Punishments</col>] <col=255>" + target.getUsername() + "</col> has been manually unmuted."));
                                break;

                            case "ban player":
                                player.setAttribute("runscript", new RunScript() {

                                    @Override
                                    public boolean handle() {
                                        String value = (String) getValue();
                                        long duration;

                                        try {
                                            duration = Punishment.getPunishmentDuration(value);
                                        } catch (NumberFormatException e) {
                                            player.getActionSender().sendMessage("Please enter a valid punishment modifier!");
                                            return true;
                                        } catch (IllegalArgumentException e) {
                                            player.getActionSender().sendMessage("Please enter a valid punishment unit!");
                                            return true;
                                        }

                                        player.setAttribute("runscript", new RunScript() {

                                            @Override
                                            public boolean handle() {
                                                String reason = (String) getValue();
                                                if (reason == null || reason.trim().length() == 0) {
                                                    player.getActionSender().sendMessage("A valid punishment reason is required.");
                                                    return true;
                                                }
                                                if (!target.getDetails().getPortal().getBan().punish(target, player, duration, reason)) {
                                                    player.getActionSender().sendMessage("Could not punish target.");
                                                }
                                                return true;
                                            }
                                        });
                                        player.getDialogueInterpreter().sendInput(true, "Ban reason:");

                                        return true;
                                    }
                                });
                                player.getDialogueInterpreter().sendInput(true, "Enter punishment duration:");
                                break;

                            case "unban player":
                                target.getDetails().getPortal().getBan().reverse();
                                player.getActionSender().sendMessage("Unbanned " + target.getUsername());
                                break;

                            case "check bank":
                            case "check inventory":
                                player.getActionSender().sendMessage("Coming soon...");
                                break;

                            case "teleport to":
                                player.getActionSender().sendMessage("Teleporting to " + target.getUsername());
                                player.teleport(target.getLocation());
                                break;

                            case "teleport to me":
                                player.getActionSender().sendMessage("Teleporting " + target.getUsername() + " to you.");
                                target.teleport(player.getLocation());
                                break;

                            case "kick":
                                player.getActionSender().sendMessage("Kicking " + target.getUsername());
                                target.getSession().disconnect();
                                break;

                            case "close":
                                break;
                        }
                        if (!remoteControl) {
                            player.lock(1);
                            player.faceLocation(target.getLocation());
                            player.playAnimation(new Animation(2067));
                        }
                        return true;
                    }
                    i++;
                }
                return true;

        }
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.forId(27700).setPlugin(this);
        return this;
    }
}
