package plugin.activity.clanwars;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents a rule within the {@link plugin.activity.clanwars.ClanWarsActivityPlugin}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum ClanWarsRule {
    /**
     * Game end rule.
     */
    LAST_TEAM_STANDING(24135, 800, 1, ClanWarsRuleCategory.GAME, "The battle ends when all members of a team have been<br>defeated. Fighters may not join or re-join the battle<br>after it has begun"),
    // TODO Messages for First to x kills and Most kills
    KILLS_25(24137, 800, 2, 25, ClanWarsRuleCategory.GAME, "First to 25 kills: The first team to score 25 kills will win.<br>Fighters may enter the battle at any time"),
    KILLS_50(24139, 800, 3, 50, ClanWarsRuleCategory.GAME, "First to 50 kills: The first team to score 50 kills will win.<br>Fighters may enter the battle at any time"),
    KILLS_100(24141, 800, 4, 100, ClanWarsRuleCategory.GAME, "First to 100 kills: The first team to score 100 kills will win.<br>Fighters may enter the battle at any time"),
    KILLS_200(24143, 800, 5, 200, ClanWarsRuleCategory.GAME, "First to 200 kills: The first team to score 200 kills will win.<br>Fighters may enter the battle at any time"),
    KILLS_500(24145, 800, 6, 500, ClanWarsRuleCategory.GAME, "First to 500 kills: The first team to score 500 kills will win.<br>Fighters may enter the battle at any time"),
    KILLS_1000(24147, 800, 7, 1000, ClanWarsRuleCategory.GAME, "First to 1,000 kills: The first team to score 1,000 kills will win.<br>Fighters may enter the battle at any time"),
    KILLS_5000(24149, 800, 8, 5000, ClanWarsRuleCategory.GAME, "First to 5,000 kills: The first team to score 5,000 kills will win.<br>Fighters may enter the battle at any time"),
    KILLS_10000(24151, 800, 9, 10000, ClanWarsRuleCategory.GAME, "First to 10,000 kills: The first team to score 10,000 kills will win.<br>Fighters may enter the battle at any time"),
    MOST_KILLS_5_MINS(24153, 800, 10, 5, ClanWarsRuleCategory.GAME, "Most kills: The team with the most kills for 5 minutes will win.<br>Fighters may enter the battle at any time"),
    MOST_KILLS_10_MINS(24155, 800, 11, 10, ClanWarsRuleCategory.GAME, "Most kills: The team with the most kills for 10 minutes will win.<br>Fighters may enter the battle at any time"),
    MOST_KILLS_20_MINS(24157, 800, 12, 20, ClanWarsRuleCategory.GAME, "Most kills: The team with the most kills for 20 minutes will win.<br>Fighters may enter the battle at any time"),
    MOST_KILLS_60_MINS(24159, 800, 13, 60, ClanWarsRuleCategory.GAME, "Most kills: The team with the most kills for 60 minutes will win.<br>Fighters may enter the battle at any time"),
    MOST_KILLS_120_MINS(24161, 800, 14, 120, ClanWarsRuleCategory.GAME, "Most kills: The team with the most kills for 120 minutes will win.<br>Fighters may enter the battle at any time"),
    ODDSKULL_100(24163, 800, 15, 100, ClanWarsRuleCategory.GAME, "Oddskull: The first team to hold the skull for 100 points<br>will win"),
    ODDSKULL_300(24165, 800, 16, 300, ClanWarsRuleCategory.GAME, "Oddskull: The first team to hold the skull for 300 points<br>will win"),
    ODDSKULL_500(24167, 800, 17, 500, ClanWarsRuleCategory.GAME, "Oddskull: The first team to hold the skull for 500 points<br>will win"),
    /**
     * Arena rule.
     */
    WASTELAND(24171, 801, 1, ClanWarsRuleCategory.ARENA, null),
    PLATEAU(24173, 801, 2, ClanWarsRuleCategory.ARENA, null),
    SYLVAN_GLADE(24175, 801, 3, ClanWarsRuleCategory.ARENA, null),
    FORSAKEN_QUARRY(24177, 801, 4, ClanWarsRuleCategory.ARENA, null),
    TURRETS(24179, 801, 5, ClanWarsRuleCategory.ARENA, null) {
        @Override
        public boolean canActivate(Player player, boolean message) {
            player.getActionSender().sendMessage("Coming soon!");
            return false;
        }
    },
    GHASTLY_SWAMP(24181, 801, 6, ClanWarsRuleCategory.ARENA, null) {
        @Override
        public boolean canActivate(Player player, boolean message) {
            player.getActionSender().sendMessage("Coming soon!");
            return false;
        }
    },
    NORTHLEACH_QUELL(24183, 801, 7, ClanWarsRuleCategory.ARENA, null) {
        @Override
        public boolean canActivate(Player player, boolean message) {
            player.getActionSender().sendMessage("Coming soon!");
            return false;
        }
    },
    GRIDLOCK(24185, 801, 8, ClanWarsRuleCategory.ARENA, null) {
        @Override
        public boolean canActivate(Player player, boolean message) {
            player.getActionSender().sendMessage("Coming soon!");
            return false;
        }
    },
    ETHEREAL(24187, 801, 9, ClanWarsRuleCategory.ARENA, null) {
        @Override
        public boolean canActivate(Player player, boolean message) {
            player.getActionSender().sendMessage("Coming soon!");
            return false;
        }
    },
    CLAN_CUP_AREA(24189, 801, 10, ClanWarsRuleCategory.ARENA, null) {
        @Override
        public boolean canActivate(Player player, boolean message) {
            player.getActionSender().sendMessage("Coming soon!");
            return false;
        }
    },
    /**
     * Miscellaneous rules.
     */
    IGNORE_FREEZING(24192, 802, -2, ClanWarsRuleCategory.OTHER_SETTINGS, "Spells such as Bind and Ice Barrage will not prevent<br>their targets from moving. Their damage will be applied<br>normally"),
    PJ_TIMER(24194, 803, -2, ClanWarsRuleCategory.OTHER_SETTINGS, "In single-way combat areas, players are protected<br>from being attacked for 10 secs after they have been<br>attacking someone else"),
    SINGLE_SPELLS(24196, 804, -2, ClanWarsRuleCategory.OTHER_SETTINGS, "Multi-target attacks, including chinchompas, will hit<br>only one target, even in multi-way combat areas"),
    ALLOW_TRIDENT_IN_PVP(24198, 805, -2, ClanWarsRuleCategory.OTHER_SETTINGS, "The trident of the Seas can cast its spell against<br>players") {
        @Override
        public boolean canActivate(Player player, boolean message) {
            player.getActionSender().sendMessage("Coming soon!");
            return false;
        }
    },
    /**
     * Melee rule.
     */
    MELEE_ALLOWED(24200, 806, 1, ClanWarsRuleCategory.COMBAT, ""),
    MELEE_DISABLED(24202, 806, 2, ClanWarsRuleCategory.COMBAT, ""),
    /**
     * Ranging rule.
     */
    RANGING_ALLOWED(24204, 807, 1, ClanWarsRuleCategory.COMBAT, ""),
    RANGING_DISABLED(24206, 807, 2, ClanWarsRuleCategory.COMBAT, ""),
    /**
     * Food rule.
     */
    FOOD_ALLOWED(24208, 808, 1, ClanWarsRuleCategory.COMBAT, "Food may be eaten during the battle"),
    FOOD_DISABLED(24210, 808, 2, ClanWarsRuleCategory.COMBAT, "No food may be eaten during the battle"),
    /**
     * Drinks rule.
     */
    DRINKS_ALLOWED(24212, 809, 1, ClanWarsRuleCategory.COMBAT, "Drinks, such as potions, may be used during the<br>battle"),
    DRINKS_DISABLED(24214, 809, 2, ClanWarsRuleCategory.COMBAT, "No drinks may be consumed during the battle"),
    /**
     * Special attacks rule.
     */
    SPECIAL_ATTACKS_ALLOWED(24216, 810, 1, ClanWarsRuleCategory.SPECIAL_ATTACKS, "Special attacks are allowed"),
    SPECIAL_ATTACKS_NO_SOTD(24218, 810, 2, ClanWarsRuleCategory.SPECIAL_ATTACKS, "") {
        @Override
        public boolean canActivate(Player player, boolean message) {
            player.getActionSender().sendMessage("Coming soon!");
            return false;
        }
    },
    SPECIAL_ATTACKS_DISABLED(24220, 810, 3, ClanWarsRuleCategory.SPECIAL_ATTACKS, "Special attacks are forbidden"),
    /**
     * Stragglers rule.
     */
    KILL_EM_ALL(24222, 811, 1, ClanWarsRuleCategory.OTHER_SETTINGS, null),
    IGNORE_FIVE(24224, 811, 2, ClanWarsRuleCategory.OTHER_SETTINGS, "If a team has fewer than 5 fighters in the arena, that<br>team will lose immediately"),
    /**
     * Magic rule.
     */
    MAGIC_ALL_SPELLBOOKS(24226, 812, 1, ClanWarsRuleCategory.COMBAT, "All spellbooks are allowed"),
    MAGIC_STANDARD_SPELLS(24228, 812, 2, ClanWarsRuleCategory.COMBAT, "Only the standard spellbook is allowed "),
    MAGIC_BINDING_ONLY(24230, 812, 3, ClanWarsRuleCategory.COMBAT, "Only the Bind, Snare and Entangle spells are<br>allowed"),
    MAGIC_DISABLED(24232, 812, 4, ClanWarsRuleCategory.COMBAT, "No magical combat is allowed"),
    /**
     * Prayer rule.
     */
    PRAYER_ALLOWED(24234, 813, 1, ClanWarsRuleCategory.COMBAT, "All prayers are allowed"),
    PRAYER_NO_OVERHEADS(24236, 813, 2, ClanWarsRuleCategory.COMBAT, "Prayers that use an overhead icon are<br>forbidden. Other prayers are allowed"),
    PRAYER_DISABLED(24238, 813, 3, ClanWarsRuleCategory.COMBAT, "No prayers are allowed");

    /**
     * The id of the button.
     */
    private final int buttonId;

    /**
     * The configuration id.
     */
    private final int configId;

    /**
     * The configuration value.
     */
    private final int configValue;

    /**
     * The {@link plugin.activity.clanwars.ClanWarsRuleCategory}.
     */
    private final ClanWarsRuleCategory clanWarsRuleCategory;

    /**
     * The text.
     */
    private final String text;

    /**
     * The amount of kills / points required in the war.
     */
    private int pointsRequired = -1;

    /**
     * Constructs a new <code>ClanWarsRule</code>.
     *
     * @param buttonId             The id of the button.
     * @param configId             The configuration id.
     * @param configValue          The configuration value.
     * @param pointsRequired       The amount of kills / points required in the war.
     * @param clanWarsRuleCategory The {@link plugin.activity.clanwars.ClanWarsRuleCategory}.
     * @param text                 The text.
     */
    ClanWarsRule(int buttonId, int configId, int configValue, int pointsRequired, ClanWarsRuleCategory clanWarsRuleCategory, String text) {
        this.buttonId = buttonId;
        this.configId = configId;
        this.configValue = configValue;
        this.pointsRequired = pointsRequired;
        this.clanWarsRuleCategory = clanWarsRuleCategory;
        this.text = text;
    }

    /**
     * Constructs a new <code>ClanWarsRule</code>.
     *
     * @param buttonId             The id of the button.
     * @param configId             The configuration id.
     * @param configValue          The configuration value.
     * @param clanWarsRuleCategory The {@link plugin.activity.clanwars.ClanWarsRuleCategory}.
     * @param text                 The text.
     */
    ClanWarsRule(int buttonId, int configId, int configValue, ClanWarsRuleCategory clanWarsRuleCategory, String text) {
        this(buttonId, configId, configValue, -1, clanWarsRuleCategory, text);
    }

    /**
     * The action upon activation.
     *
     * @param player  The player.
     * @param message If the message should be sent.
     */
    public void toggle(Player player, boolean message) {
        if (!canActivate(player, true)) {
            return;
        }
        WarSession warSession = player.getExtension(WarSession.class);
        if (warSession == null || warSession.getOpponent() == null || warSession.getClanWarsRules() == null) {
            return;
        }
        WarSession opponentSession = warSession.getOpponent().getExtension(WarSession.class);
        if (opponentSession == null) {
            return;
        }
        if (warSession.getClanWarsRules().contains(this)) {
            return;
        }
        for (ClanWarsRule clanWarsRule : warSession.getClanWarsRules()) {
            if (clanWarsRule.getConfigId() == getConfigId()) {
                warSession.getClanWarsRules().remove(clanWarsRule);
                opponentSession.getClanWarsRules().remove(clanWarsRule);
                break;
            }
        }

        int currentValue = player.getInterfaceState().get(getConfigId());
        int configValue = getConfigValue() == -2 ? (currentValue == 0 ? 1 : 0) : getConfigValue();
        player.getInterfaceState().force(getConfigId(), configValue, false);
        warSession.getOpponent().getInterfaceState().force(getConfigId(), configValue, false);
        warSession.getClanWarsRules().add(this);
        opponentSession.getClanWarsRules().add(this);
        if (message) {
            if (MELEE_DISABLED.isActivated(player) && RANGING_DISABLED.isActivated(player) && MAGIC_DISABLED.isActivated(player)) {
                if (warSession.getAcceptPulse() != null && warSession.getAcceptPulse().isRunning()) {
                    warSession.getAcceptPulse().stop();
                }
                toggleAccept(player, "<col=FF0000>If you turn off all forms of<br><col=FF0000>combat, your battle will be<br><col=FF0000>mighty peculiar");
                toggleAccept(warSession.getOpponent(), "<col=FF0000>If you turn off all forms of<br><col=FF0000>combat, your battle will be<br><col=FF0000>mighty peculiar");
            } else {
                if (opponentSession.getAcceptPulse() == null || !opponentSession.getAcceptPulse().isRunning()) {
                    toggleAccept(player, null);
                }
                if (warSession.getAcceptPulse() == null || !warSession.getAcceptPulse().isRunning()) {
                    toggleAccept(warSession.getOpponent(), "Your opponent has made<br>changes!<br>5");
                    warSession.setAcceptPulse(new Pulse(0, player, warSession.getOpponent()) {

                        int timer = 5;

                        @Override
                        public boolean pulse() {
                            if (timer == 4) {
                                this.setDelay(2);
                            }
                            toggleAccept(warSession.getOpponent(), "Your opponent has made<br>changes!<br>" + timer);
                            if (timer == 0) {
                                toggleAccept(warSession.getOpponent(), null);
                                warSession.setAcceptPulse(null);
                                return true;
                            }
                            timer--;
                            return false;
                        }
                    });
                    warSession.getOpponent().getPulseManager().run(warSession.getAcceptPulse());
                }
            }
        }
    }

    /**
     * If this rule can be activated.
     *
     * @param player  The player.
     * @param message If the message should be sent.
     * @return <code>True</code> if the message should be sent.
     */
    public boolean canActivate(Player player, boolean message) {
        return true;
    }

    /**
     * Whether or not this rule is activated.
     *
     * @param player The player.
     * @return <code>True</code> if so.
     */
    public boolean isActivated(Player player) {
        WarSession warSession = player.getExtension(WarSession.class);
        return warSession != null && warSession.getClanWarsRules().contains(this);
    }

    /**
     * Enables or disables the accept button depending on if there is an error message.
     *
     * @param message The message.
     */
    public void toggleAccept(Player player, String message) {
        player.getActionSender().sendInterfaceConfig(message == null ? 51 : 50, 24239);
        player.getActionSender().sendInterfaceConfig(message == null ? 51 : 50, 24240);
        player.getActionSender().sendInterfaceConfig(message == null ? 50 : 51, 24241);
        player.getActionSender().sendString(24241, message);
        player.getInterfaceState().force(814, 0, false);
        player.getActionSender().sendHideComponent(24239, false);
    }

    /**
     * Gets the id of the button.
     *
     * @return The button id.
     */
    public int getButtonId() {
        return buttonId;
    }

    /**
     * Gets the configuration id.
     *
     * @return The config id.
     */
    public int getConfigId() {
        return configId;
    }

    /**
     * Gets the configuration value.
     *
     * @return The config value.
     */
    public int getConfigValue() {
        return configValue;
    }

    /**
     * Gets the {@link plugin.activity.clanwars.ClanWarsRuleCategory}.
     *
     * @return The category.
     */
    public ClanWarsRuleCategory getClanWarsRuleCategory() {
        return clanWarsRuleCategory;
    }

    /**
     * Gets the text.
     *
     * @return The text.
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the amount of kills / points required in the war.
     *
     * @return The points required.
     */
    public int getPointsRequired() {
        return pointsRequired;
    }

    /**
     * Gets a <code>ClanWarsRule</code> by the id of the button.
     *
     * @param buttonId The id of the button.
     * @return The clan wars rule.
     */
    public static ClanWarsRule forId(int buttonId) {
        for (ClanWarsRule clanWarsRule : values()) {
            if (clanWarsRule.getButtonId() == buttonId) {
                return clanWarsRule;
            }
        }
        return null;
    }

}
