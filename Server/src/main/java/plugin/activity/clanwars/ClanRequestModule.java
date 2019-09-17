package plugin.activity.clanwars;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.request.RequestModule;

/**
 * Represents the clan challenge {@link org.gielinor.game.node.entity.player.link.request.RequestModule}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ClanRequestModule implements RequestModule {

    @Override
    public void open(Player player, Player target) {
        WarSession warSession;
        WarSession opponentSession;
        player.addExtension(WarSession.class, warSession = new WarSession(player, target));
        target.addExtension(WarSession.class, opponentSession = new WarSession(target, player));
        warSession.openSetup();
        opponentSession.openSetup();
    }
}
