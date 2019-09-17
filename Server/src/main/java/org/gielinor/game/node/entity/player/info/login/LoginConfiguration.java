package org.gielinor.game.node.entity.player.info.login;

import org.gielinor.content.donators.DonatorConfigurations;
import org.gielinor.content.periodicity.util.PeriodicityPulseLoginMessagesKt;
import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.impl.YellCommand;
import org.gielinor.game.system.communication.ClanCommunication;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.game.world.update.UpdateSequence;
import org.gielinor.game.world.update.flag.player.AppearanceFlag;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.CameraContext;
import org.gielinor.net.packet.context.CameraContext.CameraType;
import org.gielinor.net.packet.context.PlayerDetailContext;
import org.gielinor.net.packet.context.XPDropContext;
import org.gielinor.net.packet.out.CameraViewPacket;
import org.gielinor.net.packet.out.PlayerDetail;
import org.gielinor.net.packet.out.XPDrop;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.interaction.inter.QuestTabInterface;
import plugin.interaction.item.MaxCapePlugin;
import plugin.interaction.item.RunePouchPlugin;

import java.util.ArrayList;
import java.util.List;

import static org.gielinor.constants.AbyssalBludgeonAttributeConstants.BLUDGEON_SPECIAL_MULTIPLIER;

/**
 * Sends the login configuration packets.
 *
 * @author Emperor
 */
public final class LoginConfiguration {

    private static final Logger log = LoggerFactory.getLogger(LoginConfiguration.class);

    /**
     * Represents the quest point items to remove.
     */
    private static final Item[] QUEST_ITEMS = new Item[]{ new Item(9813), new Item(9814) };

    /**
     * Represents the achievement point items to remove.
     */
    private static final Item[] ACHIEVEMENT_ITEMS = new Item[]{ new Item(Item.ACHIEVEMENT_DIARY_CAPE_T),
        new Item(Item.ACHIEVEMENT_DIARY_HOOD) };

    /**
     * The plugins
     */
    private static List<Plugin<Object>> loginPlugins = new ArrayList<>();

    /**
     * Configures the lobby login.
     *
     * @param player
     *            The player.
     */
    public static void configureLobby(Player player) {
        player.updateSceneGraph(true); // TODO Reset camera = 107
        // if (!player.isArtificial() && player.getAttribute("login_type",
        // LoginType.NORMAL_LOGIN) != LoginType.RECONNECT_TYPE) {
        // sendLoginInterface(player);
        // configureGameWorld(player);
        // } else {
        player.getDetails().setLastLogin(System.currentTimeMillis());
        String lastLogin = player.getSession().getRemoteAddress();
        if (lastLogin.startsWith("/")) {
            lastLogin = player.getSession().getRemoteAddress().replaceFirst("/", "");
        }
        if (lastLogin.contains(":")) {
            lastLogin = lastLogin.substring(0, lastLogin.indexOf(":"));
        }
        if (!player.getDetails().isInvalidLogin()) {
            player.getDetails().setLastIp(lastLogin);
            player.getDetails().setIp(lastLogin);
        }
        configureGameWorld(player);
        // }
    }

    /**
     * Configures the game world.
     *
     * @param player
     *            The player.
     */
    public static void configureGameWorld(final Player player) {
        player.getConfigManager().reset();
        player.getInterfaceState().resetConfigurations();
        player.getSettings().setFrameMode(0);
        sendGameConfiguration(player);
        Repository.getLobbyPlayers().remove(player);
        player.setPlaying(true);
        UpdateSequence.getRenderablePlayers().add(player);
        RegionManager.move(player);
        player.getMusicPlayer().init();
        player.getUpdateMasks().register(new AppearanceFlag(player));
        player.getPlayerFlags().setUpdateSceneGraph(true);
        player.getStateManager().init();
        player.getSavedData().getGlobalData().setStartTime();
        player.getLoyaltyPointMonitor().sendValues();
        PeriodicityPulseLoginMessagesKt.sendMessages(player);
        if (player.getDonorManager().hasMembership()) {
            DonatorConfigurations.loginConfigurations(player);
        }
    }

