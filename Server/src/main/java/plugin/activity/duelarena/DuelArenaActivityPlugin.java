package plugin.activity.duelarena;


import org.gielinor.content.periodicity.PeriodicityPulseManager;
import org.gielinor.content.statistics.boards.HighestStakesBoard;
import org.gielinor.content.statistics.boards.LatestDuelsBoard;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.request.RequestType;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneType;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.utilities.string.TextUtils;

import java.util.Date;
import java.util.List;

/**
 * Handles the Duel Arena activity.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class DuelArenaActivityPlugin extends ActivityPlugin {

    /**
     * The overlay.
     */
    private static final Component OVERLAY = new Component(201);
    /**
     * The friendly duel request.
     */
    private static final RequestType DUEL_REQUEST = new RequestType("Sending challenge request...", ":duelreq:", new DuelReqModule(), "duel");

    /**
     * Constructs a new {@code DuelArenaActivity} {@code Object}.
     */
    public DuelArenaActivityPlugin() {
        super("Duel arena", false, false, true);
    }

    @Override
    public boolean continueAttack(Entity e, Node target, CombatStyle style, boolean message) {
        if (e instanceof Familiar) {
            e = ((Familiar) e).getOwner();
        }
        if (target instanceof Familiar) {
            target = ((Familiar) target).getOwner();
        }
        if (e instanceof Player && target instanceof Player) {
            Player player = (Player) e;
            Player other = (Player) target;
            // TODO IF IN DUEL
            if (DuelSession.getExtension(player) == null) {
                return false;
            }
            Player duelPlayer = DuelSession.getExtension(player).getPlayer();
            Player opponent = DuelSession.getExtension(duelPlayer).getOpponent();
            if (other != opponent) {
                player.getActionSender().sendMessage("That is not your opponent!");
                player.getWalkingQueue().reset();
                return true;
            }
            if (!DuelSession.getExtension(player).hasDuelStarted()) {
                player.getActionSender().sendMessage("The duel hasn't started yet!");
                player.getWalkingQueue().reset();
                return true;
            }
        }
        return true;
    }


    @Override
    public boolean enter(Entity e) {
        if (e instanceof Player) {
            Player player = (Player) e;
            player.getInterfaceState().openOverlay(OVERLAY);
            player.getInteraction().set(Option._P_CHALLENGE);
        }
        return super.enter(e);
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        if (e instanceof Player) {
            Player player = (Player) e;
            player.getInterfaceState().closeOverlay();
            player.getInteraction().remove(Option._P_CHALLENGE);
        }
        return super.leave(e, logout);
    }

    @Override
    public boolean death(Entity e, Entity killer) {
        if (e instanceof Player && killer instanceof Player) {
            handleVictory((Player) killer, player);
            return true;
        }
        return true;
    }

    public static void handleVictory(Player p, Player player) {
        DuelSession duelSession = DuelSession.getExtension(p);
        if (duelSession == null) {
            return;
        }
        LatestDuelsBoard.INSTANCE.addStatistic(p.getName(), duelSession.getOpponent().getName(),
            p.getProperties().getCurrentCombatLevel(), duelSession.getOpponent().getProperties().getCurrentCombatLevel());
        DuelSession opponentSession = DuelSession.getExtension(duelSession.getOpponent());
        // TODO Duel scoreboard
        p.setTeleportTarget(DuelArea.getRandomDuelArenaLocation());
        p.getActionSender().sendMessage("Well done! You have defeated " + TextUtils.formatDisplayName(duelSession.getOpponent().getName()) + "!");
        duelSession.getOpponent().getSavedData().getGlobalData().increaseDuelDeaths();
        // 6840 = username 6839 = combat level
        // TODO OSRS victory interface
        p.getActionSender().sendString(6840, TextUtils.formatDisplayName(duelSession.getOpponent().getName()));
        p.getActionSender().sendString(6839, String.valueOf(duelSession.getOpponent().getProperties().getCurrentCombatLevel()));
        List<Item> spoilsList = opponentSession.getSpoils();
        p.getActionSender().sendUpdateItems(6822, spoilsList.toArray(new Item[spoilsList.size()]));
        for (Item item : duelSession.getSpoils()) { // TODO Swap?
            p.getInventory().add(item, player);
        }
        for (Item item : spoilsList) {
            p.getInventory().add(item, player);
        }
        if (spoilsList.size() > 0) {
            PeriodicityPulseManager.Companion.getSTAKING_SATURDAY_INSTANCE().giveReward(p);
            HighestStakesBoard.INSTANCE.addStatistic(p.getName(), opponentSession.getOpponent().getName(),
                duelSession.getSpoilsValue() + opponentSession.getSpoilsValue());
        }
        p.getInterfaceState().open(new Component(6733));
        if (duelSession.getOpponent().isActive() && duelSession.getOpponent().isPlaying()) {
            duelSession.getOpponent().setTeleportTarget(DuelArea.getRandomDuelArenaLocation());
        }
        DuelSession.reset(p);
        DuelSession.reset(duelSession.getOpponent());
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return this;
    }

    @Override
    public Location getSpawnLocation() {
        return DuelArea.getRandomDuelArenaLocation();
    }

    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if (target instanceof Player) {
            Player player = (Player) e;
            Player opponent = (Player) target;
            DuelSession duelSession = DuelSession.getExtension(player);
            if (duelSession == null) {
                return false;
            }
            switch (option.getName().toLowerCase()) {
                case "attack":
                    e.faceLocation(target.getLocation());
                    if (DuelSession.getExtension(opponent) == null || duelSession.getOpponent() != opponent) {
                        player.getActionSender().sendMessage("That is not your opponent.");
                        return true;
                    }
                    if (!duelSession.hasDuelStarted()) {
                        player.getActionSender().sendMessage("The duel hasn't started yet!");
                        player.getWalkingQueue().reset();
                        return true;
                    }
                    player.getPulseManager().clear("interaction:attack:" + target.hashCode());
                    player.getProperties().getCombatPulse().attack(target);
                    return true;
                case "challenge":
                    return false;
                case "trade":
                    if (DuelSession.getExtension(player).getDuelStage() == DuelStage.IN_PROGRESS) {
                        player.getActionSender().sendMessage("You cannot trade while in a duel!");
                        return true;
                    }
                    duelSession = DuelSession.getExtension(opponent);
                    if (duelSession != null) {
                        if (duelSession.getDuelStage() == DuelStage.IN_PROGRESS) {
                            player.getActionSender().sendMessage("You cannot trade that player while they are in a duel!");
                            return true;
                        }
                    }
                    return true;
            }
        }
        // TODO Forfeit object
        return false;
    }

    @Override
    public void configure() {
        setZoneType(ZoneType.SAFE.getId());
        Option._P_CHALLENGE.setHandler(new OptionHandler() {

            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                return this;
            }

            @Override
            public boolean handle(Player player, Node node, String option) {
                Player other = (Player) node;
                if (other.getInterfaceState().isOpened() || other.getExtension(DuelSession.class) != null) {
                    player.getActionSender().sendMessage("Other player is busy at the moment.");
                    return true;
                }
                player.getRequestManager().request(other, DUEL_REQUEST);
                player.setAttribute("duel:partner", other);
                player.getConfigManager().set(283, 1 << 26); // TODO 283
                return true;
            }
        });
        PluginManager.definePlugin(new DuelSession(null, null));
        register(new ZoneBorders(3325, 3201, 3396, 3280));
    }

    @Override
    public boolean teleport(Entity e, int type, Node node) {
        if (!(e instanceof Player)) {
            return true;
        }
        Player player = (Player) e;
        if (DuelSession.getExtension(player) == null) {
            return true;
        }
        //player.getActionSender().sendMessage("You cannot teleport away from a duel!");
        return false;
    }
}
