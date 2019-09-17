package plugin.activity.duelarena;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;

/**
 * The {@link org.gielinor.game.component.CloseEvent} for declining a duel.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DuelCloseEvent implements CloseEvent {

    @Override
    public void close(Player player, Component component) {
        final DuelSession duelSession = DuelSession.getExtension(player);
        if (duelSession == null) {
            return;
        }
        if (duelSession.getDuelStage() == DuelStage.IN_PROGRESS &&
            DuelSession.getExtension(duelSession.getOpponent()).getDuelStage() == DuelStage.IN_PROGRESS) {
            return;
        }
        DuelSession.decline(player);
    }

    @Override
    public boolean canClose(Player player, Component component) {
        return true;
    }
}
