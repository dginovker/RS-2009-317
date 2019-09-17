package org.gielinor.game.system.communication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import bot.DiscordBot;
import bot.DiscordConstants;
import org.gielinor.database.DataSource;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.system.SystemManager;
import org.gielinor.game.world.World;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.mqueue.message.impl.ClanChatMessage;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketHeader;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ClanContext;
import org.gielinor.net.packet.context.MessageContext;
import org.gielinor.net.packet.out.CommunicationMessage;
import org.gielinor.net.packet.out.UpdateClanChat;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles clan communication.
 *
 * @author Emperor
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ClanCommunication {

    private static final Logger log = LoggerFactory.getLogger(ClanCommunication.class);

    /**
     * The default clan.
     */
    public static final ClanCommunication DEFAULT = new ClanCommunication(Constants.DEFAULT_CLAN_NAME, 0);

    /**
     * The maximum amount of members to be in a clan chat.
     */
    private static final int MAX_MEMBERS = 100;

    /**
     * The clan repository.
     */
    private static final Map<String, ClanCommunication> CLAN_REPOSITORY = new HashMap<>();

    /**
     * The name of the clan owner.
     */
    private final String owner;

    /**
     * The pidn of the clan owner.
     */
    private final int pidn;

    /**
     * The clan name.
     */
    private String name = "Chat disabled";

    /**
     * The rank required for joining.
     */
    private ClanRank joinRequirement = ClanRank.NOT_IN_CLAN;

    /**
     * The rank required for messaging.
     */
    private ClanRank messageRequirement = ClanRank.ANYONE;

    /**
     * The rank required for kicking members.
     */
    private ClanRank kickRequirement = ClanRank.OWNER;

    /**
     * The rank required for loot-share.
     */
    private ClanRank lootRequirement = ClanRank.NO_ONE;

    /**
     * The {@link java.util.List} of
     * {@link org.gielinor.game.system.communication.ClanMember}s.
     */
    private final List<ClanMember> clanMembers = new ArrayList<>();

    /**
     * The banned players.
     */
    private final Map<String, Long> banned = new HashMap<>();

    /**
     * The players who are currently in the friends chat.
     */
    private List<Player> players = new ArrayList<>(MAX_MEMBERS);

    /**
     * The current clan wars activity.
     */
    private ActivityPlugin clanWar;

    static {
        DEFAULT.name = Constants.SERVER_NAME;
        DEFAULT.joinRequirement = ClanRank.ANYONE;
        DEFAULT.messageRequirement = ClanRank.ANYONE;
        DEFAULT.kickRequirement = ClanRank.GENERAL;
        DEFAULT.lootRequirement = ClanRank.ANYONE;
    }

    /**
     * Constructs a new {@code ClanCommunication} {@code Object}.
     *
     * @param owner
     *            The owner of the clan.
     */
    public ClanCommunication(String owner, int pidn) {
        this.owner = owner.toLowerCase();
        this.pidn = pidn;
    }

    /**
     * Enters the clan chat.
     *
     * @param player
     *            The player.
     * @return <code>True</code> if the player successfully entered the clan
     *         chat.
     */
    public boolean enter(Player player) {
        player.getActionSender().sendMessage("Attempting to join channel...");
        if (this != DEFAULT && players.size() >= MAX_MEMBERS) {
            player.getActionSender().sendMessage("The channel you tried to join is full.");
            return false;
        }
        if (!player.getName().equals(owner) && !player.getDetails().getRights().isAdministrator()) {
            if (isBanned(player.getName())) {
                player.getActionSender().sendMessage("You are temporarily banned from this clan channel.");
                return false;
            }
            Player o = Repository.getPlayerByName(owner);
            if (o != null) {
                if (o.getCommunication().getBlocked().containsValue(player.getName())) {
                    player.getActionSender()
                        .sendMessage("You do not have a high enough rank to join this clan channel.");
                    return false;
                }
            }
            ClanRank rank = getRank(player);
            if (rank.ordinal() < joinRequirement.ordinal()) {
                player.getActionSender().sendMessage("You do not have a high enough rank to join this clan channel.");
                return false;
            }
        }
        if (!players.contains(player)) {
            players.add(player);
        }
        player.getActionSender().sendMessage("Now talking in clan channel " + name);
        player.getActionSender().sendMessage("To talk, start each line of chat with the / symbol.");
        player.getSavedData().getGlobalData().setLastClanChat(owner);
        update();
        final ClanCommunication clanCommunication = this;
        if (clanCommunication == DEFAULT) {
            World.submit(new Pulse(2) {

                @Override
                public boolean pulse() {
                    if (player.getAttribute("NEW_PLAYER_CLAN") != null) {
                        player.removeAttribute("NEW_PLAYER_CLAN");
                        if (!World.getConfiguration().isBetaEnabled()) {
                            ClanCommunication.forceMessage("Help",
                                "Player " + TextUtils.formatDisplayName(player.getUsername())
                                    + " has joined for the first time!",
                                1, clanCommunication);
                        }
                    }
                    return true;
                }
            });
        }
        return true;
    }

    /**
     * Cleans the chat from all players that shouldn't be in it.
     */
    public void clean(boolean disable) {
        for (Iterator<Player> it = players.iterator(); it.hasNext(); ) {
            Player player = it.next();
            boolean remove = disable;
            if (!remove) {
                remove = getRank(player).getSettingValue() < joinRequirement.getSettingValue(); // or
                // settingValue()?
            }
            if (remove) {
                leave(player, false, false);
                it.remove();
            }
        }
        if (players.isEmpty()) {
            banned.clear();
        }
        update();
    }

    /**
     * Checks if a player is banned.
     *
     * @param name
     *            The player's name.
     * @return <code>True</code> if so.
     */
    public boolean isBanned(String name) {
        if (banned.containsKey(name)) {
            long time = banned.get(name);
            if (time > System.currentTimeMillis()) {
                return true;
            }
            banned.remove(name);
        }
        return false;
    }

    /**
     * Sends a message to all players in the chat.
     *
     * @param player
     *            The player sending the message.
     * @param message
     *            The message to send.
     */
    public void message(Player player, String message) {
        if (player.getLocks().isLocked("cc_message")) {
            return;
        }
        player.getLocks().lock("cc_message", 1);
        if (!player.getName().equals(owner) && !player.getDetails().getRights().isAdministrator()) {
            ClanRank rank = getRank(player);
            if (rank.ordinal() < messageRequirement.ordinal()) {
                player.getActionSender()
                    .sendMessage("You do not have a high enough rank to talk in this clan channel.");
                return;
            }
        }
        if (SystemManager.isActive()) {
            World.submit(new ClanChatMessage(player, owner, message));
        }
        for (Player p : players) {
            if (p != null) {
                PacketRepository.send(CommunicationMessage.class, new MessageContext(p, player,
                    MessageContext.CLAN_MESSAGE, message.substring(1, message.length())));
            }
        }

        if (owner.equalsIgnoreCase("help")) {
            DiscordBot.getBot().sendMessage(DiscordConstants.HELP_CC_CHANNEL_ID, "<:dev:234301795352969217>**" + player.getName() + "**: " + message.substring(1, message.length()));
        }

    }

    /**
     * Forces a clan chat message with a name.
     *
     * @param name
     *            The name that the message is sent by.
     * @param message
     *            The message.
     * @param chatIcon
     *            The chat icon.
     * @param clanCommunication
     *            The clan.
     */
    public static void forceMessage(String name, String message, int chatIcon, ClanCommunication clanCommunication) {
        if (clanCommunication == null) {
            return;
        }
        for (Player player : clanCommunication.getPlayers()) {
            if (player == null) {
                continue;
            }
            PacketBuilder packetBuilder = new PacketBuilder(217, PacketHeader.BYTE);
            packetBuilder.putLong(TextUtils.stringToLong(name));
            packetBuilder.put((byte) 0);
            packetBuilder.putLong(TextUtils.stringToLong(clanCommunication.getName()));
            packetBuilder.putShort(new Random().nextInt(0xFFFF));
            packetBuilder.putTri(0);
            packetBuilder.put((byte) chatIcon);
            packetBuilder.putString(message);
            player.getSession().write(packetBuilder);
        }
    }

    /**
     * Kicks a player from this chat.
     *
     * @param player
     *            the player.
     * @param target
     *            the victim.
     */
    public void kick(Player player, Player target) {
        ClanRank rank = getRank(player);
        if (!player.getDetails().getRights().isAdministrator() && rank.ordinal() < kickRequirement.ordinal()) {
            player.getActionSender().sendMessage("You do not have a high enough rank to kick in this clan channel.");
            return;
        }
        if (target.getName().equals(owner)) {
            player.getActionSender().sendMessage("You can't kick the owner of this clan channel.");
            return;
        }
        //for (Player p : players) {
        // TODO 317 CLAN KICK
        // PacketRepository.send(CommunicationMessage.class, new
        // MessageContext(p, player, MessageContext.CLAN_MESSAGE,
        // "[Attempting to kick/ban " + target.getUsername() + " from this
        // Friend Chat.]"));
        //}
        leave(target, true, true);
        target.getCommunication().setClan(null);
        banned.put(target.getName(), System.currentTimeMillis() + (3_600_000));
        target.getActionSender().sendMessage("You have been kicked from the channel.");
    }

    /**
     * Represents the method to leave a clan.
     *
     * @param player
     *            the player.
     * @param remove
     *            If the player should be removed from the list.
     * @param exitFully
     */
    public void leave(Player player, boolean remove, boolean exitFully) {
        if (remove) {
            players.remove(player);
            update();
            if (players.size() < 1) {
                banned.clear();
            }
        }
        PacketRepository.send(UpdateClanChat.class, new ClanContext(player, this, true));
        player.getActionSender().sendMessage("You have left the channel.");
        if (clanWar != null) {
            clanWar.fireEvent("leavefc", player);
        }
        if (exitFully) {
            player.getSavedData().getGlobalData().setLastClanChat(null);
        }
    }

    /**
     * Ranks a {@link org.gielinor.game.system.communication.ClanMember} of this
     * chat.
     *
     * @param name
     *            The name of the member.
     * @param clanRank
     *            The rank to set.
     */
    public void rank(String name, ClanRank clanRank) {
        boolean update;
        ClanMember clanMember = null;
        int index = 0;
        for (ClanMember clanMember1 : clanMembers) {
            if (clanMember1.getUsername().equalsIgnoreCase(name)) {
                clanMember = clanMember1;
                break;
            }
            index++;
        }
        if (clanMember == null) {
            return;
        }
        if (clanRank == ClanRank.ANYONE) {
            update = clanMembers.remove(clanMember);
        } else {
            clanMember.setClanRank(clanRank);
            update = clanMembers.set(index, clanMember).getClanRank() == clanRank;
        }
        if (update) {
            update();
        }
    }

    /**
     * Updates the clan chat for all players in this clan.
     */
    public void update() {
        for (Player p : players) {
            PacketRepository.send(UpdateClanChat.class, new ClanContext(p, this, false));
        }
    }

    /**
     * Gets the rank of the player.
     *
     * @param player
     *            The player.
     * @return The clan rank.
     */
    public ClanRank getRank(Player player) {
        if (getClanMember(player.getPidn()) == null) {
            return ClanRank.NO_ONE;
        }
        ClanRank clanRank = getClanMember(player.getPidn()).getClanRank();
        if (player.getDetails().getRights().isAdministrator() && !player.getName().equals(owner)) {
            return ClanRank.NO_ONE;
        }
        if (clanRank == null) {
            if (player.getName().equals(owner)) {
                return ClanRank.OWNER;
            }
            return ClanRank.ANYONE;
        }
        return clanRank;
    }

    /**
     * Opens the clan settings for the player.
     *
     * @param player
     *            The player.
     */
    public static void openSettings(Player player) {
        player.getInterfaceState().open(new Component(32320));
        ClanCommunication clanCommunication = get(player.getName());
        if (clanCommunication == null) {
            clanCommunication = create(player);
        }
        if (clanCommunication != null) {
            clanCommunication.updateSettings(player);
        }
    }

    /**
     * Updates the clan settings for the player.
     *
     * @param player
     *            The player.
     */
    public void updateSettings(Player player) {
        player.getActionSender().sendString(name, 32343);
        player.getActionSender().sendString(joinRequirement.getInfo(), 32344);
        player.getActionSender().sendString(messageRequirement.getInfo(), 32345);
        player.getActionSender().sendString(kickRequirement.getInfo(), 32346);
    }

    /**
     * Creates a new clan chat.
     *
     * @param owner
     *            The player creating the chat.
     * @return The created clan.
     */
    public static ClanCommunication create(Player owner) {
        ClanCommunication clanCommunication = new ClanCommunication(owner.getName(), owner.getPidn());
        for (int pidn : owner.getCommunication().getContacts().keySet()) {
            clanCommunication.clanMembers.add(new ClanMember(pidn, owner.getCommunication().getContacts().get(pidn)));
        }
        CLAN_REPOSITORY.put(owner.getName().toLowerCase(), clanCommunication);
        return clanCommunication;
    }

    /**
     * Loads the clan data.
     *
     * @param owner
     *            The owner of the clan to load.
     * @return The clan data.
     */
    public static ClanCommunication get(String owner) {
        return get(owner, false);
    }

    /**
     * Loads clans from the database.
     */
    public static void load() {
        log.info("Loading player clans...");
        try (Connection connection = DataSource.getGameConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "SELECT * FROM player_clan LEFT JOIN player_detail ON player_clan.pidn = player_detail.pidn")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("username") == null || resultSet.getInt("pidn") == -1) {
                    continue;
                }
                ClanCommunication clanCommunication = new ClanCommunication(resultSet.getString("username"),
                    resultSet.getInt("pidn"));
                clanCommunication.setName(resultSet.getString("name"));
                clanCommunication.setJoinRequirement(ClanRank.values()[resultSet.getByte("join_rank")]);
                clanCommunication.setMessageRequirement(ClanRank.values()[resultSet.getByte("message_rank")]);
                clanCommunication.setKickRequirement(ClanRank.values()[resultSet.getByte("kick_rank")]);
                clanCommunication.setLootRequirement(ClanRank.NO_ONE);
                ClanCommunication.getClanRepository().put(clanCommunication.getOwner(), clanCommunication);
                clanCommunication.getClanMembers().add(
                    new ClanMember(resultSet.getInt("pidn"), resultSet.getString("username"), ClanRank.OWNER));
                try (PreparedStatement stmt2 = connection.prepareStatement(
                    "SELECT f.pidn, f.friend_pidn, f.clan_rank, d.username " +
                        "FROM player_friend_list f, player_detail d " +
                        "WHERE f.pidn=? AND f.friend_pidn=d.pidn")) {
                    stmt2.setInt(1, resultSet.getInt("pidn"));
                    ResultSet res2 = stmt2.executeQuery();
                    while (res2.next()) {
                        clanCommunication.getClanMembers().add(new ClanMember(res2.getInt("friend_pidn"),
                            res2.getString("username"), ClanRank.values()[res2.getInt("clan_rank")]));
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Exception while loading player clans.", ex);
        }
        log.info("Loaded {} player clans.", CLAN_REPOSITORY.size());
    }

    /**
     * Loads the clan data.
     *
     * @param owner
     *            The owner of the clan to load.
     * @param create
     *            If the clan should be created if it doesn't exist.
     * @return The clan data.
     */
    public static ClanCommunication get(String owner, boolean create) {
        ClanCommunication clan = CLAN_REPOSITORY.get(owner.toLowerCase());
        return clan == null ? (create ? create(Repository.getPlayerByName(owner)) : null) : clan;
    }

    /**
     * Gets the list of players currently in the clan.
     *
     * @return The list of players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the joinRequirement.
     *
     * @return The joinRequirement.
     */
    public ClanRank getJoinRequirement() {
        return joinRequirement;
    }

    /**
     * Sets the joinRequirement.
     *
     * @param joinRequirement
     *            The joinRequirement to set.
     */
    public void setJoinRequirement(ClanRank joinRequirement) {
        this.joinRequirement = joinRequirement;
        clean(false);
    }

    /**
     * Gets the messageRequirement.
     *
     * @return The messageRequirement.
     */
    public ClanRank getMessageRequirement() {
        return messageRequirement;
    }

    /**
     * Sets the messageRequirement.
     *
     * @param messageRequirement
     *            The messageRequirement to set.
     */
    public void setMessageRequirement(ClanRank messageRequirement) {
        this.messageRequirement = messageRequirement;
    }

    /**
     * Gets the kickRequirement.
     *
     * @return The kickRequirement.
     */
    public ClanRank getKickRequirement() {
        return kickRequirement;
    }

    /**
     * Sets the kickRequirement.
     *
     * @param kickRequirement
     *            The kickRequirement to set.
     */
    public void setKickRequirement(ClanRank kickRequirement) {
        this.kickRequirement = kickRequirement;
        update();
    }

    /**
     * Gets the lootRequirement.
     *
     * @return The lootRequirement.
     */
    public ClanRank getLootRequirement() {
        return lootRequirement;
    }

    /**
     * Sets the lootRequirement.
     *
     * @param lootRequirement
     *            The lootRequirement to set.
     */
    public void setLootRequirement(ClanRank lootRequirement) {
        this.lootRequirement = lootRequirement;
    }

    /**
     * Gets the {@link java.util.List} of
     * {@link org.gielinor.game.system.communication.ClanMember}s.
     *
     * @return The list.
     */
    public List<ClanMember> getClanMembers() {
        return clanMembers;
    }

    /**
     * Gets a {@link org.gielinor.game.system.communication.ClanMember} by name.
     *
     * @param username
     *            The username.
     * @return The {@code ClanMember}.
     */
    public ClanMember getClanMember(String username) {
        for (ClanMember clanMember : clanMembers) {
            if (clanMember == null || username == null || clanMember.getUsername() == null) {
                continue;
            }
            if (clanMember.getUsername().equalsIgnoreCase(username)) {
                return clanMember;
            }
        }
        return null;
    }

    /**
     * Gets a {@link org.gielinor.game.system.communication.ClanMember} by pidn.
     *
     * @param pidn
     *            The pidn.
     * @return The {@code ClanMember}.
     */
    public ClanMember getClanMember(int pidn) {
        for (ClanMember clanMember : clanMembers) {
            if (clanMember.getPidn() == pidn) {
                return clanMember;
            }
        }
        return null;
    }

    /**
     * Gets the owner.
     *
     * @return The owner.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the clanWar.
     *
     * @return The clanWar.
     */
    public ActivityPlugin getClanWar() {
        return clanWar;
    }

    /**
     * Sets the clanWar.
     *
     * @param clanWar
     *            The clanWar to set.
     */
    public void setClanWar(ActivityPlugin clanWar) {
        this.clanWar = clanWar;
    }

    /**
     * Gets the {@link #CLAN_REPOSITORY}.
     *
     * @return The clan repository.
     */
    public static Map<String, ClanCommunication> getClanRepository() {
        return CLAN_REPOSITORY;
    }

}
