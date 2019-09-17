package org.gielinor.game.system.communication;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.system.SystemManager;
import org.gielinor.game.world.World;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.mqueue.message.impl.PrivateChatMessage;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContactContext;
import org.gielinor.net.packet.context.MessageContext;
import org.gielinor.net.packet.out.CommunicationMessage;
import org.gielinor.net.packet.out.ContactPackets;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.utilities.buffer.ByteBufferUtils;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles communication between players.
 *
 * @author Emperor
 */
public final class CommunicationSystem implements SavingModule {

    private static final Logger log = LoggerFactory.getLogger(CommunicationSystem.class);

    /**
     * The maximum list size.
     */
    public static final int MAX_LIST_SIZE = 200;

    /**
     * The player.
     */
    private final Player player;

    /**
     * The player's social contacts.
     */
    private final Map<Integer, String> contacts = new HashMap<>(MAX_LIST_SIZE);

    /**
     * The player's blocked contacts.
     */
    private final Map<Integer, String> blocked = new HashMap<>(MAX_LIST_SIZE);

    /**
     * The clan the player has joined.
     */
    private ClanCommunication clan;

    /**
     * Constructs a new {@code CommunicationSystem} {@code Object}.
     *
     * @param player The player.
     */
    public CommunicationSystem(Player player) {
        this.player = player;
    }

    @Override
    public void save(ByteBuffer buffer) {
        buffer.put((byte) contacts.size());
        for (String s : contacts.values()) {
            ByteBufferUtils.putString(s, buffer);
        }
        buffer.put((byte) blocked.size());
        for (String s : blocked.values()) {
            ByteBufferUtils.putString(s, buffer);
        }
        boolean hasClan = clan != null && clan != ClanCommunication.DEFAULT;
        buffer.put((byte) (hasClan ? 1 : 0));
        if (hasClan) {
            ByteBufferUtils.putString(clan.getOwner(), buffer);
        }
    }

    @Override
    public void parse(ByteBuffer buffer) {
        int size = buffer.get() & 0xFF;
        for (int i = 0; i < size; i++) {
            contacts.put(i, ByteBufferUtils.getString(buffer));
        }
        size = buffer.get() & 0xFF;
        for (int i = 0; i < size; i++) {
            blocked.put(i, ByteBufferUtils.getString(buffer));
        }
        if (buffer.get() == 1) {
            clan = ClanCommunication.get(ByteBufferUtils.getString(buffer));
        } else {
            clan = ClanCommunication.DEFAULT;
        }
    }

    /**
     * Checks if a save is needed.
     *
     * @return <code>True</code> if so.
     */
    public boolean saveNeeded() {
        return !contacts.isEmpty() || !blocked.isEmpty() || clan != null;
    }

    /**
     * Sends a message to a player.
     *
     * @param name    The name of the recipient.
     * @param message The message to send.
     */
    public void message(String name, String message, byte[] messageBytes, int messageLength) {
        Player otherPlayer = Repository.getPlayerByName(name);
        if (otherPlayer == null || !otherPlayer.isActive() || !otherPlayer.getCommunication().showActive(player)) {
            player.getActionSender().sendMessage("That player is currently offline.");
            return;
        }
        if (!hasContact(otherPlayer.getDetails().getPidn())) {
            log.warn("Failed to send PM from [{}] to [{}].", player.getName(), name);
            return; //Shouldn't happen
        }
        if (player.getDetails().getPortal().getMute().isPunished()) {
            return;
        }
        if (SystemManager.isActive()) {
            World.submit(new PrivateChatMessage(player, otherPlayer, message));
        }
        // PacketRepository.send(CommunicationMessage.class, new MessageContext(player, otherPlayer, MessageContext.SEND_MESSAGE, messageBytes, messageLength));
        PacketRepository.send(CommunicationMessage.class, new MessageContext(otherPlayer, player, MessageContext.RECEIVE_MESSAGE, messageBytes, messageLength));
    }

    /**
     * Synchronizes the contact lists.
     *
     * @param joinClan Whether or not to join a clan chat.
     */
    public void sync(boolean joinClan) {
        if (player.getSession() == null) {
            return;
        }
        if (player.getSettings().getPrivateChatSetting() != 2) {
            notifyPlayers(true);
        }
        PacketRepository.send(ContactPackets.class, new ContactContext(player, ContactContext.UPDATE_STATE_TYPE));
        PacketRepository.send(ContactPackets.class, new ContactContext(player, ContactContext.IGNORE_LIST_TYPE));
        for (String name : contacts.values()) {
            Player p = Repository.getPlayerByName(name);
            PacketRepository.send(ContactPackets.class, new ContactContext(player, name, p != null && showActive(p)));
        }
        if (joinClan && clan != null && !player.isArtificial()) {
            clan.enter(player);
        }
    }

    /**
     * Synchronizes the contact lists.
     */
    public void sync() {
        sync(true);
    }

    /**
     * Notifies all other players.
     *
     * @param online      If this player is online.
     * @param chatSetting if it was a chat setting changed.
     */
    public void notifyPlayers(boolean online, boolean chatSetting) {
        if (player.getSession() == null) {
            return;
        }
        for (Player p : Repository.getPlayers()) {
            if (p == player || !p.isActive()) {
                continue;
            }
            if (p.getCommunication().hasContact(player.getPidn())) {
                boolean view = online && p.getCommunication().showActive(player);
                PacketRepository.send(ContactPackets.class, new ContactContext(p, player.getName(), view));
            }
        }
        if (!online && !chatSetting && clan != null) {
            clan.leave(player, true, false);
        }
    }

