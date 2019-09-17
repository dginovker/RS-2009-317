package plugin.activity.duelarena;


import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.request.RequestModule;

/**
 * Handles a duel request getting accepted.
 *
 * @author Emperor
 */
public final class DuelReqModule implements RequestModule {

    /**
     * Represents if this session has accepted.
     */
    private boolean accepted;

    /**
     * Constructs a new {@code DuelReqModule}.
     */
    public DuelReqModule() {

    }

    @Override
    public void open(Player player, Player target) {
        DuelSession session = new DuelSession(player, target);
        player.addExtension(DuelSession.class, new DuelSession(player, target));
        target.addExtension(DuelSession.class, new DuelSession(target, player));
        session.openRules();
    }

    /**
     * Gets if this session has accepted.
     *
     * @return <code>True</code> if so.
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Sets if this session has accepted.
     *
     * @param accepted If this session is accepted.
     */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}