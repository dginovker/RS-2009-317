package plugin.activity.clanwars;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.system.communication.ClanCommunication;
import org.gielinor.game.world.World;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents a {@link plugin.activity.clanwars.ClanWarsActivityPlugin} session.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class WarSession {

    /**
     * The default {@link plugin.activity.clanwars.ClanWarsRule} settings.
     */
    private final ClanWarsRule[] DEFAULT_RULES = new ClanWarsRule[]{
        ClanWarsRule.LAST_TEAM_STANDING, ClanWarsRule.WASTELAND, ClanWarsRule.MELEE_ALLOWED,
        ClanWarsRule.RANGING_ALLOWED, ClanWarsRule.FOOD_ALLOWED, ClanWarsRule.DRINKS_ALLOWED,
        ClanWarsRule.SPECIAL_ATTACKS_ALLOWED, ClanWarsRule.KILL_EM_ALL, ClanWarsRule.MAGIC_ALL_SPELLBOOKS,
        ClanWarsRule.PRAYER_ALLOWED
    };
    /**
     * The setup component.
     */
    private static final Component SETUP_COMPONENT = new Component(24126);

    /**
     * The confirmation component.
     */
    private static final Component CONFIRMATION_COMPONENT = new Component(24242).setCloseEvent(new ClanWarsCloseEvent());

    /**
     * The first player.
     */
    private final Player player;

    /**
     * The opponent player.
     */
    private final Player opponent;

    /**
     * The first clan.
     */
    private ClanCommunication firstClan;

    /**
     * The second clan.
     */
    private ClanCommunication secondClan;

    /**
     * The first clan's players.
     */
    private List<Player> firstClanPlayers = new ArrayList<>();

    /**
     * The second clan's players.
     */
    private List<Player> secondClanPlayers = new ArrayList<>();

    /**
     * The {@link java.util.List} of {@link plugin.activity.clanwars.ClanWarsRule} settings.
     */
    private final List<ClanWarsRule> clanWarsRules = new ArrayList<>();

    /**
     * The {@link plugin.activity.clanwars.ChallengeStage}.
     */
    private ChallengeStage challengeStage;

    /**
     * The accept timeout {@link org.gielinor.rs2.pulse.Pulse}.
     */
    private Pulse acceptPulse;

    /**
     * Constructs a new <code>WarSession</code>.
     *
     * @param player   The first player.
     * @param opponent The second player.
     */
    public WarSession(Player player, Player opponent) {
        this.player = player;
        this.opponent = opponent;
    }

    /**
     * Opens the clan wars challenge setup.
     */
    public void openSetup() {
        player.getActionSender().sendString("Clan Wars Setup: Challenging " + opponent.getUsername(), 24131);
        player.getInterfaceState().closeDefaultTabs();
        resetRules();
        SETUP_COMPONENT.setCloseEvent(new ClanWarsCloseEvent());
        player.getInterfaceState().open(SETUP_COMPONENT);
    }

    /**
     * Resets the rules to their defaults.
     */
    public void resetRules() {
        player.getActionSender().sendInterfaceConfig(51, 24239);
        player.getActionSender().sendInterfaceConfig(51, 24240);
        player.getActionSender().sendInterfaceConfig(50, 24241);
        player.getActionSender().sendString(24241, null);
        clanWarsRules.clear();
        for (ClanWarsRule clanWarsRule : ClanWarsRule.values()) {
            player.getInterfaceState().force(clanWarsRule.getConfigId(), 0, false);
        }
        for (ClanWarsRule clanWarsRule : DEFAULT_RULES) {
            player.getInterfaceState().force(clanWarsRule.getConfigId(), clanWarsRule.getConfigValue(), false);
            clanWarsRules.add(clanWarsRule);
        }
        player.getInterfaceState().force(814, 0, false);
        player.getActionSender().sendHideComponent(24239, false);
        player.getInterfaceState().force(815, 0, false);
        player.getInterfaceState().force(50, 24250, false);
        player.getInterfaceState().force(50, 24251, false);
        player.getInterfaceState().force(51, 24252, false);
    }

    /**
     * Accepts the setup.
     */
    public void acceptSetup() {
        setChallengeStage(ChallengeStage.ACCEPT_WAITING);
        WarSession opponentSession = getExtension(opponent);
        if (opponentSession != null && opponentSession.getChallengeStage() == ChallengeStage.ACCEPT_WAITING) {
            SETUP_COMPONENT.setCloseEvent(null);
            openConfirm();
            opponentSession.openConfirm();
            return;
        }
        player.getActionSender().sendHideComponent(24239, true);
        player.getInterfaceState().force(814, 1, false);
    }

    /**
     * Opens the confirmation screen, displaying the setup.
     */
    public void openConfirm() {
        player.getActionSender().sendString(24247, "Clan Wars Setup: Challenging " + opponent.getUsername());
        player.getActionSender().sendString(24249, "");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<col=FFFFFF><u=134217720>The Game:<br><br>");
        ClanWarsRule clanWarsRule = getClanWarsRule(ClanWarsRuleCategory.GAME);
        stringBuilder.append(clanWarsRule.getText()).append(".<br><br>");

        stringBuilder.append("<col=FFFFFF><u=134217720>The Arena:</col></u> ");
        clanWarsRule = getClanWarsRule(ClanWarsRuleCategory.ARENA);
        stringBuilder.append(TextUtils.formatDisplayName(clanWarsRule.name())).append("<br><br>");

        stringBuilder.append("<col=FFFFFF><u=134217720>The Combat:<br><br>");
        stringBuilder.append("<col=FFFFFF>Melee: </col>");
        stringBuilder.append("Melee combat is ").append(ClanWarsRule.MELEE_ALLOWED.isActivated(player) ? "allowed" : "disabled").append(".<br>");
        stringBuilder.append("<col=FFFFFF>Ranging: </col>");
        stringBuilder.append("Ranging is ").append(ClanWarsRule.RANGING_ALLOWED.isActivated(player) ? "allowed" : "disabled").append(".<br>");
        stringBuilder.append("<col=FFFFFF>Magic: </col>");
        clanWarsRule = getActiveClanWarsRule(ClanWarsRule.MAGIC_ALL_SPELLBOOKS, ClanWarsRule.MAGIC_STANDARD_SPELLS, ClanWarsRule.MAGIC_BINDING_ONLY, ClanWarsRule.MAGIC_DISABLED);
        stringBuilder.append(clanWarsRule.getText()).append(".<br><br>");
        stringBuilder.append("<col=FFFFFF>Prayer: </col>");
        clanWarsRule = getActiveClanWarsRule(ClanWarsRule.PRAYER_ALLOWED, ClanWarsRule.PRAYER_NO_OVERHEADS, ClanWarsRule.PRAYER_DISABLED);
        stringBuilder.append(clanWarsRule.getText()).append(".<br><br>");

        stringBuilder.append("<col=FFFFFF>Food: </col>");
        clanWarsRule = getActiveClanWarsRule(ClanWarsRule.FOOD_ALLOWED, ClanWarsRule.FOOD_DISABLED);
        stringBuilder.append(clanWarsRule.getText()).append(".<br>");

        stringBuilder.append("<col=FFFFFF>Drinks: </col>");
        clanWarsRule = getActiveClanWarsRule(ClanWarsRule.DRINKS_ALLOWED, ClanWarsRule.DRINKS_DISABLED);
        stringBuilder.append(clanWarsRule.getText()).append(".<br><br>");

        stringBuilder.append("<col=FFFFFF>Special attacks: </col>");
        clanWarsRule = getActiveClanWarsRule(ClanWarsRule.SPECIAL_ATTACKS_ALLOWED, ClanWarsRule.SPECIAL_ATTACKS_NO_SOTD, ClanWarsRule.SPECIAL_ATTACKS_DISABLED);
        stringBuilder.append(clanWarsRule.getText()).append(".<br><br>");

        boolean otherSettings = false;
        ClanWarsRule[] clanWarsRules1 = new ClanWarsRule[]{ ClanWarsRule.IGNORE_FREEZING, ClanWarsRule.PJ_TIMER, ClanWarsRule.SINGLE_SPELLS, ClanWarsRule.ALLOW_TRIDENT_IN_PVP };
        for (ClanWarsRule clanWarsRule1 : clanWarsRules1) {
            if (clanWarsRule1.isActivated(player)) {
                otherSettings = true;
                break;
            }
        }
        if (otherSettings) {
            stringBuilder.append("<col=FFFFFF><u=134217720>Other settings:<br><br>");
            for (ClanWarsRule clanWarsRule1 : clanWarsRules1) {
                if (clanWarsRule1.isActivated(player)) {
                    stringBuilder.append(clanWarsRule1.getText()).append(".<br><br>");
                }
            }
        }
        player.getActionSender().sendString(24249, stringBuilder.toString());
        setChallengeStage(ChallengeStage.CONFIRM);
        player.getInterfaceState().force(815, 0, false);
        player.getInterfaceState().open(CONFIRMATION_COMPONENT);
        player.getInterfaceState().force(50, 24250, false);
        player.getInterfaceState().force(50, 24251, false);
        player.getInterfaceState().force(51, 24252, false);
        World.submit(new Pulse(10) {

            @Override
            public boolean pulse() {
                player.getInterfaceState().force(51, 24250, false);
                player.getInterfaceState().force(51, 24251, false);
                player.getInterfaceState().force(50, 24252, false);
                return true;
            }
        });
    }

    /**
     * Confirms the setup.
     */
    public void confirmSetup() {
        setChallengeStage(ChallengeStage.CONFIRM_WAITING);
        WarSession opponentSession = getExtension(opponent);
        if (opponentSession != null && opponentSession.getChallengeStage() == ChallengeStage.CONFIRM_WAITING) {
            CONFIRMATION_COMPONENT.setCloseEvent(null);
            return;
        }
        player.getActionSender().sendHideComponent(24250, true);
        player.getInterfaceState().force(815, 1, false);
    }

    /**
     * Declines the challenge.
     *
     * @param player The player declining.
     */
    public static void decline(Player player) {
        WarSession warSession = player.getExtension(WarSession.class);
        if (warSession == null) {
            return;
        }
        // TODO FORFEIT
//        if (warSession.getWarStage() == WarStage.IN_PROGRESS ||
//                WarSession.getExtension(warSession.getOpponent()).getWarStage() == WarStage.IN_PROGRESS) {
//            forfeit(player);
//            return;
//        }
        warSession.player.removeExtension(WarSession.class);
        warSession.opponent.removeExtension(WarSession.class);
        player.getInterfaceState().closeSingleTab();
        player.getInterfaceState().openDefaultTabs();
        warSession.opponent.getInterfaceState().closeSingleTab();
        warSession.opponent.getInterfaceState().openDefaultTabs();
        if (player == warSession.opponent) {
            warSession.player.getInterfaceState().close();
            warSession.player.getActionSender().sendMessage(warSession.opponent.getUsername() + " has cancelled the challenge.");
        } else {
            warSession.opponent.getInterfaceState().close();
            warSession.opponent.getActionSender().sendMessage(warSession.player.getUsername() + " has cancelled the challenge.");
        }
        player.getInterfaceState().close();
        warSession.opponent.getInterfaceState().close();
        player.getActionSender().sendMessage("You have cancelled the challenge.");
    }

    /**
     * Gets the current player.
     *
     * @return The {@link #player}.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the current opponent.
     *
     * @return The {@link #opponent}.
     */
    public Player getOpponent() {
        return opponent;
    }

    /**
     * Gets the {@link java.util.List} of {@link plugin.activity.clanwars.ClanWarsRule} settings.
     *
     * @return The settings.
     */
    public List<ClanWarsRule> getClanWarsRules() {
        return clanWarsRules;
    }

    /**
     * Gets a <code>ClanWarsRule</code> by the category.
     *
     * @param clanWarsRuleCategory The category.
     */
    public ClanWarsRule getClanWarsRule(ClanWarsRuleCategory clanWarsRuleCategory) {
        for (ClanWarsRule clanWarsRule : clanWarsRules) {
            if (clanWarsRule.getClanWarsRuleCategory() == clanWarsRuleCategory) {
                return clanWarsRule;
            }
        }
        return null;
    }

    /**
     * Gets the active <code>ClanWarsRule</code> from an array.
     *
     * @param clanWarsRules The rules to check.
     */
    public ClanWarsRule getActiveClanWarsRule(ClanWarsRule... clanWarsRules) {
        for (ClanWarsRule clanWarsRule : clanWarsRules) {
            if (clanWarsRule.isActivated(player)) {
                return clanWarsRule;
            }
        }
        return null;
    }

    /**
     * Gets the {@link plugin.activity.clanwars.ChallengeStage}.
     *
     * @return The challenge stage.
     */
    public ChallengeStage getChallengeStage() {
        return challengeStage;
    }

    /**
     * Sets the {@link plugin.activity.clanwars.ChallengeStage}.
     *
     * @param challengeStage The challenge stage.
     */
    public void setChallengeStage(ChallengeStage challengeStage) {
        this.challengeStage = challengeStage;
    }

    /**
     * Gets the accept timeout {@link org.gielinor.rs2.pulse.Pulse}.
     *
     * @return The pulse.
     */
    public Pulse getAcceptPulse() {
        return acceptPulse;
    }

    /**
     * Sets the accept timeout {@link org.gielinor.rs2.pulse.Pulse}.
     *
     * @param acceptPulse The accept pulse.
     */
    public void setAcceptPulse(Pulse acceptPulse) {
        this.acceptPulse = acceptPulse;
    }

    /**
     * Gets an extension of this class for the player.
     *
     * @param player The player.
     * @return The session.
     */
    public static WarSession getExtension(final Player player) {
        return player.getExtension(WarSession.class);
    }

    /**
     * The {@link org.gielinor.game.component.CloseEvent} for declining a war challenge.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static class ClanWarsCloseEvent implements CloseEvent {

        @Override
        public void close(Player player, Component component) {
            final WarSession warSession = WarSession.getExtension(player);
            if (warSession == null) {
                return;
            }
            final WarSession opponentSession = warSession.getOpponent().getExtension(WarSession.class);
            if (warSession.getChallengeStage() == ChallengeStage.ACCEPT_WAITING &&
                opponentSession.getChallengeStage() == ChallengeStage.ACCEPT_WAITING) {
                return;
            }
            if (warSession.getChallengeStage() == ChallengeStage.IN_PROGRESS && opponentSession.getChallengeStage() == ChallengeStage.IN_PROGRESS) {
                return;
            }
            WarSession.decline(player);
        }

        @Override
        public boolean canClose(Player player, Component component) {
            return true;
        }
    }

}