    /**
     * Notifies all other players.
     *
     * @param online If this player is online.
     */
    public void notifyPlayers(boolean online) {
        if (player.getSession() == null) {
            return;
        }
        notifyPlayers(online, false);
    }

    /**
     * Checks if the player is on this friends list.
     *
     * @param pidn The player's PIDN.
     * @return <code>True</code> if so.
     */
    public boolean hasContact(int pidn) {
        return contacts.get(pidn) != null;
    }

    /**
     * Adds a new contact.
     *
     * @param name The name of the player to add.
     */
    public void add(String name) {
        if (name.equalsIgnoreCase(player.getName())) {
            player.getActionSender().sendMessage("You can't add yourself to your own friend list.");
            return;
        }
        if (contacts.size() >= MAX_LIST_SIZE) {
            player.getActionSender().sendMessage("Your friend list is full.");
            return;
        }
        if (contacts.containsValue(name)) {
            player.getActionSender().sendMessage(TextUtils.formatDisplayName(name) + " is already on your friend list.");
            return;
        }
        int friendPidn = Repository.getPidn(name);
        if (friendPidn == -1) {
            player.getActionSender().sendMessage("Unable to add friend - unknown player.");
            return;
        }

        ClanCommunication clan = ClanCommunication.get(player.getName());
        if (clan != null) {
            clan.getClanMembers().add(new ClanMember(friendPidn, name));
        }
        contacts.put(friendPidn, name);
        Player target = Repository.getPlayerByName(name);
        if (target != null) {
            if (showActive(target)) {
                PacketRepository.send(ContactPackets.class, new ContactContext(player, name, true));
            }
            if (player.getSettings().getPrivateChatSetting() == 1 && target.getCommunication().showActive(player)) {
                PacketRepository.send(ContactPackets.class, new ContactContext(target, player.getName(), true));
            }
        }
    }

    /**
     * Removes a contact.
     *
     * @param name The name of the player to remove.
     */
    public void remove(String name) {
        contacts.remove(getPidnByName(name));
        ClanCommunication clan = ClanCommunication.get(player.getName());
        if (clan != null) {
            clan.rank(name, ClanRank.ANYONE);
        }
        if (player.getSettings().getPrivateChatSetting() == 1) {
            Player target = Repository.getPlayerByName(name);
            if (target != null) {
                PacketRepository.send(ContactPackets.class, new ContactContext(target, player.getName(), false));
            }
        }
    }

    /**
     * Gets a pidn from the player's contact list by name.
     *
     * @param name The name.
     * @return The pidn.
     */
    public int getPidnByName(String name) {
        for (Map.Entry<Integer, String> contact : contacts.entrySet()) {
            if (contact.getValue().equalsIgnoreCase(name)) {
                return contact.getKey();
            }
        }
        return -1;
    }

    /**
     * Blocks a player from social contact.
     *
     * @param name The name of the player to block.
     */
    public void block(String name) {
        if (blocked.size() >= MAX_LIST_SIZE) {
            player.getActionSender().sendMessage("Your ignore list is full.");
            return;
        }
        if (blocked.containsValue(name)) {
            player.getActionSender().sendMessage(TextUtils.formatDisplayName(name) + " is already on your ignore list.");
            return;
        }
        int ignorePidn = Repository.getPidn(name);
        if (ignorePidn == -1) {
            player.getActionSender().sendMessage("Unable to add ignore - unknown player.");
            return;
        }
        blocked.put(ignorePidn, name);
        Player target = Repository.getPlayerByName(name);
        if (target != null && target.getCommunication().hasContact(player.getPidn())) {
            PacketRepository.send(ContactPackets.class, new ContactContext(target, player.getName(), false));
        }
    }

    /**
     * Removes a player from the blocked contacts list.
     *
     * @param name The name of the player to unblock.
     */
    public void unblock(String name) {
        blocked.remove(name);
        Player target = Repository.getPlayerByName(name);
        if (target != null && target.getCommunication().hasContact(player.getPidn())) {
            PacketRepository.send(ContactPackets.class, new ContactContext(target, player.getName(), target.getCommunication().showActive(player)));
        }
    }

    /**
     * Checks if the target should be shown as online.
     *
     * @param name The target's name.
     * @return <code>True</code> if so.
     */
    public boolean showActive(String name) {
        Player p = Repository.getPlayerByName(name);
        if (p != null) {
            return showActive(p);
        }
        return false;
    }

    /**
     * Checks if the target should be shown as online.
     *
     * @param target The target.
     * @return {@True} if so.
     */
    public boolean showActive(Player target) {
        if (target.getCommunication().getBlocked().containsValue(player.getName())) {
            return false;
        }
        switch (target.getSettings().getPrivateChatSetting()) {
            case 1:
                return target.getCommunication().hasContact(player.getPidn());
            case 2:
                return false;
        }
        return true;
    }

    /**
     * Gets the current communication server state.
     *
     * @return The state.
     */
    public int getServerState() {
        return 2;
    }

    /**
     * Gets the contacts.
     *
     * @return The contacts.
     */
    public Map<Integer, String> getContacts() {
        return contacts;
    }

    /**
     * Gets the blocked.
     *
     * @return The blocked.
     */
    public Map<Integer, String> getBlocked() {
        return blocked;
    }

    /**
     * Gets the clan.
     *
     * @return The clan.
     */
    public ClanCommunication getClan() {
        return clan;
    }

    /**
     * Sets the clan.
     *
     * @param clan The clan to set.
     */
    public void setClan(ClanCommunication clan) {
        this.clan = clan;
    }

}
