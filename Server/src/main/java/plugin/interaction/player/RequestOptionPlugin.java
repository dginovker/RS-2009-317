package plugin.interaction.player;

import java.security.SecureRandom;

import org.gielinor.game.interaction.Option;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.entity.player.link.request.RequestType;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.config.ServerVar;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.string.TextUtils;

import plugin.activity.duelarena.DuelSession;
import plugin.activity.duelarena.DuelStage;

/**
 * Represents the plugin used to handle the player option interacting.
 *
 * @author 'Vexia
 */
public final class RequestOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        Option._P_ASSIST.setHandler(this);
        Option._P_TRADE.setHandler(this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        Player target = (Player) node;
        if (player.getAttribute("PICKPOCKET") != null) {
            player.getActionSender()
                .sendMessage("You attempt to pickpocket " + TextUtils.formatDisplayName(target.getName()) + "...");
            player.playAnimation(new Animation(881));
            final boolean success = new SecureRandom().nextInt(4) != 2;
            World.submit(new Pulse(3) {

                @Override
                public boolean pulse() {
                    if (!success) {
                        player.animate(new Animation(424));
                        player.getActionSender().sendSound(new Audio(1842));
                        player.getStateManager().set(EntityState.STUNNED, 4);
                        player.setAttribute("thief-delay", World.getTicks() + 4);
                        player.getActionSender().sendMessage(
                            "You fail to pickpocket " + TextUtils.formatDisplayName(target.getName()) + "!");
                        target.animate(player.getProperties().getAttackAnimation());
                        target.sendChat("What do you think you're doing?!");
                        target.faceLocation(player.getLocation());
                        player.getImpactHandler().manualHit(target, 69, ImpactHandler.HitsplatType.NORMAL);
                        return true;
                    }
                    Item pickpocket = target.getInventory().getRandomItem();
                    if (pickpocket != null) {
                        target.getActionSender().sendMessage(
                            TextUtils.formatDisplayName(player.getName()) + " has picked your pocket!");
                        target.getInventory().remove(pickpocket);
                        player.getInventory().add(pickpocket);
                        player.getActionSender().sendMessage(
                            "You pick " + TextUtils.formatDisplayName(target.getName()) + "'s pocket!");
                    }
                    return true;
                }
            });
            return true;
        }
        DuelSession duelSession = DuelSession.getExtension(player);
        if (duelSession != null) {
            if (DuelSession.getExtension(player).getDuelStage() == DuelStage.IN_PROGRESS) {
                player.getActionSender().sendMessage("You cannot trade while in a duel!");
                return true;
            }
        }
        duelSession = DuelSession.getExtension((Player) node);
        if (duelSession != null) {
            if (DuelSession.getExtension(player).getDuelStage() == DuelStage.IN_PROGRESS) {
                player.getActionSender().sendMessage("You cannot trade that player while they are in a duel!");
                return true;
            }
        }
        if (ServerVar.fetch("player_trading_disabled", 0) == 1) {
            player.getDialogueInterpreter().sendPlaneMessage("Player trading has been disabled.");
            return true;
        }
        if (player.getSavedData().getGlobalData().getTotalPlayTime() < Constants.MINIMUM_PLAY_TIME) {
            player.getActionSender().sendMessage("You must have been playing for 30 minutes to trade.");
            return true;
        }
        if (!player.canInteractWith(target)) {
            player.getActionSender().sendMessage("You cannot trade this player.");
            return true;
        }
        player.getRequestManager().request((Player) node, RequestType.forOption(option));
        return true;
    }
}