    /**
     * Sends the game configuration packets.
     *
     * @param player
     *            The player to send to.
     */
    public static void sendGameConfiguration(final Player player) {
        player.getInterfaceState().openDefaultTabs();
        welcome(player);
        config(player);
        conditions(player);
        player.getCommunication().sync(false);
        World.submit(new Pulse(3) {

            @Override
            public boolean pulse() {
                joinClanChat(player);
                return true;
            }
        });
    }

    /**
     * Method used to welcome the player.
     *
     * @param player
     *            the player.
     */
    public static void welcome(final Player player) {
        PacketRepository.send(PlayerDetail.class, new PlayerDetailContext(player, true, player.getIndex()));
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.RESET));
        player.getActionSender().sendMessages("Welcome to " + Constants.SERVER_NAME + ".");
        player.getSavedData().getGlobalData().getRunePouch().sendInventory(player, RunePouchPlugin.INVENTORY_COMPONENT);
        if (player.getReferralManager().getReferralRequests() > 0 && !player.getReferralManager().isReferred()) {
            player.getActionSender().sendMessage("<shad=1><col=9B701D>You have pending referral requests!");
        }
        player.getReferralManager().getReferrals().stream()
            .filter(referred -> referred.getReferredTime() > 0 && !referred.isNotified() && !referred.isRequested())
            .forEach(referred -> {
                player.getActionSender().sendMessage(
                    "<shad=1><col=9B701D>" + referred.getUsername() + " has accepted your referral!");
                referred.setNotified(true);
                player.getReferralManager().reward(false);
            });
        if (Constants.DISCORD_SERVER.length() > 0) {
            player.getActionSender().sendMessage("<col=7289DA><shad=1><a href=\"" + Constants.DISCORD_SERVER + "\">Join the official Discord server by clicking here!</a></shad></col>");
        }
        if (player.getSavedData().getGlobalData().getVotingPoints() > 50000) {
            player.getSavedData().getGlobalData().setVotingPoints(356);
        }
        Starter starter = new Starter(player);
        if (!starter.hasStarter()) {
            starter.start();
        }
        if (player.getDetails().getPortal().getMute().isPunished()) {
            player.getDetails().getPortal().getMute().inflict(player);
        }
        if (player.getDetails().getUnreadMessages() > 0) {
            player.getActionSender().sendMessage("You have <col=ff0000>" + player.getDetails().getUnreadMessages() + "</col> unread message(s), click <col=8B0000><a href=\"" + Constants.FORUMS_URL + "index.php?/messenger/\">here</a></col> to go to your message centre!");
        }
        if (player.getDetails().getEmail() == null || player.getDetails().getEmail().trim().length() == 0) {
            player.getActionSender().sendMessage("You have not set an email address! <col=9B701D><a href=\"" + Constants.FORUMS_URL + "\">Set one here!</a></col>");
        } else if (!player.getDetails().getEmailVerified()) {
            player.getActionSender().sendMessage("Your email is not verified! <col=9B701D><a href=\"" + Constants.FORUMS_URL + "\">Verify it here!</a></col>");
        }
    }

    /**
     * Rejoins the last clan chat the player was in before they logged out.
     *
     * @param player
     *            The player.
     */
    public static void joinClanChat(Player player) {
        if (player.getSavedData().getGlobalData().getLastClanChat() != null) {
            ClanCommunication clanCommunication = player.getSavedData().getGlobalData().getLastClanChat()
                .equalsIgnoreCase("help") ? ClanCommunication.DEFAULT
                : ClanCommunication.get(player.getSavedData().getGlobalData().getLastClanChat());
            if (clanCommunication != null && !clanCommunication.getName().equals("Chat disabled")) {
                boolean entered = clanCommunication.enter(player);
                if (entered) {
                    player.getCommunication().setClan(clanCommunication);
                    player.getCommunication().getClan().update();
                    return;
                }
                player.getSavedData().getGlobalData().setLastClanChat(null);
            }
        }
    }

    /**
     * Method used to configure all possible settings for the player.
     *
     * @param player
     *            the player.
     */
    public static void config(final Player player) {
        player.getInventory().refresh();
        player.getEquipment().refresh();
        player.getSkills().refresh();
        player.getSkills().configure();
        player.getSettings().update();
        player.getInteraction().setDefault();
        player.getActionSender().sendRunEnergy();
        player.getEmotes().refreshListConfigs();
        PacketRepository.send(XPDrop.class,
            new XPDropContext(player, -1, player.getSavedData().getGlobalData().getXpDrops()));
        player.getFamiliarManager().login();
        player.getInterfaceState().openDefaultTabs();
        player.getActionSender().sendString(5067, "Friends List");
        player.getActionSender().sendString(2451, Constants.SERVER_NAME + ", always use the");
        if (player.getAttributes().containsKey("spell:swap")) {
            player.getSpellBookManager().setSpellBook(SpellBook.LUNAR);
        }
        player.getConfigManager().init();
        player.getInterfaceState().initConfigurations();
        player.getFamiliarManager().sendLeftClickOptions();
        player.getGrandExchange().init();
        player.getAntiMacroHandler().init();
        player.getQuestRepository().update(player);
        player.getAchievementRepository().update(player);
        player.getPrayer().reset();
        for (PrayerType prayerType : PrayerType.values()) {
            player.getConfigManager().set(prayerType.getConfig(), 0);
        }
        player.getDetails().setLastLogin(System.currentTimeMillis());
        String lastLogin = player.getSession().getRemoteAddress();
        if (lastLogin.startsWith("/")) {
            lastLogin = player.getSession().getRemoteAddress().replaceFirst("/", "");
        }
        if (lastLogin.contains(":")) {
            lastLogin = lastLogin.substring(0, lastLogin.indexOf(":"));
        }
        player.getDetails().setLastIp(lastLogin);
        player.getDetails().setIp(lastLogin);
        player.getFarmingManager().getSeedlingManager().parse();
        player.getInterfaceState().sendInterfaceConfigurations();
        player.getSettings().setDataOrbs(player.getSettings().isDataOrbs(), true);
        player.getSettings().setRemainingXP(player.getSettings().isRemainingXP(), true);
        player.getSettings().setRoofRemoval(player.getSettings().isRoofRemoval(), true);
        player.getSettings().setAreaSoundVolume(4);
        player.getConfigManager().force(168, 4, true);
        player.getConfigManager().force(169, 4, true);
        QuestTabInterface.sendDefaultInformation(player);
        if (player.getAttribute("stamina-time", 0) > 0) {
            player.getInterfaceState().force(InterfaceConfiguration.STAMINA_POTION, true, false);
            Pulse pulse;
            World.submit(pulse = new Pulse(1, player) {

                int count = player.getAttribute("stamina-time", 0);

                @Override
                public boolean pulse() {
                    count--;
                    if (count == 25) {
                        player.getActionSender().sendMessage("<col=8F4808>Your stamina potion is about to expire.");
                    }
                    if (count <= 0) {
                        player.getActionSender().sendMessage("<col=8F4808>Your stamina potion has expired.");
                        player.getInterfaceState().force(InterfaceConfiguration.STAMINA_POTION, false, false);
                        player.removeAttribute("stamina-time");
                        return true;
                    }
                    player.saveAttribute("stamina-time", count);
                    return false;
                }
            });
            player.setAttribute("stamina-potion", pulse);
        }
        if (World.getConfiguration().isBetaEnabled()) {
            player.getSavedData().getGlobalData().setBetaStatus(true);
        }
    }

    /**
     * Method used to check for all possible conditions on login.
     *
     * @param player
     *            the player.
     */
    public static void conditions(final Player player) {
        if (player.isArtificial()) {
            return;
        }
        if (player.getAttribute("barrelchest", false)) {
            ActivityManager.start(player, "barrelchest", true);
        }
        if (player.getAttribute("fc_wave", -1) > -1) {
            ActivityManager.start(player, "fight caves", true);
            player.getActionSender().sendMessage("<col=FF0000>Your wave will start in 10 seconds.");
        }
        if (player.getAttribute("falconry", false)) {
            ActivityManager.start(player, "falconry", true);
        }
        if (player.getLocation().getRegionId() == 9622) {
            player.getProperties().setTeleportLocation(Location.create(3564, 3288, 0));
        }
        if (player.getRights().getAnnouncementOnLogin()) {
            World.sendCustomWorldMessage(YellCommand.getTagColour(player) + player.getRights().toString() + " " + player.getUsername() + " has logged in!</shad></col>");
        }
        if (player.getAttribute(BLUDGEON_SPECIAL_MULTIPLIER) == null) {
            player.setAttribute(BLUDGEON_SPECIAL_MULTIPLIER, 1.0);
        }

        player.getConfigManager().set(177, 1967876); // Dragon slayer boat repaired
        player.getConfigManager().set(465, 11); // Dorgesh-kaan wall "Squeeze through"
        checkDiaryItems(player);

        for (Plugin<Object> plugin : loginPlugins) {
            try {
                plugin.newInstance(player);
            } catch (Throwable t) {
                log.error("Error loading log-in plugin for [{}].", player.getName(), t);
            }
        }
    }

    /**
     * Method used to check for the quest and achievement diary items.
     *
     * @param player
     *            the player.
     */
    private static void checkDiaryItems(final Player player) {
        if (!player.getAchievementRepository().hasCompletedAll() && player.getEquipment().contains(20116, 1)
            || player.getEquipment().contains(20117, 1)) {
            for (Item i : ACHIEVEMENT_ITEMS) {
                if (player.getEquipment().remove(i)) {
                    player.getDialogueInterpreter().sendItemMessage(i,
                        "As you no longer have completed all of the achievements,",
                        "your " + i.getName() + " unequips itself",
                        "to your " + (player.getInventory().freeSlots() < 1 ? "bank" : "inventory") + "!");
                    if (player.getInventory().freeSlots() < 1) {
                        player.getBank().add(i);
                    } else {
                        player.getInventory().add(i);
                    }
                }
            }
        }
        if (!player.getQuestRepository().hasCompletedAll() && player.getEquipment().contains(9813, 1)
            || player.getEquipment().contains(9814, 1)) {
            for (Item i : QUEST_ITEMS) {
                if (player.getEquipment().remove(i)) {
                    player.getDialogueInterpreter().sendItemMessage(i,
                        "As you no longer have completed all of the quests,",
                        "your " + i.getName() + " unequips itself",
                        "to your " + (player.getInventory().freeSlots() < 1 ? "bank" : "inventory") + "!");
                    if (player.getInventory().freeSlots() < 1) {
                        player.getBank().add(i);
                    } else {
                        player.getInventory().add(i);
                    }
                }
            }
        }
        MaxCapePlugin.hasRequirements(player, true);
    }

    /**
     * Gets the loginPlugins.
     *
     * @return The loginPlugins.
     */
    public static List<Plugin<Object>> getLoginPlugins() {
        return loginPlugins;
    }

    /**
     * Represents a weekly message.
     *
     * @author 'Vexia
     */
    public enum WeeklyMessage {

        MOVING_COGS(15488, 15491),
        QUESTION_MARKS(17511, 17513),
        DRAMA_FACE(15767, 15769),
        BANK_PIN_VAULT(15812, 15814),
        BANK_PIN_QUESTION_MARK(15791, 15793),
        PLAYER_SCAMMING(15801, 15803),
        BANK_PIN_KEY(15774, 15776),
        CHRISTMAS_PRESENT(15819, 15821);

        /**
         * Represents the child id.
         */
        private final int child;
        /**
         * The text child id.
         */
        private final int textId;
        /**
         * The message of the component.
         */
        private String[] message;

        /**
         * Constructs a new {@code WeeklyMessage} {@code Object}.
         *
         * @param child
         *            The id of the child.
         * @param message
         *            the message.
         */
        WeeklyMessage(int child, int textId, String... message) {
            this.child = child;
            this.textId = textId;
            this.message = message;
        }

        /**
         * Gets the message.
         *
         * @return The message.
         */
        public String[] getMessage() {
            if (message == null) {
                return new String[]{ "Welcome to " + Constants.SERVER_NAME + "." };
            }
            return message;
        }

        /**
         * Sets the message.
         *
         * @param message
         *            The message.
         */
        public void setMessage(String... message) {
            this.message = message;
        }

        /**
         * Gets the child.
         *
         * @return The child.
         */
        public int getChild() {
            return child;
        }

        /**
         * Gets the text child id.
         *
         * @return The text id.
         */
        public int getTextId() {
            return textId;
        }
    }

}
